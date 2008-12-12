package be.uclouvain.jail.fa.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.chefbe.javautils.collections.singleton.SingletonIterable;
import be.uclouvain.jail.fa.IAlphabet;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.constraints.DFAGraphConstraint;
import be.uclouvain.jail.fa.utils.AutoAlphabet;
import be.uclouvain.jail.graph.GraphConstraintViolationException;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.adjacency.AdjacencyDirectedGraph;

/**
 * Implementation of a DFA on top of a IDirectedGraph. 
 * 
 * <p>This DFA implementation act as a decorator of graph. This decoration
 * works with any correct graph implementation, but can be tuned according to 
 * the main specific constructor parameters.</p> 
 * 
 * <p>This class works with the following contributors. Please note that when
 * not provided by the user, the different constructors use the default classes 
 * indicated below for these contributors.</p>
 * <ul>
 *     <li>{@link IDirectedGraph} : graph implementation which provides the actual 
 *         DFA data-structure. Default class used: {@link AdjacencyDirectedGraph} 
 *         with a {@link DFAComponentFactory}.</li>
 *     <li>{@link IAlphabet} : actual collection of letters used by the automaton.
 *         Default class used: {@link AutoAlphabet}.</li>
 *     <li>{@link IGraphFAInformer} : informer used to retrieve state flags and edge
 *         letter inside IUserInfoS. Default class used: {@link AttributeGraphFAInformer}
 *         with default attribute keys.</li>
 * </ul>
 * 
 */
public class GraphDFA extends GraphFA implements IDFA {

	/** Creates a DFA instance with all default contributors. */
	public GraphDFA() {
		this(new AttributeGraphFAInformer());
	}
	
	/** Creates a DFA instance with a specific user informer. */
	public GraphDFA(IGraphFAInformer informer) {
		this(new AdjacencyDirectedGraph(new DFAComponentFactory()),informer);
	}
	
	/** Creates a DFA instance with a specific alphabet. */
	public GraphDFA(IAlphabet alphabet) {
		this(new AdjacencyDirectedGraph(new DFAComponentFactory()),
			 new AttributeGraphFAInformer(),
			 alphabet);
	}
	
	/** Creates a DFA instance with specific graph and alphabet. */
	public GraphDFA(IDirectedGraph g, IAlphabet alphabet) {
		this(g, new AttributeGraphFAInformer(), alphabet);
	}

	/** Creates a DFA instance on top of an existing graph, with a specific informer. */
	public GraphDFA(IDirectedGraph graph, IGraphFAInformer informer) {
		super(graph,informer);
		checkGraph(graph);
	}
	
	/** Creates a DFA instance on top of an existing graph. */
	public GraphDFA(IDirectedGraph graph) {
		super(graph,new AttributeGraphFAInformer());
		checkGraph(graph);
	}
	
	/** Creates a DFA instance with specific graph informer and alphabet. */
	public GraphDFA(IDirectedGraph graph, IGraphFAInformer informer, IAlphabet alphabet) {
		super(graph,informer,alphabet);
		checkGraph(graph);
	}
	
	/** Checks that a graph is a DFA. */
	private void checkGraph(IDirectedGraph g) {
		if (g.getVerticesTotalOrder().size()>0) {
			if (!new DFAGraphConstraint().isRespectedBy(g)) {
				throw new IllegalArgumentException("Graph does not respected DFA structure.");
			}
		}
	}
	
	/** Returns the initialState. */
	public Object getInitialState() {
		for (Object state: graph.getVertices()) {
			if (isInitial(state)) { return state; }
		}
		throw new GraphConstraintViolationException(null,"No initial state");
	}
	
	/** Returns initial states. */
	public Iterable<Object> getInitialStates() {
		return new SingletonIterable<Object>(getInitialState());
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
