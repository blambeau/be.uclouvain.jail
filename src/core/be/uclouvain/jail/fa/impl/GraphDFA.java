package be.uclouvain.jail.fa.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import be.uclouvain.jail.fa.IAlphabet;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.adjacency.AdjacencyDirectedGraph;
import be.uclouvain.jail.graph.deco.GraphConstraintViolationException;

/**
 * Default implementation of a deterministic automaton. 
 * 
 * <p>This INFA implementation act as a decorator of graph. This decoration
 * works with any correct graph implementation, but can be tuned according to 
 * the main specific constructor parameters.</p> 
 */
public class GraphDFA extends GraphFA implements IDFA {

	/** 
	 * Creates a DFA instance.
	 * 
	 * <p>AdjacencyDirectedGraph(NFAComponentFactory) will be used as efficient data 
	 * structure for automaton implementation. Moreover, the AttributeGraphFAInformer
	 * with default attributes will be used.</p>
	 */
	public GraphDFA() {
		this(new AttributeGraphFAInformer());
	}
	
	/** 
	 * Creates a DFA instance.
	 * 
	 * <p>AdjacencyDirectedGraph(DFAComponentFactory) will be used as efficient data 
	 * structure for automaton implementation.</p>
	 * 
	 * @param informer DFA informer to get edge letter and state flags.
	 */
	public GraphDFA(IGraphFAInformer informer) {
		this(new AdjacencyDirectedGraph(new DFAComponentFactory()),informer);
	}
	
	/** 
	 * Creates a DFA instance.
	 * 
	 * <p>AdjacencyDirectedGraph(DFAComponentFactory) will be used as efficient data 
	 * structure for automaton implementation.</p>
	 * 
	 * @param informer DFA informer to get edge letter and state flags.
	 */
	public GraphDFA(IAlphabet alphabet) {
		this(new AdjacencyDirectedGraph(new DFAComponentFactory()),
			 new AttributeGraphFAInformer(),
			 alphabet);
	}
	
	/** 
	 * Creates a DFA instance. 
	 * 
	 * <p>This constructor can be used to decorate any graph as an automaton using
	 * the informer provided. Efficiency of the DFA cannot be garantied as actual
	 * data structure used by the underlying graph is unknown.</p>
	 */
	public GraphDFA(IDirectedGraph graph, IGraphFAInformer informer) {
		super(graph,informer);
	}
	
	/** 
	 * Creates a DFA instance. 
	 * 
	 * <p>This constructor can be used to decorate any graph as an automaton using
	 * the informer provided. Efficiency of the DFA cannot be garantied as actual
	 * data structure used by the underlying graph is unknown.</p>
	 */
	public GraphDFA(IDirectedGraph graph, IGraphFAInformer informer, IAlphabet alphabet) {
		super(graph,informer,alphabet);
	}
	
	/** Returns the initialState. */
	public Object getInitialState() {
		for (Object state: graph.getVertices()) {
			if (isInitial(state)) { return state; }
		}
		throw new GraphConstraintViolationException(null,"No initial state");
	}
	
	/** Returns the outgoing edge of s labeled by the given letter, null
	 * if no such edge. */
	public Object getOutgoingEdge(Object s, Object letter) {
		if (s instanceof DFAVertex) {
			return ((DFAVertex)s).getOutgoingEdge(letter);
		} else {
			for (Object edge: graph.getOutgoingEdges(s)) {
				if (letter.equals(getEdgeLetter(edge))) {
					return edge;
				}
			}
			return null;
		}
	}

	/** Returns outgoing letters of a state. */
	public Collection<Object> getIncomingLetters(Object s) {
		if (s instanceof DFAVertex) {
			return ((DFAVertex)s).getIncomingLetters(graph);
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
		if (s instanceof DFAVertex) {
			return ((DFAVertex)s).getOutgoingLetters(graph);
		} else {
			Set<Object> letters = new HashSet<Object>();
			for (Object edge: graph.getOutgoingEdges(s)) {
				letters.add(getEdgeLetter(edge));
			}
			return letters;
		}
	}
	
}
