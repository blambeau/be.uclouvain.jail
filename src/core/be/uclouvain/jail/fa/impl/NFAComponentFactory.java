package be.uclouvain.jail.fa.impl;

import be.uclouvain.jail.graph.adjacency.DefaultEdge;
import be.uclouvain.jail.graph.adjacency.IEdge;
import be.uclouvain.jail.graph.adjacency.IGraphComponentFactory;
import be.uclouvain.jail.graph.adjacency.IVertex;
import be.uclouvain.jail.uinfo.IUserInfo;

/** 
 * Graph component factory for NFA. 
 * 
 * <p>This factory creates {@link NFAVertex} and {@link DefaultEdge} instances for
 * vertices and edges, respectively.</p>
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