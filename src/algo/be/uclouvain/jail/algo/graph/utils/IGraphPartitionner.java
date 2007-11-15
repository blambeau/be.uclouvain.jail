package be.uclouvain.jail.algo.graph.utils;

/**
 * Partitions a set of values according to some heuristic.
 * 
 * @author blambeau
 * @param <T>
 */
public interface IGraphPartitionner<T> {

	/** Returns the partition identifier of some value. */
	public Object getPartitionOf(T value);
	
}
