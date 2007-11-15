package be.uclouvain.jail.algo.graph.merge;

import be.uclouvain.jail.algo.graph.utils.GraphEdgeGroup;
import be.uclouvain.jail.algo.graph.utils.GraphPartition;
import be.uclouvain.jail.algo.graph.utils.GraphVertexGroup;
import be.uclouvain.jail.algo.graph.utils.IGraphMemberGroup;
import be.uclouvain.jail.algo.graph.utils.IGraphPartition;
import be.uclouvain.jail.algo.graph.utils.IGraphPartitionner;
import be.uclouvain.jail.graph.IDirectedGraph;

/**
 * Simple algorithm that creates a new graph by merging vertices of 
 * another one.
 * 
 * @author blambeau
 */
public class GraphMergingAlgo {

	/** Executes the algorithm. */
	public void execute(IGraphMergingInput input, IGraphMergingResult result) {
		final IDirectedGraph graph = input.getGraph();

		// retrieve partitionners
		final IGraphPartitionner<Object> vPart = input.getVertexPartitionner();
		final IGraphPartitionner<Object> ePart = input.getEdgePartitionner();
		
		// retrieve vertices
		final IGraphPartition vertices = getVertices(graph,vPart);
		
		// for each vertex
		for (IGraphMemberGroup vertexDef: vertices) {
			GraphVertexGroup vertex = new GraphVertexGroup(vertexDef);
			
			// take outgoing edges
			GraphEdgeGroup outEdges = vertex.getOutgoingEdges();
			GraphPartition outEdgesP = new GraphPartition(graph,outEdges);
			
			// refine according to reached group
			outEdgesP.refine(new IGraphPartitionner<Object>() {

				// return the target group
				public Object getPartitionOf(Object edge) {
					return vertices.getPartitionOf(graph.getEdgeTarget(edge));
				}
				
			});
			
			// refine according to the user-defined edge partitioner
			outEdgesP.refine(ePart);
			
			// create edges
			for (IGraphMemberGroup edgeDef: outEdgesP) {
				GraphEdgeGroup edge = new GraphEdgeGroup(edgeDef);
				
				Object sedge = edge.iterator().next(); 
				Object target = graph.getEdgeTarget(sedge);
				IGraphMemberGroup group = vertices.getGroupOf(target);
				GraphVertexGroup targets = new GraphVertexGroup(group);
				
				result.createEdge(vertex, targets, edge);
			}
		}
		
	}

	/** Converts the partitionner to a partition. */
	private IGraphPartition getVertices(IDirectedGraph graph, IGraphPartitionner<Object> part) {
		if (part instanceof IGraphPartition) {
			return (IGraphPartition) part;
		} else {
			GraphPartition p = new GraphPartition(graph,graph.getVertices());
			p.refine(part);
			return p;
		}
	}
	
}
