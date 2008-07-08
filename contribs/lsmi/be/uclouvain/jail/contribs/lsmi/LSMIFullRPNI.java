package be.uclouvain.jail.contribs.lsmi;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;
import java.util.Stack;

import be.uclouvain.jail.Jail;
import be.uclouvain.jail.algo.fa.walk.PTADepthFirstWalker;
import be.uclouvain.jail.algo.fa.walk.PTADepthFirstWalker.IVisitor;
import be.uclouvain.jail.algo.induct.processor.ForwardLabelProcessor;
import be.uclouvain.jail.algo.lsm.LSMAlgo;
import be.uclouvain.jail.algo.lsm.LSMAlgoInput;
import be.uclouvain.jail.algo.lsm.LSMAlgoResult;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.ISample;
import be.uclouvain.jail.fa.IString;
import be.uclouvain.jail.fa.IWalkInfo;
import be.uclouvain.jail.fa.impl.GraphDFA;
import be.uclouvain.jail.fa.utils.DefaultSample;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.IUserInfoPopulator;
import be.uclouvain.jail.vm.toolkits.AutomatonFacade;
import be.uclouvain.jail.vm.toolkits.GraphFacade;
import be.uclouvain.jail.vm.toolkits.InductionFacade;

/**
 * Executes all algorithms.
 * 
 * @author blambeau
 */
public class LSMIFullRPNI {

	/** Randomizer. */
	private Random r = Jail.randomizer();
	
	/** Database to use. */
	private LSMIDatabase db;
	
	/** Sample proportions. */
	private double[] sprops = new double[]{0.03, 0.0625, 0.125, 0.25, 0.5, 1.0};
	
	/** Labeling proportions. */
	private double[] lprops = new double[]{1.0};
	
	/** Number of sample spliting to made. */
	private int number = 1;
	
	/** Creates a DFA generator based on a folder. */
	public LSMIFullRPNI(File folder) {
		this.db = new LSMIDatabase(folder);
	}

	/** Checks that a result is valid according to a sample. */
	public void assertValidResult(String algo, ISample<?> sample, IDFA result) {
		try {
			for (IString<?> s: sample) {
				IWalkInfo<?> info = s.walk(result);
				boolean accepted = info.isFullyIncluded() && info.getIncludedPart().isAccepted();
				boolean positive = s.isPositive();
				assert (accepted == positive) : "Valid only when accepted iif positive";
			}
		} catch (AssertionError e) {
			db.addFailure(algo, sample, e);
		}
	}
	
	/** Tests RPNI with LSMI extension. */
	private IDFA lsm(ISample<String> sample, double sprop, double lprop, int slid, int llid) {
		sample = allaccept(sample);
		IDFA pta = (IDFA) sample.adapt(IDFA.class);
		IDFA source = mergeAllSame(pta);
		
		LSMAlgoInput input = new LSMAlgoInput(source);
		LSMAlgoResult result = new LSMAlgoResult();

		// execute and log time
		long t1 = System.currentTimeMillis();
		new LSMAlgo().execute(input,result);
		long t2 = System.currentTimeMillis();
		
		IDFA target = result.resultDFA();
		target = AutomatonFacade.uncomplement(target);
		
		assertValidResult("lsm",sample,target);
		
		// set attributes
		db.setAttribute(target,"INDUCT_algo","lsm");
		db.setAttribute(target, "INDUCT_sprop", sprop);
		db.setAttribute(target, "INDUCT_slid", slid);
		db.setAttribute(target, "INDUCT_lprop", lprop);
		db.setAttribute(target, "INDUCT_llid", llid);
		db.setAttribute(target, "INDUCT_time", t2-t1);
		return target;
	}

	/** Merge all same states. */
	public IDFA mergeAllSame(IDFA source) {
		LSMAlgoInput input = new LSMAlgoInput(source);
		LSMAlgoResult result = new LSMAlgoResult();
		result.getUserInfoHandler().getVertexAggregator().allsame("toallsame");
		new LSMAlgo().execute(input,result);
		return result.resultDFA();
	}
	
	/** Copies a sample. */
	private ISample<String> allaccept(ISample<String> s) {
		IDFA dfa = (IDFA) s.adapt(IDFA.class);
		return new DefaultSample<String>(AutomatonFacade.allaccepting(dfa));
	}
	
	/** Executes the algorithms on a sample. */
	private void execute(IDFA target, ISample<String> sample) throws IOException {
		GraphFacade.identify(target.getGraph(),"id",null);
		
		long total = (sprops.length*number)*(2+(lprops.length*number));
		long count = 1;
		
		// for each proportion
		for (double sprop: sprops) {
			// for 1 to number
			for (int i=0; i<number; i++) {
				// choose some strings
				ISample<String> chosen = InductionFacade.choose(sample,sprop);

				// for each labeling propotion
				for (double lprop: lprops) {
					// number times
					for (int j=0; j<number; j++) {
						// labelize the DFA and the sample
						IDFA lTarget = labelize(target, lprop);
						ISample<String> lSample = labelize(chosen, lTarget);
						
						//AutomatonFacade.debug(lTarget, (IDFA)lSample.adapt(IDFA.class));
						
						// execute rpni with lsmi extension
						{
							System.out.println((count++) + "/" + total + ": lSMIed RPNI on " + db.toString(target) 
									         + " with props " + sprop + "/" + lprop 
									         + ":" + i + "/" + j);
							try {
								IDFA result = lsm(lSample,sprop,lprop,i,j);
								db.addResult(target, sample, result);
							} catch (Exception ex) {
								db.addFailure("lsm",lSample, ex);
							}  catch (AssertionError ex) {
								db.addFailure("lsm",lSample, ex);
							}
						}
						
					}
				}
			}
		}
	}

	/** Randomly labelizes a DFA. */
	private IDFA labelize(IDFA target, final double lprop) {
		IDirectedGraph g = target.getGraph();
		IDirectedGraph copy = GraphFacade.copy(g,new IUserInfoPopulator<IUserInfo>() {
			public void populate(IUserInfo target, IUserInfo source) {
				if (r.nextDouble() <= lprop) {
					target.setAttribute("class", source.getAttribute("id"));
				}
			}
		},null);
		return new GraphDFA(copy);
	}

	/** Labelizes a sample. */
	private ISample<String> labelize(ISample<String> chosen, final IDFA target) {
		// Copy PTA underlying sample
		IDFA pta2 = (IDFA) chosen.adapt(IDFA.class);
		final IDFA pta = AutomatonFacade.copy(pta2, null, null);
		
		final IDirectedGraph ptag = pta.getGraph();
		final IDirectedGraph targetg = target.getGraph();
		
		// make a depth first walk and decorate
		new PTADepthFirstWalker(pta).execute(new IVisitor() {

			/** State stack. */
			private Stack<Object> stack = new Stack<Object>();
			
			/** Handles label on PTA state. */
			private void handleState(Object ptaState, Object dfaState) {
				// extract DFA state clazz
				Object clazz = targetg.getVertexInfo(dfaState).getAttribute("class");
				// set it on the PTA state when found
				if (clazz != null) {
					ptag.getVertexInfo(ptaState).setAttribute("class", clazz);
				}
			}
			
			/** When entering a state. */
			public boolean entering(Object state, Object edge) {
				if (edge == null) {
					Object initial = target.getInitialState();
					handleState(state,initial);
					stack.push(initial);
				} else {
					// current state on the DFA
					Object current = stack.peek();
					// letter on the incoming edge
					Object letter = pta.getEdgeLetter(edge);
					// next state in the DFA
					Object next = target.getOutgoingEdge(current, letter);
					if (next == null) {
						// no such state, just return
						return false;
					} else {
						Object nextState = targetg.getEdgeTarget(next);
						handleState(state,nextState);
						// push the current state
						stack.push(nextState);
					}
				}
				return true;
			}

			/** When leaving a state. */
			public boolean leaving(Object state, Object edge, boolean recurse) {
				if (recurse) { stack.pop(); }
				return false;
			}
			
		});
		
		ForwardLabelProcessor.Input input = new ForwardLabelProcessor.Input(pta, "class", "toallsame");
		new ForwardLabelProcessor().process(input);
		return new DefaultSample<String>(pta);
	}

	/** Execute all algorithms. */
	private void execute() throws IOException {
		// for each DFA
		Iterator<IDFA> dfas = db.getAllDFAs();
		while (dfas.hasNext()) {
			IDFA dfa = dfas.next();
			
			// for each sample on this dfa
			Iterator<ISample<String>> samples = db.getSamples(dfa);
			while (samples.hasNext()) {
				ISample<String> sample = samples.next();
				execute(dfa,sample);
			}
		}		
	}
	
	/** Main method. */
	public static void main(String[] args) throws IOException {
		File folder = null;
		if (args.length == 1) {
			folder = new File(args[0]);
		} else {
			folder = new File(".");
		}
		new LSMIFullRPNI(folder).execute();
	}
	
}
