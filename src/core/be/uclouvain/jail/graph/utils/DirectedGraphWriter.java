package be.uclouvain.jail.graph.utils;

import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.IDirectedGraphWriter;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.UserInfoCopier;

/** Provides a useful copy algorithm output. */
public class DirectedGraphWriter<T> implements IDirectedGraphWriter {
	
	/** Graph to fill. */
	private IDirectedGraph graph;
	
	/** Vertex info creator to use. */
	private UserInfoCopier vInfoCopier;
	
	/** Edge info creator to use. */
	private UserInfoCopier eInfoCopier;
	
	/** Creates an algo output instance. */
	public DirectedGraphWriter(IDirectedGraph graph) {
		this.graph = graph;
		this.vInfoCopier = new UserInfoCopier();
		this.eInfoCopier = new UserInfoCopier();
	}

	/** Returns vertex info creator. */
	public UserInfoCopier getVertexCopier() {
		return vInfoCopier;
	}
	
	/** Returns edge info creator. */
	public UserInfoCopier getEdgeCopier() {
		return vInfoCopier;
	}
	
	/** Clones some vertex IUserInfo. */
	protected IUserInfo cloneVertexInfo(IUserInfo info) {
		return vInfoCopier.create(info);
	}
	
	/** Clones some edge IUserInfo. */
	protected IUserInfo cloneEdgeInfo(IUserInfo info) {
		return eInfoCopier.create(info);
	}
	
	/** Creates a vertex. */
	public Object createVertex(IUserInfo info) {
		info = cloneVertexInfo(info);
		return graph.createVertex(info);
	}
	
	/** Creates an edge. */
	public Object createEdge(Object source, Object target, IUserInfo info) {
		info = cloneEdgeInfo(info);
		return graph.createEdge(source, target, info);
	}
	
}