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
	
	/** Sets the distance of a vertex. */
	public void setDistance(Object vertex, T distance) {
		getVertexInfo(vertex,true).distance = distance;
	}

	/** Sets the incoming edge of a vertex. */
	public void setIncomingEdge(Object vertex, Object edge) {
		getVertexInfo(vertex,true).edge = edge;
	}

}
