package be.uclouvain.jail.algo.graph.utils;

/**
 * Defines a graph partition.
 * 
 * @author blambeau
 */
public interface IGraphPartition extends Iterable<IGraphMemberGroup>, IGraphPartitionner<Object> {

	/** Returns number of groups. */
	public int size();
	
	/** Returns the group of a member. */
	public IGraphMemberGroup getGroupOf(Object member);
	
}
