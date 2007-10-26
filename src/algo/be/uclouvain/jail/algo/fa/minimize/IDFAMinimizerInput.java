package be.uclouvain.jail.algo.fa.minimize;

import be.uclouvain.jail.fa.IDFA;

/**
 * Abstracts the input of {@link DFAMinimizerAlgo}
 *   
 * @author blambeau
 */
public interface IDFAMinimizerInput {

	/** Return s the DFA to minimize. */
	public IDFA getDFA();
	
}
