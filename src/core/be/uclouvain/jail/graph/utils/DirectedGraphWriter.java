package be.uclouvain.jail.graph.utils;

import be.uclouvain.jail.graph.IDirectedGraphWriter;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.IUserInfoCreator;
import be.uclouvain.jail.uinfo.IUserInfoRewriters;
import be.uclouvain.jail.uinfo.UserInfoCopier;

/**
 * Provides a graph writer which acts as a modifier before actual writing.
 * 
 * <p>This utility class is introduced to allow some modifications to be done
 * before actually writing a real graph. It decorates another writer (which 
 * should be a real graph in practive) and acts as a prefilter on vertex and 
 * edge creations.</p>
 * 
 * @author blambeau
 */
public class DirectedGraphWriter<T> implements IDirectedGraphWriter,
                                               IUserInfoRewriters {
	
	/** Graph to fill. */
	private IDirectedGraphWriter writer;
	
	/** Graph info creator to use. */
	private UserInfoCopier gInfoCopier;
	
	/** Vertex info creator to use. */
	private UserInfoCopier vInfoCopier;
	
	/** Edge info creator to use. */
	private UserInfoCopier eInfoCopier;
	
	/** Creates an algo output instance. */
	public DirectedGraphWriter(IDirectedGraphWriter writer) {
		this.writer = writer;
		this.gInfoCopier = new UserInfoCopier();
		this.vInfoCopier = new UserInfoCopier();
		this.eInfoCopier = new UserInfoCopier();
	}

	/** Returns vertex info creator. */
	public UserInfoCopier getGraphCopier() {
		return gInfoCopier;
	}
	
	/** Returns the graph rewriter to use. */
	public IUserInfoCreator<IUserInfo> getGraphRewriter() {
		return gInfoCopier;
	}
	
	/** Rewrites a graph info. */
	public IUserInfo rewriteGraphInfo(IUserInfo info) {
		return gInfoCopier.create(info);
	}
	
	/** Returns vertex info creator. */
	public UserInfoCopier getVertexCopier() {
		return vInfoCopier;
	}
	
	/** Returns the vertex rewriter to use. */
	public IUserInfoCreator<IUserInfo> getVertexRewriter() {
		return vInfoCopier;
	}
	
	/** Rewrites a vertex info. */
	public IUserInfo rewriteVertexInfo(IUserInfo info) {
		return vInfoCopier.create(info);
	}
	
	/** Returns edge info creator. */
	public UserInfoCopier getEdgeCopier() {
		return eInfoCopier;
	}
	
	/** Returns the edge rewriter to use. */
	public IUserInfoCreator<IUserInfo> getEdgeRewriter() {
		return eInfoCopier;
	}
	
	/** Rewrites an edge info. */
	public IUserInfo rewriteEdgeInfo(IUserInfo info) {
		return eInfoCopier.create(info);
	}
	
	/** Creates the graph info. */
	public void setUserInfo(IUserInfo info) {
		info = rewriteGraphInfo(info);
		writer.setUserInfo(info.copy());
	}
	
	/** Creates a vertex. */
	public Object createVertex(IUserInfo info) {
		info = rewriteVertexInfo(info);
		return writer.createVertex(info);
	}
	
	/** Creates an edge. */
	public Object createEdge(Object source, Object target, IUserInfo info) {
		info = rewriteEdgeInfo(info);
		return writer.createEdge(source, target, info);
	}

	/** Adapts this writer to a specific class. */
	public <S> Object adapt(Class<S> c) {
		if (c.isAssignableFrom(getClass())) {
			return this;
		}
		return writer.adapt(c);
	}
	
}