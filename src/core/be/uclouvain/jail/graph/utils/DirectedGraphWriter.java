package be.uclouvain.jail.graph.utils;

import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.IDirectedGraphWriter;
import be.uclouvain.jail.graph.adjacency.AdjacencyDirectedGraph;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.IUserInfoHandler;
import be.uclouvain.jail.uinfo.UserInfoHandler;

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
public class DirectedGraphWriter implements IDirectedGraphWriter {
	
	/** Graph to fill. */
	private IDirectedGraphWriter writer;
	
	/** Handler to use. */
	private IUserInfoHandler handler;
	
	/** Creates an algo output instance. */
	public DirectedGraphWriter(IUserInfoHandler handler, IDirectedGraphWriter writer) {
		this.writer = writer;
		this.handler = handler;
	}

	/** Creates an algo output instance. */
	public DirectedGraphWriter(IDirectedGraphWriter writer) {
		this(new UserInfoHandler(),writer);
	}

	/** Creates an algo output instance. */
	public DirectedGraphWriter(IUserInfoHandler handler) {
		this(handler,new AdjacencyDirectedGraph());
	}

	/** Creates an algo output instance. */
	public DirectedGraphWriter() {
		this(new UserInfoHandler(), new AdjacencyDirectedGraph());
	}
	
	/** Returns underlying graph, if any. */
	public IDirectedGraph getGraph() {
		return writer instanceof IDirectedGraph ?
			   (IDirectedGraph) writer : null;
	}
	
	/** Checks if there is a graph. */
	public boolean hasGraph() {
		return writer instanceof IDirectedGraph;
	}
	
	/** Returns graph info. */
	public IUserInfo getGraphInfo() {
		return writer.getGraphInfo();
	}

	/** Creates the graph info. */
	public void setUserInfo(IUserInfo info) {
		writer.setUserInfo(handler.graphCopy(info));
	}
	
	/** Returns vertex info. */
	public IUserInfo getVertexInfo(Object vertex) {
		return writer.getVertexInfo(vertex);
	}

	/** Creates a vertex. */
	public Object createVertex(IUserInfo info) {
		return writer.createVertex(handler.vertexCopy(info));
	}
	
	/** Returns an edge info. */
	public IUserInfo getEdgeInfo(Object edge) {
		return writer.getEdgeInfo(edge);
	}

	/** Creates an edge. */
	public Object createEdge(Object source, Object target, IUserInfo info) {
		return writer.createEdge(source, target, handler.edgeCopy(info));
	}

	/** Adapts this writer to a specific class. */
	public <S> Object adapt(Class<S> c) {
		if (c.isAssignableFrom(getClass())) {
			return this;
		} 
		return writer.adapt(c);
	}
	
}