package be.uclouvain.jail.algo.fa.kernel;

import be.uclouvain.jail.graph.IDirectedGraphWriter;

/**
 * Abstracts result of the DFAKernelExtractorAlgo.
 * 
 * @author blambeau
 */
public interface IDFAKernelExtractorResult {

	/** Returns the NFA writer to use. */
	public IDirectedGraphWriter getNFAWriter();

}
