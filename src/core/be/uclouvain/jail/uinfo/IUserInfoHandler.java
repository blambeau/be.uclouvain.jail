package be.uclouvain.jail.uinfo;

import java.util.Collection;

/**
 * Provides a facade for handling UserInfoS.
 * 
 * @author blambeau
 */
public interface IUserInfoHandler {

	/** Returns used copier when new graphs are created. */
	public UserInfoCopier getGraphCopier();
	
	/** Returns used copier when graphs are merged. */
	public UserInfoAggregator getGraphAggregator();
	
	/** Returns used copier when new vertices are created. */
	public UserInfoCopier getVertexCopier();
	
	/** Returns used copier when vertices are merged. */
	public UserInfoAggregator getVertexAggregator();
	
	/** Returns used copier when new edges are created. */
	public UserInfoCopier getEdgeCopier();
	
	/** Returns used copier when edges are merged. */
	public UserInfoAggregator getEdgeAggregator();
	
	/** Copies user infos from a graph. */
	public IUserInfo graphCopy(IUserInfo info);
	
	/** Aggregates user infos of graphs. */
	public IUserInfo graphAggregate(Collection<IUserInfo> infos);
	
	/** Aggregates user infos of graphs. */
	public IUserInfo graphAggregate(IUserInfo...infos);
	
	/** Copies user infos from a graph. */
	public IUserInfo vertexCopy(IUserInfo info);
	
	/** Aggregates user infos of vertices. */
	public IUserInfo vertexAggregate(Collection<IUserInfo> infos);
	
	/** Aggregates user infos of vertices. */
	public IUserInfo vertexAggregate(IUserInfo...infos);
	
	/** Copies user infos from an edge. */
	public IUserInfo edgeCopy(IUserInfo info);
	
	/** Aggregates user infos of edges. */
	public IUserInfo edgeAggregate(Collection<IUserInfo> infos);
	
	/** Aggregates user infos of edges. */
	public IUserInfo edgeAggregate(IUserInfo...infos);

	/** Keeps all attributes on marked components. */
	public void keepAll(boolean onGraph, boolean onVertex, boolean onEdge);
	
}
