package be.uclouvain.jail.fa.impl;

import be.uclouvain.jail.graph.adjacency.DefaultEdge;
import be.uclouvain.jail.graph.adjacency.IEdge;
import be.uclouvain.jail.graph.adjacency.IGraphComponentFactory;
import be.uclouvain.jail.graph.adjacency.IVertex;
import be.uclouvain.jail.uinfo.IUserInfo;

/** Default component factory ; creates DefaultVertex and DefaultEdge instances. */
public class NFAComponentFactory implements IGraphComponentFactory {

	/** Creates a DefaultVertex instance. */
	public IVertex createVertex(IUserInfo info) {
		return new NFAVertex(info);
	}

	/** Creates an DefaultEdge instance. */
	public IEdge createEdge(IUserInfo info) {
		return new DefaultEdge(info);
	}

}