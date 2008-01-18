package be.uclouvain.jail.uinfo;

import java.util.Collection;

/** 
 * Base implementation of {@link IUserInfoHandler}.
 * 
 * @author blambeau
 */
public class UserInfoHandler implements IUserInfoHandler {

	/** Graph copier. */
	private UserInfoCopier graphCopier;
	
	/** Graph aggregator. */
	private UserInfoAggregator graphAggregator;
	
	/** Vertex copier. */
	private UserInfoCopier vertexCopier;
	
	/** Vertex aggregator. */
	private UserInfoAggregator vertexAggregator;
	
	/** Edge copier. */
	private UserInfoCopier edgeCopier;
	
	/** Edge aggregator. */
	private UserInfoAggregator edgeAggregator;
	
	/** Returns used copier when new graphs are created. */
	public UserInfoCopier getGraphCopier() {
		if (graphCopier == null) { graphCopier = new UserInfoCopier(); }
		return graphCopier;
	}
	
	/** Returns used copier when graphs are merged. */
	public UserInfoAggregator getGraphAggregator() {
		if (graphAggregator == null) { graphAggregator = new UserInfoAggregator(); }
		return graphAggregator;
	}
	
	/** Returns used copier when new vertices are created. */
	public UserInfoCopier getVertexCopier() {
		if (vertexCopier == null) { vertexCopier = new UserInfoCopier(); }
		return vertexCopier;
	}
	
	/** Returns used copier when vertices are merged. */
	public UserInfoAggregator getVertexAggregator() {
		if (vertexAggregator == null) { vertexAggregator = new UserInfoAggregator(); }
		return vertexAggregator;
	}
	
	/** Returns used copier when new edges are created. */
	public UserInfoCopier getEdgeCopier() {
		if (edgeCopier == null) { edgeCopier = new UserInfoCopier(); }
		return edgeCopier;
	}
	
	/** Returns used copier when edges are merged. */
	public UserInfoAggregator getEdgeAggregator() {
		if (edgeAggregator == null) { edgeAggregator = new UserInfoAggregator(); }
		return edgeAggregator;
	}

	/** Copies user infos from a graph. */
	public IUserInfo graphCopy(IUserInfo info) {
		return getGraphCopier().create(info);
	}
	
	/** Aggregates user infos of graphs. */
	public IUserInfo graphAggregate(Collection<IUserInfo> infos) {
		return getGraphAggregator().create(infos);
	}
	
	/** Aggregates user infos of graphs. */
	public IUserInfo graphAggregate(IUserInfo...infos) {
		return getGraphAggregator().create(infos);
	}
	
	/** Copies user infos from a graph. */
	public IUserInfo vertexCopy(IUserInfo info) {
		return getVertexCopier().create(info);
	}
	
	/** Aggregates user infos of vertices. */
	public IUserInfo vertexAggregate(Collection<IUserInfo> infos) {
		return getVertexAggregator().create(infos);
	}
	
	/** Aggregates user infos of vertices. */
	public IUserInfo vertexAggregate(IUserInfo...infos) {
		return getVertexAggregator().create(infos);
	}
	
	/** Copies user infos from an edge. */
	public IUserInfo edgeCopy(IUserInfo info) {
		return getEdgeCopier().create(info);
	}
	
	/** Aggregates user infos of edges. */
	public IUserInfo edgeAggregate(Collection<IUserInfo> infos) {
		return getEdgeAggregator().create(infos);
	}
	
	/** Aggregates user infos of edges. */
	public IUserInfo edgeAggregate(IUserInfo...infos) {
		return getEdgeAggregator().create(infos);
	}
	
	/** Marks some keep all on copiers. */
	public void keepAll(boolean onGraph, boolean onVertex, boolean onEdge) {
		if (onGraph)  { getGraphCopier().keepAll(); }
		if (onVertex) { getVertexCopier().keepAll(); }
		if (onEdge)   { getEdgeCopier().keepAll(); }
	}
	
}
