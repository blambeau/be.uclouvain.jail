package be.uclouvain.jail.contribs.lsmi;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;
import java.util.Stack;

import be.uclouvain.jail.Jail;
import be.uclouvain.jail.algo.commons.Unable;
import be.uclouvain.jail.algo.fa.walk.PTADepthFirstWalker;
import be.uclouvain.jail.algo.fa.walk.PTADepthFirstWalker.IVisitor;
import be.uclouvain.jail.algo.induct.extension.LSMIExtension;
import be.uclouvain.jail.algo.induct.internal.BlueFringeAlgo;
import be.uclouvain.jail.algo.induct.internal.DefaultInductionAlgoInput;
import be.uclouvain.jail.algo.induct.internal.IInductionAlgoInput;
import be.uclouvain.jail.algo.induct.internal.RPNIAlgo;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.ISample;
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
public class LSMIAlgorithms {

	/** Randomizer. */
	private Random r = Jail.randomizer();
	
	/** Database to use. */
	private LSMIDatabase db;
	
	/** Sample proportions. */
	//private double[] sprops = new double[]{0.1};
	private double[] sprops = new double[]{0.03, 0.0625, 0.125, 0.25, 0.5, 1.0};
	
	/** Labelong proportions. */
	private double[] lprops = new double[]{0.0625, 0.125, 0.25, 0.50, 1.0};
	//private double[] lprops = new double[]{0.50};
	
	/** Number of sample spliting to made. */
	private int number = 10;
	
	/** Creates a DFA generator based on a folder. */
	public LSMIAlgorithms(File folder) {
		this.db = new LSMIDatabase(folder);
	}
	
	/** Tests an induction algorithm. */
	public IDFA rpni(IInductionAlgoInput input, double sprop, double lprop, int slid, int llid) {
		// execute and log time
		long t1 = System.currentTimeMillis();
		IDFA result = new RPNIAlgo().execute(input);
		long t2 = System.currentTimeMillis();
		
		// uncomplement
		result = AutomatonFacade.uncomplement(result);

		// set attributes
		db.setAttribute(result,"INDUCT_algo","rpni");
		db.setAttribute(result, "INDUCT_sprop", sprop);
		db.setAttribute(result, "INDUCT_slid", slid);
		db.setAttribute(result, "INDUCT_lprop", lprop);
		db.setAttribute(result, "INDUCT_llid", llid);
		db.setAttribute(result, "INDUCT_time", t2-t1);
		return result;
	}

	/** Tests an induction algorithm. */
	public IDFA bluefringe(IInductionAlgoInput input, double sprop, double lprop, int slid, int llid) {
		// execute and log time
		long t1 = System.currentTimeMillis();
		IDFA result = new BlueFringeAlgo().execute(input);
		long t2 = System.currentTimeMillis();

		// uncomplement
		result = AutomatonFacade.uncomplement(result);

		// set attributes
		db.setAttribute(result,"INDUCT_algo","blue-fringe");
		db.setAttribute(result, "INDUCT_sprop", sprop);
		db.setAttribute(result, "INDUCT_slid", slid);
		db.setAttribute(result, "INDUCT_lprop", lprop);
		db.setAttribute(result, "INDUCT_llid", llid);
		db.setAttribute(result, "INDUCT_time", t2-t1);
		return result;
	}
	
	/** Copies a sample. */
	private ISample<String> allaccept(ISample<String> s) {
		IDFA dfa = (IDFA) s.adapt(IDFA.class);
		return new DefaultSample<String>(AutomatonFacade.allaccepting(dfa));
	}
	
	/** Tests RPNI algorithm on a sample, with known target. */
	public IDFA rpni(ISample<String> sample, double sprop, int slid) {
		sample = allaccept(sample);
		IInductionAlgoInput input = new DefaultInductionAlgoInput(sample);
		return rpni(input, sprop, 0.0, slid, 0);
	}
	
	/** Tests BlueFringe algorithm. */
	public IDFA bluefringe(ISample<String> sample, double sprop, int llid) {
		sample = allaccept(sample);
		IInductionAlgoInput input = new DefaultInductionAlgoInput(sample);
		return bluefringe(input,sprop,0.0,llid,0);
	}
	
	/** Tests RPNI with LSMI extension. */
	private IDFA rpnilsmi(ISample<String> sample, double sprop, double lprop, int slid, int llid) {
		sample = allaccept(sample);
		DefaultInductionAlgoInput input = new DefaultInductionAlgoInput(sample);
		input.setRepresentorAttr("class");
		input.setUnknown(null);
		input.setExtension(new LSMIExtension());
		try {
			return rpni(input, sprop, lprop, slid, llid);
		} catch (Unable ex) {
			try {
				AutomatonFacade.debug((IDFA)sample.adapt(IDFA.class));
			} catch (IOException e) {}
			throw ex;
		}
	}

	/** Tests blue-fringe with LSMI extension. */
	private IDFA bluefringelsmi(ISample<String> sample, double sprop, double lprop, int slid, int llid) {
		sample = allaccept(sample);
		DefaultInductionAlgoInput input = new DefaultInductionAlgoInput(sample);
		input.setRepresentorAttr("class");
		input.setUnknown("");
		input.setExtension(new LSMIExtension());
		try {
			return bluefringe(input, sprop, lprop, slid, llid);
		} catch (Unable ex) {
			try {
				AutomatonFacade.debug((IDFA)sample.adapt(IDFA.class));
			} catch (IOException e) {}
			throw ex;
		}
	}

	/** Executes the algorithms on a sample. */
	private void execute(IDFA target, ISample<String> sample) throws IOException {
		GraphFacade.identify(target.getGraph(),"id",null);
		
		// for each proportion
		for (double sprop: sprops) {
			// for 1 to number
			for (int i=0; i<number; i++) {
				// choose some strings
				ISample<String> chosen = InductionFacade.choose(sample,sprop);

				// execute rpni
				{
					System.out.println("Classic RPNI on " + db.toString(target) + " with prop " + sprop + ":" + i);
					IDFA result = rpni(chosen,sprop,i);
					db.addResult(target, sample, result);
				}
				
				// execute blue fringe
				{
					System.out.println("Classic BlueFringe on " + db.toString(target) + " with prop " + sprop + ":" + i);
					IDFA result = bluefringe(chosen,sprop, i);
					db.addResult(target, sample, result);
				}
				
				// for each labeling propotion
				for (double lprop: lprops) {
					// number times
					for (int j=0; j<number; j++) {
						// labelize the DFA and the sample
						IDFA lTarget = labelize(target, lprop);
						ISample<String> lSample = labelize(chosen, lTarget);
						//AutomatonFacade.show(lTarget, (IDFA) chosen.adapt(IDFA.class), (IDFA) lSample.adapt(IDFA.class));
						
						// execute rpni with lsmi extension
						{
							System.out.println("LSMIed RPNI on " + db.toString(target) 
									         + " with props " + sprop + "/" + lprop 
									         + ":" + i + "/" + j);
							IDFA result = rpnilsmi(lSample,sprop,lprop,i,j);
							db.addResult(target, sample, result);
						}
						
						// execute blue fringe with lsmi extension
						{
							System.out.println("LSMIed BlueFringe on " + db.toString(target) 
							                 + " with props " + sprop + "/" + lprop 
							                 + ":" + i + "/" + j);
							IDFA result = bluefringelsmi(lSample,sprop,lprop,i,j);
							db.addResult(target, sample, result);
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
		new LSMIAlgorithms(folder).execute();
	}
	
}
