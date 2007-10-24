package be.uclouvain.jail.algo.fa.equiv;

import be.uclouvain.jail.fa.IDFATrace;

/** 
 * Encapsulates output of the equivalence algorithm.
 * 
 * @author blambeau
 */
public interface IDFAEquivOutput {

	/** Provides the different counter example kinds. */
	public static enum CounterExampleKind {
		
		/** The counter-example provides a trace (in the tested DFA) which is 
		 * accepted by tested DFA but not in the reference DFA. */
		ACCEPTED,
		
		/** The counter example provides a trace (in the reference DFA) which is 
		 * rejected by the tested DFA but not in the reference DFA. */ 
		REJECTED,
		
		/** The counter example provides a trace of the tested DFA which reach a
		 * state that is not compatible with the equivalent state of the reference 
		 * DFA, according to the user-defined informer. */
		STATE,
		
		/** The counter example provides a trace of the tested DFA which ends with
		 * an edge that is not compatible with the equivalent edge of the reference 
		 * DFA, according to the user-defined informer. */
		EDGE,
		
		/** The counter example cannot be provided, sorry. */
		UNEXPLAINED;
		
	}
	
	/** Mark equivalence as proved. */
	public void equivProved();
	
	/** Mark equivalence as unproved and register the provided
	 * counter-example (that may be null, according to counterExampleEnabled flag
	 * of the algorithm input). */ 
	public void counterExampleFound(CounterExampleKind kind, IDFATrace trace);
	
	/** Returns true if equivalence has been proved. */
	public boolean areEquivalent();
	
	/** Returns the kind of counter example. */
	public CounterExampleKind getCounterExampleKind();
	
	/** Returns the counter-example. */
	public IDFATrace getCounterExample();
	
}
