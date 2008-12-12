package be.uclouvain.jail.algo.fa.minimize;

import be.uclouvain.jail.algo.graph.utils.IGraphPartitionner;
import be.uclouvain.jail.fa.IDFA;

/**
 * Abstracts the input of {@link DFAMinimizerAlgo}
 *   
 * @author blambeau
 */
public interface IDFAMinimizerInput {

	/** Return s the DFA to minimize. */
	public IDFA getDFA();

	/** Ensures connex component from the initial state? */
	public boolean connex();
	
	/** Returns the initial state partitionner. */
	public IGraphPartitionner<Object> getInitPartitionner();

}
