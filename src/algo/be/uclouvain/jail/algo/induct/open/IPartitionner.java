package be.uclouvain.jail.algo.induct.open;

import be.uclouvain.jail.fa.IDFA;


/** Partitions PTA states into equivalence classes. 
 * 
 * <p>Each partition is identified by a single integer. The partitioning
 * is thus defined a an array of pta-size integers, mapping each of them 
 * to their respective partition.</p>
 *  
 */
public interface IPartitionner {

	/** Computes and returns the partitionning. */
	public abstract int[] partition(IDFA pta);

}
