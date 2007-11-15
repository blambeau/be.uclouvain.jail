package be.uclouvain.jail.algo.fa.complement;

import be.uclouvain.jail.fa.IDFA;

/**
 * Abstracts input of the IDFAComplementor algorithm.
 * 
 * @author blambeau
 */
public interface IDFAComplementorInput {

	/** Returns the DFA to complement. */
	public IDFA getDFA();
	
	/** Returns heuristic to use. */
	public DFAComplementorHeuristic getHeuristic();
	
}
