package be.uclouvain.jail.algo.graph.shortest.dsp;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides a default implementation of IDSPOutput.
 * 
 * @author blambeau
 */
public class DefaultDSPOutput<T> implements IDSPOutput<T> {

	/** Info kept by vertex. */
	class VertexInfo {
		T distance;
		Object edge;
	}
	
	/** Distances by vertex. */
	private Map<Object,VertexInfo> info = new HashMap<Object,VertexInfo>();

	/** Creates an output instance. */
	public DefaultDSPOutput() {
	}
	
	/** Returns the distance between some vertex and the root. */
	public T getDistance(Object vertex) {
		return info.get(vertex).distance;
	}

	/** Returns incoming edge of a vertex. */
	public Object getIncommingEdge(Object vertex) {
		return info.get(vertex).edge;
	}

	/** Returns a vertex info, creating if if required. */
	private VertexInfo getVertexInfo(Object vertex, boolean create) {
		VertexInfo vInfo = info.get(vertex);
		if (vInfo == null && create) {
			vInfo = new VertexInfo();
			info.put(vertex, vInfo);
		}
		return vInfo;
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
		VertexInfo info = getVertexInfo(vertex,true);
		info.distance = distance;
		info.edge = edge;
	}

}
