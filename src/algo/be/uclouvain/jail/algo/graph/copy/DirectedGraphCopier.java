package be.uclouvain.jail.algo.graph.copy;

import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.IDirectedGraphWriter;
import be.uclouvain.jail.graph.utils.ITotalOrder;
import be.uclouvain.jail.uinfo.IUserInfo;

/**
 * Copies a graph.
 * 
 * @author blambeau
 */
public class DirectedGraphCopier {

	/** Copies some directed graph. */
	public static void copy(IDirectedGraph graph, IDirectedGraphWriter output) {
		output.setUserInfo(graph.getUserInfo());
		
		// retrieve input vertices and edges
		ITotalOrder<Object> vertices = graph.getVerticesTotalOrder();
		ITotalOrder<Object> edges = graph.getEdgesTotalOrder();

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
		
		// copy all edges
		for (Object edge: edges) {
			eInfo = graph.getEdgeInfo(edge);
			Object src = graph.getEdgeSource(edge);
			Object trg = graph.getEdgeTarget(edge);
			Object srcCopy = copies[vertices.indexOf(src)];
			Object trgCopy = copies[vertices.indexOf(trg)];
			output.createEdge(srcCopy, trgCopy, eInfo);
		}
	}
	
}
