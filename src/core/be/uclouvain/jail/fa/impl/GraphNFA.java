package be.uclouvain.jail.fa.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import be.uclouvain.jail.fa.INFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.adjacency.AdjacencyDirectedGraph;
import be.uclouvain.jail.graph.deco.GraphConstraintViolationException;

/**
 * Default implementation of a non deterministic automaton.
 * 
 * <p>This INFA implementation act as a decorator of graph. This decoration
 * works with any correct graph implementation, but can be tuned according to 
 * the main specific constructor parameters.</p> 
 */
public class GraphNFA extends GraphFA implements INFA {

	/** 
	 * Creates a NFA instance.
	 * 
	 * <p>AdjacencyDirectedGraph(NFAComponentFactory) will be used as efficient data 
	 * structure for automaton implementation. Moreover, the AttributeGraphFAInformer
	 * with default attributes will be used.</p>
	 */
	public GraphNFA() {
		this(new AttributeGraphFAInformer());
	}
	
	/** 
	 * Creates a NFA instance.
	 * 
	 * <p>AdjacencyDirectedGraph(NFAComponentFactory) will be used as efficient data 
	 * structure for automaton implementation.</p>
	 * 
	 * @param informer NFA informer to get edge letter and state flags.
	 */
	public GraphNFA(IGraphFAInformer informer) {
		this(new AdjacencyDirectedGraph(new NFAComponentFactory()),informer);
	}
	
	/** 
	 * Creates a NFA instance. 
	 * 
	 * <p>This constructor can be used to decorate any graph as an automaton using
	 * the informer provided. Efficiency of the NFA cannot be garantied as actual
	 * data structure used by the underlying graph is unknown.</p>
	 */
	public GraphNFA(IDirectedGraph graph, IGraphFAInformer informer) {
		super(graph,informer);
	}
	
	/** Returns the initialState. */
	public Iterable<Object> getInitialStates() {
		List<Object> states = new ArrayList<Object>();
		for (Object state: graph.getVertices()) {
			if (isInitial(state)) { 
				states.add(state);
			}
		}
		throw new GraphConstraintViolationException(null,"No initial state");
	}
	
	/** Returns the outgoing edge of s labeled by the given letter, null
	 * if no such edge. */
	@SuppressWarnings("unchecked")
	public Collection<Object> getOutgoingEdges(Object s, Object letter) {
		if (s instanceof NFAVertex) {
			Collection coll = ((NFAVertex)s).getOutgoingEdges(letter);
			return coll;
		} else {
			List<Object> edges = new ArrayList<Object>();
			for (Object edge: graph.getOutgoingEdges(s)) {
				if (letter.equals(getEdgeLetter(edge))) {
					edges.add(edge);
				}
			}
			return edges;
		}
	}

	/** Returns outgoing letters of a state. */
	public Collection<Object> getIncomingLetters(Object s) {
		if (s instanceof NFAVertex) {
			return ((NFAVertex)s).getIncomingLetters(graph);
		} else {
			Set<Object> letters = new HashSet<Object>();
			for (Object edge: graph.getIncomingEdges(s)) {
				letters.add(getEdgeLetter(edge));
			}
			return letters;
		}
	}
	
	/** Returns outgoing letters of a state. */
	public Collection<Object> getOutgoingLetters(Object s) {
		if (s instanceof NFAVertex) {
			return ((NFAVertex)s).getOutgoingLetters(graph);
		} else {
			Set<Object> letters = new HashSet<Object>();
			for (Object edge: graph.getOutgoingEdges(s)) {
				letters.add(getEdgeLetter(edge));
			}
			return letters;
		}
	}
	
}
