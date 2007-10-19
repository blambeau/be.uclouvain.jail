package be.uclouvain.jail.graph.adjacency;

import be.uclouvain.jail.uinfo.IUserInfo;

/** Default component factory ; creates DefaultVertex and DefaultEdge instances. */
public class DefaultGraphComponentFactory implements IGraphComponentFactory {

	/** Creates a DefaultVertex instance. */
	public IVertex createVertex(IUserInfo info) {
		return new DefaultVertex(info);
	}

	/** Creates an DefaultEdge instance. */
	public IEdge createEdge(IUserInfo info) {
		return new DefaultEdge(info);
	}

}