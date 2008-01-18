package be.uclouvain.jail.fa.constraints;

import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.constraints.AbstractGraphConstraint;

/**
 * Ensures that a graph contains no deadlock state.
 * 
 * @author blambeau
 */
public class NoDeadlockGraphConstraint extends AbstractGraphConstraint {

	/** Checks a graph. */
	@Override
	public boolean isRespectedBy(IDirectedGraph graph) {
		// check each vertex
		for (Object vertex: graph.getVertices()) {
			// found an outgoing edge to another vertex?
			boolean found = false;
			
			// check each outgoing edge
			for (Object edge: graph.getOutgoingEdges(vertex)) {
				Object target = graph.getEdgeTarget(edge);
				
				// lead to another one?
				if (!target.equals(vertex)) {
					found = true;
					break;
				}
			}
			
			// if such an edge is not found then KO
			if (!found) {
				return false;
			}
		}
		
		// seems ok
		return true;
	}

}
