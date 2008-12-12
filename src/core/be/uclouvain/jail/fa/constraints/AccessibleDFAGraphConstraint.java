package be.uclouvain.jail.fa.constraints;

import java.util.HashSet;
import java.util.Set;

import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.impl.GraphDFA;
import be.uclouvain.jail.graph.IDirectedGraph;

/**
 * Checks that all states are accessible from the initial state.
 * 
 * @author blambeau
 */
public class AccessibleDFAGraphConstraint extends DFAGraphConstraint {

	/** Checks that the constraint is respected by a graph. */
	public boolean isRespectedBy(IDirectedGraph graph) {
		if (!super.isRespectedBy(graph)) {
			return false;
		} else {
			IDFA dfa = new GraphDFA(graph, informer);
			Object init = dfa.getInitialState();
			
			Set<Object> reachable = new HashSet<Object>();
			dfs(graph, init, reachable);
			for (Object vertex: graph.getVertices()) {
				if (!reachable.contains(vertex)) {
					return false;
				}
			}
		}
		return true;
	}

	/** Do a depth-first search. */
	private void dfs(IDirectedGraph g, Object vertex, Set<Object> reachable) {
		reachable.add(vertex);
		for (Object edge: g.getOutgoingEdges(vertex)) {
			Object target = g.getEdgeTarget(edge);
			if (!reachable.contains(target)) {
				dfs(g, target, reachable);
			}
		}
	}
}
