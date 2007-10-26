package be.uclouvain.jail.algo.fa.minimize;

import java.util.Set;

/**
 * Abstracts the result of {@link IDFAMinimizerAlgo}.
 * 
 * @author blambeau
 */
public interface IDFAMinimizerResult {

	/**
	 * "Algorithm started" event.
	 *  
	 * @param input input of the started algorithm.
	 * @return the initial partitioning. 
	 */
	public IBlockStructure<Object> started(IDFAMinimizerInput input);
	
	/** 
	 * Block has been refined, unreachable states being removed
	 * from it and added to a new block. This method must update
	 * the block structure in order to reflect that change.
	 * 
	 * @return identifier of the new block.
	 */
	public int refined(Set<Object> block, Set<Object> unreachable);
	
	/** Returns the computed partition. */
	public IBlockStructure<Object> getStatePartition();
	
}

