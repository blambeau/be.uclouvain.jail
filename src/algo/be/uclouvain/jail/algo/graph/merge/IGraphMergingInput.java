package be.uclouvain.jail.algo.graph.merge;

import be.uclouvain.jail.algo.fa.minimize.IBlockStructure;
import be.uclouvain.jail.graph.IDirectedGraph;

/**
 * Abstracts input of GraphMergingAlgo. 
 * 
 * @author blambeau
 */
public interface IGraphMergingInput {

	/** Returns the graph to apply algorithm to. */
	public IDirectedGraph getGraph();
	
	/** Returns the block structure. */
	public IBlockStructure<Object> getVertexPartition();
	
}
