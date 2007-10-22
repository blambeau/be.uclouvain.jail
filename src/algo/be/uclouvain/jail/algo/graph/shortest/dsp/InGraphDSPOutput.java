package be.uclouvain.jail.algo.graph.shortest.dsp;

import be.uclouvain.jail.graph.IDirectedGraph;

/**
 * Provides a DSP output that keeps distance and incoming vertex inside the graph. 
 * 
 * @author blambeau
 */
public class InGraphDSPOutput<T> implements IDSPOutput<T> {

	/** Input graph. */
	private IDirectedGraph graph;
	
	/** Distance and Edge attributes. */
	private String distAttr, edgeAttr;
	
	/** 
	 * Creates an output instance. 
	 *
	 * @param graph input graph which will be used to keep distance and edge results.
	 * @param distAttr key of the attribute used to keep distance of the vertex.
	 * @param edgeAttr key of the attribute used to keep incoming edge of a vertex.
	 */
	public InGraphDSPOutput(IDirectedGraph graph, String distAttr, String edgeAttr) {
		super();
		this.graph = graph;
		this.distAttr = distAttr;
		this.edgeAttr = edgeAttr;
	}

	/** Returns the distance between vertex and the root. */
	@SuppressWarnings("unchecked")
	public T getDistance(Object vertex) {
		return (T) graph.getVertexInfo(vertex).getAttribute(distAttr);
	}

	/** Returns the incoming edge of vertex. */
	public Object getIncommingEdge(Object vertex) {
		return graph.getVertexInfo(vertex).getAttribute(edgeAttr);
	}

	/** Sets the distance of a vertex. */
	public void setDistance(Object vertex, T distance) {
		graph.getVertexInfo(vertex).setAttribute(distAttr, distance);
	}

	/** Sets incoming edge of a vertex. */
	public void setIncomingEdge(Object vertex, Object edge) {
		graph.getVertexInfo(vertex).setAttribute(edgeAttr, edge);
	}

}
