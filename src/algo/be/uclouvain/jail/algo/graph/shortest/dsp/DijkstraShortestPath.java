package be.uclouvain.jail.algo.graph.shortest.dsp;

import java.util.ArrayList;
import java.util.List;

import be.uclouvain.jail.graph.IDirectedGraph;

/**
 * Implementation of the well known shortest path algorithm.
 * 
 * @author blambeau
 */
public class DijkstraShortestPath<T> {

	/** Input to use. */
	private IDSPInput<T> input;

	/** Output to use. */
	private IDSPOutput<T> output;
	
	/**
	 * Initializes the algorithm, returning the list of vertices to be explored. 
	 * 
	 * Each vertex graph is marked as being at an inifite distance to the rootVertex.
	 */
	protected List<Object> initialize() {
		IWeightInformer<T> weightInformer = input.getWeightInformer();
		IDirectedGraph graph = input.getGraph();
		Object rootVertex = input.getRootVertex();

		/* create the toExplore collection (Q in the pseudo code) */
		List<Object> toExplore = new ArrayList<Object>();

		/* initialization :
		 *   a) all distance to INFINITY except for the root.
		 *   b) predecessors are the vertex himself.
		 *   c) add each vertex in toExplore
		 */
		T nullDist = weightInformer.getNullDistance();
		T infinityDist = weightInformer.getInfinityDistance();

		for (Object vertex : graph.getVertices()) {
			T dist = (vertex.equals(rootVertex)) ? nullDist : infinityDist;
			output.reachVertex(vertex, dist, null);
			toExplore.add(vertex);
		}
		return toExplore;
	}

	/**
	 * Updates the distance of some vertex v2 if its better to use some edge
	 * between v2 and another vertex v1.
	 * 
	 * @param v1 a graph vertex.
	 * @param edge edge used between the two vertices.
	 * @param v2 another graph vertex.
	 */
	protected void updtDistance(Object v1, Object edge, Object v2) {
		IWeightInformer<T> weightInformer = input.getWeightInformer();
		IDirectedGraph graph = input.getGraph();

		// extract weight of the edge
		T weight = weightInformer.weight(graph, edge);

		// get distances of v1 and v2 vertices
		T v1Dist = output.getDistance(v1);
		T v2Dist = output.getDistance(v2);

		// updates distance
		T iDistPlusWeight = weightInformer.sum(v1Dist, weight);
		if (weightInformer.compare(v2Dist, iDistPlusWeight) > 0) {
			output.reachVertex(v2, iDistPlusWeight, edge);
		}
	}

	/** Returns the vertex marked as to be explored with the minimal distance 
	 * from the root. */
	protected Object minToBeExplored(List<Object> toExplore) {
		IWeightInformer<T> weightInformer = input.getWeightInformer();

		// min to infinity, vertex not found 
		T min = weightInformer.getInfinityDistance();
		Object toRemove = null;

		// find the minimal
		for (Object current : toExplore) {
			T currentDist = output.getDistance(current);
			if ((toRemove == null)
					|| (weightInformer.compare(min, currentDist) > 0)) {
				min = currentDist;
				toRemove = current;
			}
		}

		// remove it and return
		toExplore.remove(toRemove);
		return toRemove;
	}

	/** Computes the shortest path of each graph vertex to the root. */
	public synchronized void execute(IDSPInput<T> input, IDSPOutput<T> output) {
		this.input = input;
		this.output = output;
		IDirectedGraph graph = input.getGraph();
		
		// initialize
		List<Object> toExplore = initialize();
		
		// continue until no vertex has to be explored
		while (!toExplore.isEmpty()) {
			Object explored = minToBeExplored(toExplore);
			
			for (Object edge: graph.getOutgoingEdges(explored)) {
				Object target = graph.getEdgeTarget(edge);
				updtDistance(explored, edge, target);
			}
		}
	}

}
