package be.uclouvain.jail.algo.graph.decorate;

import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.utils.ITotalOrder;
import be.uclouvain.jail.uinfo.IUserInfo;

/** Identifies states and edges. */
public class IdentifyAlgo {

	/** Executes the algorithm. */
	public void execute(IIdentifyAlgoInput input) {
		IDirectedGraph g = input.getGraph();
		ITotalOrder<Object> vertices = g.getVerticesTotalOrder();
		ITotalOrder<Object> edges = g.getEdgesTotalOrder();
		
		String vAttr = input.getVertexAttr();
		if (vAttr != null) {
			for (Object vertex: g.getVertices()) {
				IUserInfo info = g.getVertexInfo(vertex);
				info.setAttribute(vAttr,vertices.indexOf(vertex));
			}
		}

		String eAttr = input.getEdgeAttr();
		if (eAttr != null) {
			for (Object edge: g.getEdges()) {
				IUserInfo info = g.getEdgeInfo(edge);
				info.setAttribute(vAttr,edges.indexOf(edge));
			}
		}
	}
	
}
