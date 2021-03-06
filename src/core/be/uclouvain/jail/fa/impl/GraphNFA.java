package be.uclouvain.jail.fa.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import be.uclouvain.jail.fa.IAlphabet;
import be.uclouvain.jail.fa.INFA;
import be.uclouvain.jail.fa.utils.AutoAlphabet;
import be.uclouvain.jail.graph.GraphConstraintViolationException;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.adjacency.AdjacencyDirectedGraph;

/**
 * Default implementation of a non deterministic automaton.
 * 
 * <p>This INFA implementation act as a decorator of graph. This decoration
 * works with any correct graph implementation, but can be tuned according to 
 * the main specific constructor parameters.</p> 
 * 
 * <p>This class works with the following contributors. Please note that when
 * not provided by the user, the different constructors use the default classes 
 * indicated below for these contributors.</p>
 * <ul>
 *     <li>{@link IDirectedGraph} : graph implementation which provides the actual 
 *         DFA data-structure. Default class used: {@link AdjacencyDirectedGraph} 
 *         with a {@link NFAComponentFactory}.</li>
 *     <li>{@link IAlphabet} : actual collection of letters used by the automaton.
 *         Default class used: {@link AutoAlphabet}.</li>
 *     <li>{@link IGraphFAInformer} : informer used to retrieve state flags and edge
 *         letter inside IUserInfoS. Default class used: {@link AttributeGraphFAInformer}
 *         with default attribute keys.</li>
 * </ul>
 * 
 */
public class GraphNFA extends GraphFA implements INFA {

	/** Creates a NFA instance with all default contributors. */
	public GraphNFA() {
		this(new AttributeGraphFAInformer());
	}
	
	/** Creates a NFA instance with a user-defined alphabet. */
	public GraphNFA(IAlphabet alphabet) {
		this(new AdjacencyDirectedGraph(new NFAComponentFactory()),
				new AttributeGraphFAInformer(),alphabet);
	}
	
	/** Creates a NFA instance with a specific informer. */
	public GraphNFA(IGraphFAInformer informer) {
		this(new AdjacencyDirectedGraph(new NFAComponentFactory()),informer);
	}
	
	/** Creates a NFA instance on top of an existing graph. */
	public GraphNFA(IDirectedGraph graph) {
		this(graph,new AttributeGraphFAInformer());
	}
	
	/** Creates a DFA instance with specific graph and alphabet. */
	public GraphNFA(IDirectedGraph g, IAlphabet alphabet) {
		this(g, new AttributeGraphFAInformer(), alphabet);
	}

	/** Creates a NFA instance on top of an existing graph, with a specific
	 * informer. */
	public GraphNFA(IDirectedGraph graph, IGraphFAInformer informer) {
		super(graph,informer);
	}
	
	/** Creates a NFA instance with all specific constributors. */
	public GraphNFA(IDirectedGraph graph, IGraphFAInformer informer, IAlphabet alphabet) {
		super(graph,informer,alphabet);
	}
	
	/** Returns the initialState. */
	public Iterable<Object> getInitialStates() {
		List<Object> states = new ArrayList<Object>();
		for (Object state: graph.getVertices()) {
			if (isInitial(state)) { 
				states.add(state);
			}
		}
		if (states.isEmpty()) {
			throw new GraphConstraintViolationException(null,"No initial state");
		} else {
			return states;
		}
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
