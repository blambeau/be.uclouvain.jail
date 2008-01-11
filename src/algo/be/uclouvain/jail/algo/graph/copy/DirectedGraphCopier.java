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

	/** Copies a vertex. */
	protected Object copyVertex(IDirectedGraph graph, 
			                    IDirectedGraphWriter output, 
			                    Object vertex) {
		IUserInfo vInfo = graph.getVertexInfo(vertex);
		return output.createVertex(vInfo);
	}
	
	/** Copies an edge. */
	protected Object copyEdge(IDirectedGraph graph, 
                              IDirectedGraphWriter output, 
                              Object source, Object target,
                              Object edge) {
		IUserInfo eInfo = graph.getEdgeInfo(edge);
		return output.createEdge(source, target, eInfo);
	}
	
	/** Copies some directed graph. */
	public void execute(IDirectedGraph graph, IDirectedGraphWriter output) {
		output.setUserInfo(graph.getUserInfo());
		
		// retrieve input vertices and edges
		ITotalOrder<Object> vertices = graph.getVerticesTotalOrder();
		ITotalOrder<Object> edges = graph.getEdgesTotalOrder();
		
		// copy all vertices
		Object[] copies = new Object[vertices.size()];
		int i=0;
		for (Object vertex: vertices) {
			copies[i++] = copyVertex(graph, output, vertex);
		}
		
		// copy all edges
		for (Object edge: edges) {
			Object src = graph.getEdgeSource(edge);
			Object trg = graph.getEdgeTarget(edge);
			Object srcCopy = copies[vertices.indexOf(src)];
			Object trgCopy = copies[vertices.indexOf(trg)];
			if (srcCopy == null || trgCopy == null) {
				continue;
			} else {
				copyEdge(graph,output,srcCopy,trgCopy,edge);
			}
		}
	}

	/** Copies a graph to a writer. */
	public static void copy(IDirectedGraph graph, IDirectedGraphWriter writer) {
		new DirectedGraphCopier().execute(graph, writer);
	}
	
}
