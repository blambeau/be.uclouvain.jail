package be.uclouvain.jail.algo.graph.connex;

import be.uclouvain.jail.graph.IDirectedGraph;

/**
 * Abstracts input of GraphConxDetectorAlgo.
 * 
 * @author blambeau
 */
public interface IGraphConXDetectorInput {

	/** Returns the graph. */
	public IDirectedGraph getGraph();
	
}
