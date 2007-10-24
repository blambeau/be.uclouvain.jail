package be.uclouvain.jail.algo.fa.equiv;

import be.uclouvain.jail.fa.IDFA;

/**
 * Provides input of the equivalence algorithm.
 * 
 * @author blambeau
 */
public interface IDFAEquivInput {

	/** Returns tested DFA. */
	public IDFA getTested();

	/** Returns reference DFA. */
	public IDFA getReference();
	
	/** Returns user-defined equivalence informer to use, or null. */
	public IDFAEquivInformer getInformer();
	
	/** Does the algorithm needs to provides a non equivalent proof
	 * trace? */
	public boolean isCounterExampleEnabled();
	
}
