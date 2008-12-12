package be.uclouvain.jail.algo.graph.merge;

import be.uclouvain.jail.algo.graph.utils.GraphEdgeGroup;
import be.uclouvain.jail.algo.graph.utils.GraphVertexGroup;

/** 
 * Abstracts result of GraphMergingAlgo. 
 * 
 * @author blambeau
 */
public interface IGraphMergingResult {

	/** Creates a state. */
	public void createState(GraphVertexGroup vertex);
	
	/** Creates an edge between source and target. */
	public void createEdge(GraphVertexGroup sources, GraphVertexGroup targets, GraphEdgeGroup edges);

}
