package be.uclouvain.jail.algo.induct.internal;

import be.uclouvain.jail.fa.IDFA;


/** Provides utilities to use semantic information kept as attribute 
 * values inside states and edges. */ 
public class MappingUtils {

	/** Kernel/RealPTA representor. */
	protected static final String KP_REPRESENTOR = "__kp_representor";

	/** Kernel/DecPTA representor. */
	protected static final String KDP_REPRESENTOR = "__kdp_representor";

	/** Not intended to be instanciated. */
	private MappingUtils() {
	}

	/** Updates the PTA representor of a the kernel DFA state. */
	protected static void updSRepresentor(InductionAlgo algo, Object kState, Object ptaState) {
		IDFA dfa = algo.getDFA();
		dfa.getGraph().getVertexInfo(kState).setAttribute(KP_REPRESENTOR, ptaState);
	}

	/** Returns the PTA representor of a DFA state. */ 
	public static Object sRepresentor(InductionAlgo algo, Object kState) {
		IDFA dfa = algo.getDFA();
		assert (kState != null) : "We never map a null kState.";
		return dfa.getGraph().getVertexInfo(kState).getAttribute(KP_REPRESENTOR);
	}

	/** Updates the Dec/PTA representor of a the kernel DFA state. */
	protected static void updPRepresentor(InductionAlgo algo, Object kState, PTAState ptaState) {
		//System.out.println("Updating PTA representor of " + algo.getDFA().getGraph().getVerticesTotalOrder().indexOf(kState) + " " + kState);
		IDFA dfa = algo.getDFA();
		assert (ptaState != null) : "We never map a null PTAState.";
		dfa.getGraph().getVertexInfo(kState).setAttribute(KDP_REPRESENTOR, ptaState);
	}

	/** Returns the PTA representor of a kernel state. */ 
	public static PTAState pRepresentor(InductionAlgo algo, Object kState) {
		IDFA dfa = algo.getDFA();
		//System.out.println("Requesting PTA representor of " + algo.getDFA().getGraph().getVerticesTotalOrder().indexOf(kState) + " " + kState);
		PTAState state = (PTAState) dfa.getGraph().getVertexInfo(kState).getAttribute(KDP_REPRESENTOR);
		assert (state != null) : "We never request a PTA representor which does not exist yet.";
		return state;
	}
	
}
