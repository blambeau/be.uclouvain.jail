package be.uclouvain.jail.fa.utils;

import java.util.ArrayList;
import java.util.List;

import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.INFA;
import be.uclouvain.jail.graph.IDirectedGraph;

/**
 * Provides utilities to walk finite automata.
 * 
 * @author blambeau
 */
public class FAWalkUtils {

	/** Walks a NFA. */
	public static <L> Object[] walk(INFA fa, Iterable<L> s) {
		IDirectedGraph graph = fa.getGraph();
		
		// create init states
		List<Object> current = new ArrayList<Object>();
		for (Object init: fa.getInitialStates()) {
			current.add(init);
		}
		
		// walk NFA
		int size = current.size();
		for (L letter: s) {
			for (int i=0; i<size; i++) {
				Object from = current.get(i);
				if (from == null) { continue; }
				Object edge = fa.getOutgoingEdges(from, letter);
				Object to = edge == null ? null : graph.getEdgeTarget(edge);
				current.set(i, to);
			}
		}
		
		// remove null targets
		for (int i=0; i<size; i++) {
			Object to = current.get(i);
			if (to == null) {
				current.remove(i--);
			}
		}
		
		return current.toArray();
	}
	
	/** Walks a DFA. */
	public static <L> Object walk(IDFA fa, Iterable<L> s) {
		IDirectedGraph graph = fa.getGraph();
		Object current = fa.getInitialState();
		for (L letter: s) {
			Object edge = fa.getOutgoingEdge(current, letter);
			if (edge == null) { return null; }
			current = graph.getEdgeTarget(edge);
		}
		return current;
	}
}
