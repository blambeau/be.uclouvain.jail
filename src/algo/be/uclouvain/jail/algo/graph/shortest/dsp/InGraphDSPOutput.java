package be.uclouvain.jail.algo.graph.shortest.dsp;

import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.uinfo.IUserInfo;

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

	/**
	 * Updates reachability of a vertex by updating the distance and
	 * incoming edge.
	 * 
	 * @param vertex vertex to set distance to.
	 * @param distance distance from root vertex.
	 * @param edge edge to use to reach the vertex. 
	 */
	public void reachVertex(Object vertex, T distance, Object edge) {
		IUserInfo info = graph.getVertexInfo(vertex);
		info.setAttribute(distAttr, distance);
		info.setAttribute(edgeAttr, edge);
	}
	
}
