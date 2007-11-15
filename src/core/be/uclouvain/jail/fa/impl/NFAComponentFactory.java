package be.uclouvain.jail.fa.impl;

import be.uclouvain.jail.graph.adjacency.DefaultEdge;
import be.uclouvain.jail.graph.adjacency.IEdge;
import be.uclouvain.jail.graph.adjacency.IGraphComponentFactory;
import be.uclouvain.jail.graph.adjacency.IVertex;
import be.uclouvain.jail.uinfo.IUserInfo;

/** 
 * Provides a Graph component factory that creates efficient implementations
 * of vertices and edges to be used for NFAs.
 * 
 * <p>This factory is intended to be used when an {@link AdjacencyDirectedGraph} 
 * will be used to implement a NFA.</p> 
 */
public class NFAComponentFactory implements IGraphComponentFactory {

	/** Creates a NFAVertex instance. */
	public IVertex createVertex(IUserInfo info) {
		return new NFAVertex(info);
	}

	/** Creates an DefaultEdge instance. */
	public IEdge createEdge(IUserInfo info) {
		return new DefaultEdge(info);
	}

}