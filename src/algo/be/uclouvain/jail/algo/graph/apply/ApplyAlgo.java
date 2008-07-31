package be.uclouvain.jail.algo.graph.apply;

import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.UserInfoCopier;

/**
 * Applies some transformation on each state. 
 * 
 * @author blambeau
 */
public class ApplyAlgo {

	/** Executes on each state. */
	public void executeOnEachState(IDirectedGraph g, UserInfoCopier copier) {
		for (Object vertex: g.getVertices()) {
			IUserInfo info = g.getVertexInfo(vertex);
			IUserInfo rewrited = copier.create(info);
			g.setVertexInfo(vertex, rewrited);
		}
	}
	
	/** Executes on each edge. */
	public void executeOnEachEdge(IDirectedGraph g, UserInfoCopier copier) {
		for (Object edge: g.getEdges()) {
			IUserInfo info = g.getEdgeInfo(edge);
			IUserInfo rewrited = copier.create(info);
			g.setEdgeInfo(edge, rewrited);
		}
	}
	
}
