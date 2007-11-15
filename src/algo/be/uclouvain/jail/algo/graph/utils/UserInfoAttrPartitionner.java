package be.uclouvain.jail.algo.graph.utils;

import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.uinfo.IUserInfo;

/**
 * Partitions vertices and edges according to the value of some
 * IUserInfo attribute.
 * 
 * @author blambeau
 */
public class UserInfoAttrPartitionner implements IGraphPartitionner<Object> {

	/** Graph to use to extract user infos. */
	private IDirectedGraph graph;
	
	/** UserInfo attribute to use to partition. */
	private String attr;
	
	/** Creates a partitionner instance. */
	public UserInfoAttrPartitionner(IDirectedGraph graph, String attr) {
		this.graph = graph;
		this.attr = attr;
	}
	
	/** Returns the attribute value. */
	public Object getPartitionOf(Object member) {
		IUserInfo info = graph.getUserInfoOf(member);
		return info.getAttribute(attr);
	}

}
