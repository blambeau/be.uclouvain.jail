package be.uclouvain.jail.algo.fa.complement;

/**
 * Complementor and uncomplementor heuristics.
 * 
 * @author blambeau
 */
public enum DFAComplementorHeuristic {

	/** Complements by adding an error state. */
	ERROR_STATE,
	
	/** Complements on same state. */
	SAME_STATE;
	
}
