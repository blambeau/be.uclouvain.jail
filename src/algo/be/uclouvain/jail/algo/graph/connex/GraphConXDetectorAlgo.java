package be.uclouvain.jail.algo.graph.connex;

import be.uclouvain.jail.algo.graph.utils.GraphPartition;
import be.uclouvain.jail.algo.graph.utils.IGraphPartitionner;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.utils.ITotalOrder;

/**
 * Detects connex composants in a graph.
 * 
 * @author blambeau
 */
public class GraphConXDetectorAlgo {

	/** Graph. */
	private IDirectedGraph graph;
	
	/** Total order on vertices. */
	private ITotalOrder<Object> order;
	
	/** Current partition number. */
	private int current;
	
	/** Partition. */
	private int[] partition;
	
	/** Checks if a vertex is explored. */
	private boolean isExplored(Object vertex) {
		int index = order.indexOf(vertex);
		return partition[index] != 0;
	}

	/** Marks a vertex as explored. */
	private void mark(Object vertex) {
		int index = order.indexOf(vertex);
		partition[index] = current;
	}
	
	/** Explores a state. */
	private void explore(Object vertex) {
		assert (!isExplored(vertex)) : "not yet explored";
		
		// mark it
		mark(vertex);
		
		// explore each outgoing edge
		for (Object edge: graph.getOutgoingEdges(vertex)) {
			Object target = graph.getEdgeTarget(edge);
			if (!isExplored(target)) {
				explore(target);
			}
		}

		// explore each incoming edge
		for (Object edge: graph.getIncomingEdges(vertex)) {
			Object target = graph.getEdgeSource(edge);
			if (!isExplored(target)) {
				explore(target);
			}
		}
	}
	
	/** Executes the algorithm. */
	public void execute(IGraphConXDetectorInput input, IGraphConXDetectorResult result) {
		graph = input.getGraph();
		order = graph.getVerticesTotalOrder();
		partition = new int[order.size()];
		if (partition.length==0) {
			throw new IllegalArgumentException("Invalid empty graph.");
		}
		
		// compute partitionning
		current = 1;
		for (Object vertex: order) {
			// bypass already explored state
			if (isExplored(vertex)) {
				continue;
			}
			
			// explore state
			explore(vertex);
			
			// next partition
			current++;
		}
		
		// create partition
		GraphPartition p = new GraphPartition(graph,graph.getVerticesTotalOrder());
		p.refine(new IGraphPartitionner<Object>() {
			public Object getPartitionOf(Object value) {
				int index = order.indexOf(value);
				assert (index > 0) : "all vertices reached.";
				return partition[index];
			}
		});
		
		result.ended(p);
	}
	
}
