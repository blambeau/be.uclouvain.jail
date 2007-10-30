package be.uclouvain.jail.graph.utils;

import be.uclouvain.jail.graph.IDirectedGraphWriter;
import be.uclouvain.jail.uinfo.IUserInfo;
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
public class DirectedGraphWriter<T> implements IDirectedGraphWriter {
	
	/** Graph to fill. */
	private IDirectedGraphWriter writer;
	
	/** Vertex info creator to use. */
	private UserInfoCopier vInfoCopier;
	
	/** Edge info creator to use. */
	private UserInfoCopier eInfoCopier;
	
	/** Creates an algo output instance. */
	public DirectedGraphWriter(IDirectedGraphWriter writer) {
		this.writer = writer;
		this.vInfoCopier = new UserInfoCopier();
		this.eInfoCopier = new UserInfoCopier();
	}

	/** Returns vertex info creator. */
	public UserInfoCopier getVertexCopier() {
		return vInfoCopier;
	}
	
	/** Returns edge info creator. */
	public UserInfoCopier getEdgeCopier() {
		return eInfoCopier;
	}
	
	/** Clones some vertex IUserInfo. */
	protected IUserInfo cloneVertexInfo(IUserInfo info) {
		return vInfoCopier.create(info);
	}
	
	/** Clones some edge IUserInfo. */
	protected IUserInfo cloneEdgeInfo(IUserInfo info) {
		return eInfoCopier.create(info);
	}
	
	/** Creates the graph info. */
	public void setUserInfo(IUserInfo info) {
		writer.setUserInfo(info.copy());
	}
	
	/** Creates a vertex. */
	public Object createVertex(IUserInfo info) {
		info = cloneVertexInfo(info);
		return writer.createVertex(info);
	}
	
	/** Creates an edge. */
	public Object createEdge(Object source, Object target, IUserInfo info) {
		info = cloneEdgeInfo(info);
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