package be.uclouvain.jail.algo.graph.rand;

import be.uclouvain.jail.graph.IGraphPredicate;

/**
 * Provides input information for the random graph algorithm. 
 * 
 * @author blambeau
 */
public interface IRandomGraphInput {
	
	/** Returns a predicate for stopping creation of vertices. */
	public IGraphPredicate getVertexStopPredicate();

	/** Returns a predicate for stopping creation of edges. */
	public IGraphPredicate getEdgeStopPredicate();

	/** Returns a predicate for acceptation. */
	public IGraphPredicate getAcceptPredicate();

	/** Returns a predicate for stopping tries. */
	public IGraphPredicate getTryStopPredicate();
	
}
