package be.uclouvain.jail.algo.graph.decorate;

import be.uclouvain.jail.graph.IDirectedGraph;

/**
 * Provides input information to {@link IdentifyAlgo}.
 * 
 * @author blambeau
 */
public interface IIdentifyAlgoInput {

	/** Returns graph to identify. */
	public IDirectedGraph getGraph();

	/** Returns state attribute to install, null if states must 
	 * not be identified. */
	public String getVertexAttr();
	
	/** Returns edge attribute to install, null if edges must 
	 * not be identified. */
	public String getEdgeAttr();
	
}
