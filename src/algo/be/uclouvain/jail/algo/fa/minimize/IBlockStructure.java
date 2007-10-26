package be.uclouvain.jail.algo.fa.minimize;

import java.util.Set;

/**
 * Provides a refinable block structure.
 * 
 * @author blambeau
 */
public interface IBlockStructure<T> extends Iterable<Set<T>> {

	/** Returns the number of blocks. */
	public int size();

	/** Returns states in the i-th block. */
	public Set<T> getBlock(int i);

	/** Returns the block number of some element. */
	public int getBlockOf(Object element);
	
}
