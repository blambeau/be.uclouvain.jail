package be.uclouvain.jail.contribs.lsmi;

import java.util.Random;
import java.util.Stack;

import net.chefbe.javautils.adapt.AdaptUtils;
import net.chefbe.javautils.adapt.IAdapter;

import be.uclouvain.jail.Jail;
import be.uclouvain.jail.algo.fa.rand.AbadingoRandomStringsInput;
import be.uclouvain.jail.algo.fa.rand.DefaultRandomStringsResult;
import be.uclouvain.jail.algo.fa.rand.RandomDFAInput;
import be.uclouvain.jail.algo.fa.rand.RandomDFAResult;
import be.uclouvain.jail.algo.fa.rand.RandomStringsAlgo;
import be.uclouvain.jail.algo.fa.walk.PTADepthFirstWalker;
import be.uclouvain.jail.algo.fa.walk.PTADepthFirstWalker.IVisitor;
import be.uclouvain.jail.algo.graph.rand.RandomGraphAlgo;
import be.uclouvain.jail.algo.induct.internal.DefaultInductionAlgoInput;
import be.uclouvain.jail.algo.induct.internal.IInductionAlgoInput;
import be.uclouvain.jail.algo.induct.internal.RPNIAlgo;
import be.uclouvain.jail.algo.induct.processor.ForwardLabelProcessor;
import be.uclouvain.jail.algo.lsm.LSMAlgo;
import be.uclouvain.jail.algo.lsm.LSMAlgoInput;
import be.uclouvain.jail.algo.lsm.LSMAlgoResult;
import be.uclouvain.jail.fa.IAlphabet;
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

public class LSMITest {

	private static Random r = Jail.randomizer();
	
	/** Tests an induction algorithm. */
	public static IDFA rpni(ISample<String> sample) {
		sample = allaccept(sample);
		IInductionAlgoInput input = new DefaultInductionAlgoInput(sample);
		
		// execute and log time
		IDFA result = new RPNIAlgo().execute(input);
		
		// uncomplement
		return AutomatonFacade.uncomplement(result);
	}

	/** Tests RPNI with LSMI extension. */
	private static IDFA lsm(IDFA source) {
		LSMAlgoInput input = new LSMAlgoInput(source);
		LSMAlgoResult result = new LSMAlgoResult();

		// execute and log time
		new LSMAlgo().execute(input,result);
		IDFA target = result.resultDFA();
		target = AutomatonFacade.uncomplement(target);
		
		return target;
	}
	
	/** Merge all same states. */
	public static IDFA mergeAllSame(IDFA source) {
		LSMAlgoInput input = new LSMAlgoInput(source);
		LSMAlgoResult result = new LSMAlgoResult();
		result.getUserInfoHandler().getVertexAggregator().allsame("label");
		new LSMAlgo().execute(input,result);
		return result.resultDFA();
	}
	
	/** Copies a sample. */
	private static ISample<String> allaccept(ISample<String> s) {
		IDFA dfa = (IDFA) s.adapt(IDFA.class);
		return new DefaultSample<String>(AutomatonFacade.allaccepting(dfa));
	}
	
	/** Generates a DFA of a given size. */
	private static IDFA dfa(int size) {
		RandomDFAInput input = new RandomDFAInput();
		input.setAccepting(1.0);
		input.setStateCount(size);
		input.setAlphabetSize(2);
		input.setDepthControl(true);
		input.setMaxTry(1000);
		input.setTolerance(0.01);
		RandomDFAResult result = new RandomDFAResult();
		new RandomGraphAlgo().execute(input,result);
		IDFA dfa = (IDFA) result.adapt(IDFA.class);
		GraphFacade.identify(dfa.getGraph(), "id", null);
		return dfa;
	}

	/** Generates a sample. */
	private static ISample<String> sample(IDFA dfa, int stcount) {
		// create input and set options
		IAlphabet<String> alphabet = dfa.getAlphabet();
		AbadingoRandomStringsInput<String> input = new AbadingoRandomStringsInput<String>(); 
		input.setAlphabet(alphabet);
		input.setMaxTry(100000);
		
		// 2*n^2 strings
		input.setNbStrings(stcount*stcount);

		// length of the strings : 2*log(n)+3
		// where n is the number of states and log is in base 2
		long depth = Math.round(2*(Math.log(stcount)/Math.log(2)) + 3);
		input.setWordLength(new Long(depth).intValue());

		// create sample recipient 
		ISample<String> sample = new DefaultSample<String>(alphabet);
		
		// create algo result and set options
		DefaultRandomStringsResult<String> result = new DefaultRandomStringsResult<String>(sample);
		result.setLabeler(dfa);
		
		// execute algorithm and return sample
		new RandomStringsAlgo().execute(input,result);
		return sample;
	}
	
	/** Randomly labelizes a DFA. */
	private static IDFA labelize(IDFA target, final double lprop) {
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
	private static ISample<String> labelize(ISample<String> chosen, final IDFA target) {
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
		
		ForwardLabelProcessor.Input input = new ForwardLabelProcessor.Input(pta, "class", "label");
		new ForwardLabelProcessor().process(input);
		return new DefaultSample<String>(pta);
	}

	public static void main(String[] args) throws Exception {
		AdaptUtils.register(IDirectedGraph.class, IDFA.class, new IAdapter() {
			public Object adapt(Object who, Class<?> type) {
				return new GraphDFA((IDirectedGraph)who);
			}
		});

		int stCount = 20;
		
		IDFA source = dfa(stCount);

		ISample<String> sample = sample(source, stCount);
		ISample<String> chosen = allaccept(InductionFacade.choose(sample,0.06));
		
		IDFA sourceLabeled = labelize(source,0.1);
		ISample<String> chosenLabeled = labelize(chosen,sourceLabeled);

		IDFA rpniInput = (IDFA) chosen.adapt(IDFA.class);
		IDFA lsmInput = (IDFA) mergeAllSame((IDFA)chosenLabeled.adapt(IDFA.class));


		IDFA rpniResult = rpni(chosen);
		IDFA lsmResult = lsm(lsmInput);
		//AutomatonFacade.debug(source, rpniInput, lsmInput);
		AutomatonFacade.show(source, rpniInput, lsmInput, rpniResult, lsmResult);
	}
	
}
