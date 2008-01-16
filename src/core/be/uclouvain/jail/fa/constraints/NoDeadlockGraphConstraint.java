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
			for (Object edge: graph.getOutgoingEdges(vertex)) {
				Object target = graph.getEdgeTarget(edge);
				if (!target.equals(vertex)) {
					return true;
				}
			}
		}
		return false;
	}

}
