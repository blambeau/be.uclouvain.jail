package be.uclouvain.jail.algo.graph.merge;

import be.uclouvain.jail.algo.graph.utils.IGraphPartitionner;
import be.uclouvain.jail.graph.IDirectedGraph;

/**
 * Abstracts input of GraphMergingAlgo. 
 * 
 * @author blambeau
 */
public interface IGraphMergingInput {

	/** Returns the graph to apply algorithm to. */
	public IDirectedGraph getGraph();
	
	/** Returns the partitionner for vertices. */
	public IGraphPartitionner<Object> getVertexPartitionner();
	
	/** Returns the partitionner for edges. */
	public IGraphPartitionner<Object> getEdgePartitionner();
	
}
