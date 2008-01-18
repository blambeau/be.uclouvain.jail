package be.uclouvain.jail.fa.utils;

import java.util.ArrayList;
import java.util.List;

import be.uclouvain.jail.fa.IAlphabet;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.IFATrace;
import be.uclouvain.jail.fa.IString;
import be.uclouvain.jail.fa.IWalkInfo;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.utils.DefaultDirectedGraphPath;

/**
 * Provides utilities to walk finite automata.
 * 
 * @author blambeau
 */
public class FAWalkUtils {

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
	
	/** Splits a word inside a DFA. */
	public static <L> IWalkInfo<L> stringWalk(IDFA fa, IString<L> word) {
		IDirectedGraph graph = fa.getGraph();

		// create accepted trace with only initial state
		Object current = fa.getInitialState();
		DefaultDirectedGraphPath accepted = new DefaultDirectedGraphPath(graph, current);
		List<L> rest = new ArrayList<L>();
		
		// walk accepted part
		boolean endReached = false;
		for (L letter: word) {
			if (endReached) {
				rest.add(letter);
			} else {
				Object edge = fa.getOutgoingEdge(current, letter);
				if (edge != null) {
					accepted.addEdge(edge);
					current = graph.getEdgeTarget(edge);
				} else {
					rest.add(letter);
					endReached = true;
				}
			}
		}
		
		// create split info
		IFATrace<L> acceptedT = new DefaultFATrace<L>(fa, accepted);
		IAlphabet<L> alph = fa.getAlphabet();
		IString<L> rejectedW    = new DefaultString<L>(alph,rest,true);
		return new DefaultWalkInfo<L>(word, acceptedT, rejectedW);
	}
		
}
