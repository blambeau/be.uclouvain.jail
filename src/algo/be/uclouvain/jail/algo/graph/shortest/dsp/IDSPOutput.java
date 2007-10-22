package be.uclouvain.jail.algo.graph.shortest.dsp;

/** Algorithm output. */
public interface IDSPOutput<T> {

	/**
	 * Sets the distance of a vertex from the root vertex.
	 * 
	 * @param vertex vertex to set distance to.
	 * @param distance distance from root vertex. 
	 */
	public void setDistance(Object vertex, T distance);

	/** Returns the distance between some vertex and the root. */
	public T getDistance(Object vertex);

	/** Sets the incoming edge of a vertex. */
	public void setIncomingEdge(Object vertex, Object edge);

	/** Returns the incoming egde of a vertex. */
	public Object getIncommingEdge(Object vertex);
	
}