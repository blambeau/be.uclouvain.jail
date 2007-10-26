package be.uclouvain.jail.algo.graph.merge;

import java.util.Set;

import be.uclouvain.jail.uinfo.IUserInfo;

/** 
 * Abstracts result of GraphMergingAlgo. 
 * 
 * @author blambeau
 */
public interface IGraphMergingResult {

	/** Creates a vertex inside the resulting graph. */
	public Object createVertex(Set<IUserInfo> infos);

	/** Creates an edge between source and target. */
	public void createEdge(Object source, Object target, Set<IUserInfo> edgeInfo);

}
