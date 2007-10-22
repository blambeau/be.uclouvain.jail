package be.uclouvain.jail.algo.graph.shortest.dsp;

import java.util.LinkedList;

import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.IDirectedGraphPath;
import be.uclouvain.jail.graph.IDirectedGraphWriter;
import be.uclouvain.jail.graph.utils.DirectedGraphPath;
import be.uclouvain.jail.orders.ITotalOrder;
import be.uclouvain.jail.uinfo.IUserInfo;

/** Decorates an output to useful usable result. */
public class DSPResult<T> {
	
	/** Initial input. */
	private IDSPInput<T> input;
	
	/** Decorated output. */
	private IDSPOutput<T> output;
	
	/** Creates a result instance. */
	public DSPResult(IDSPInput<T> input, IDSPOutput<T> output) {
		this.input = input;
		this.output = output;
	}

	/** Returns the distance between vertex and root. */
	public T getDistance(Object vertex) {
		return output.getDistance(vertex);
	}

	/** Returns incoming edge used to reach vertex. */
	public Object getIncomingEdge(Object vertex) {
		return output.getIncommingEdge(vertex);
	}

	/** Returns the shortest path from the root to the vertex. */
	public IDirectedGraphPath getShortestPathTo(Object vertex) {
		IDirectedGraph graph = input.getGraph();
		Object root = input.getRootVertex();
		LinkedList<Object> edges = new LinkedList<Object>();
		
		// create path in reverse order
		Object current = vertex;
		while (current != root) {
			Object edge = output.getIncommingEdge(current); 
			edges.addFirst(edge);
			current = graph.getEdgeSource(edge);
		}
		
		// return path
		return new DirectedGraphPath(input.getGraph(),edges);
	}
	
	/** Flushes this result as a spanning tree. */
	public void asSpanningTree(IDirectedGraphWriter output) {
		IDirectedGraph graph = input.getGraph();
		// retrieve input vertices and edges
		ITotalOrder<Object> vertices = graph.getVerticesTotalOrder();

		// vertex info and creator
		IUserInfo vInfo = null;
		
		// edge info and creator
		IUserInfo eInfo = null;
		
		// copy all vertices
		Object[] copies = new Object[vertices.size()];
		int i=0;
		for (Object vertex: vertices) {
			vInfo = graph.getVertexInfo(vertex);
			copies[i++] = output.createVertex(vInfo);
		}
		
		// create edges
		Object root = input.getRootVertex();
		for (Object vertex: vertices) {
			if (vertex == root) { continue; }
			Object iEdge = getIncomingEdge(vertex);
			Object iSource = graph.getEdgeSource(iEdge);
			eInfo = graph.getEdgeInfo(iEdge);
			output.createEdge(copies[vertices.getElementIndex(iSource)], 
					copies[vertices.getElementIndex(vertex)], eInfo);
		}
	}
	
}