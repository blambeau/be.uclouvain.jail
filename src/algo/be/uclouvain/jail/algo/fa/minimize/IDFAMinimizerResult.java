package be.uclouvain.jail.algo.fa.minimize;

import be.uclouvain.jail.adapt.IAdaptable;
import be.uclouvain.jail.algo.graph.utils.IGraphPartition;

/**
 * Abstracts the result of {@link DFAMinimizerAlgo}.
 * 
 * @author blambeau
 */
public interface IDFAMinimizerResult extends IAdaptable {

	/** "Algorithm started" event. */
	public void started(IDFAMinimizerInput input);
	
	/** "Algorithm ended" event. */
	public void ended(IGraphPartition partition);
	
}

