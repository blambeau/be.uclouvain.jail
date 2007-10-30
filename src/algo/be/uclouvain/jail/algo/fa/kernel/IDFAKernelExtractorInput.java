package be.uclouvain.jail.algo.fa.kernel;

import be.uclouvain.jail.fa.IDFA;

/**
 * Abstracts the input of the DFAKernelExtractorAlgo.
 * 
 * @author blambeau
 */
public interface IDFAKernelExtractorInput {

	/** Returns the DFA. */
	public IDFA getDFA();
	
}
