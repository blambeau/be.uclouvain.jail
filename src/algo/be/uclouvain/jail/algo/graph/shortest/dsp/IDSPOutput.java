package be.uclouvain.jail.algo.graph.shortest.dsp;

/** Algorithm output. */
public interface IDSPOutput<T> {

	/**
	 * Updates reachability of a vertex by updating the distance and
	 * incoming edge.
	 * 
	 * @param vertex vertex to set distance to.
	 * @param distance distance from root vertex.
	 * @param edge edge to use to reach the vertex. 
	 */
	public void reachVertex(Object vertex, T distance, Object edge);

	/** Returns the distance between some vertex and the root. */
	public T getDistance(Object vertex);

	/** Returns the incoming egde of a vertex. */
	public Object getIncommingEdge(Object vertex);
	
}