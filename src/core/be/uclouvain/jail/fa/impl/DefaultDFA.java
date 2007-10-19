package be.uclouvain.jail.fa.impl;

import java.util.Collection;

import be.uclouvain.jail.adapt.AdaptUtils;
import be.uclouvain.jail.fa.DFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.adjacency.AdjacencyDirectedGraph;

/**
 * Default implementation of a deterministic automaton. 
 */
public class DefaultDFA implements DFA {

	/** Decorated graph. */
	private IDirectedGraph graph;
	
	/** Creates a DFA instance. */
	public DefaultDFA() {
		graph = new AdjacencyDirectedGraph(new DFAComponentFactory());
	}
	
	/** Returns underlying graph. */
	public IDirectedGraph getGraph() {
		return graph;
	}
	
	/** Asserts that a state is recognized. */
	protected void assertCorrectState(Object vertex) {
		if (vertex instanceof DFAVertex == false) {
			throw new IllegalArgumentException("Not a correct vertex " + vertex);
		}
	}

	/** Asserts that a state is recognized. */
	protected void assertCorrectEdge(Object edge) {
		if (edge instanceof DFAEdge == false) {
			throw new IllegalArgumentException("Not a correct edge " + edge);
		}
	}

	/** Checks if a state is the initial state. */
	public boolean isInitial(Object s) {
		assertCorrectState(s);
		return ((DFAVertex)s).isInitial();
	}
	
	/** Checks if a state is marked as accepting. */
	public boolean isAccepting(Object s) {
		assertCorrectState(s);
		return ((DFAVertex)s).isAccepting();
	}
	
	/** Checks if a state is marked as error. */
	public boolean isError(Object s) {
		assertCorrectState(s);
		return ((DFAVertex)s).isError();
	}
	
	/** Returns the initialState. */
	public Object initialState() {
		for (Object state: graph.getVertices()) {
			if (state instanceof DFAVertex == false) {
				throw new IllegalStateException("DFAVertex expected.");
			}
			if (((DFAVertex)state).isInitial()) {
				return state;
			}
		}
		throw new IllegalStateException("No initial state");
	}
	
	/** Returns the letter attached to an edge. */
	public Object edgeLetter(Object edge) {
		assertCorrectEdge(edge);
		return ((DFAEdge)edge).letter();
	}
	
	/** Returns state reached from s through letter. */
	public Object target(Object s, Object letter) {
		assertCorrectState(s);
		return ((DFAVertex)s).target(letter);
	}

	/** Returns outgoing letters of a state. */
	public Collection<Object> getIncomingLetters(Object s) {
		assertCorrectState(s);
		return ((DFAVertex)s).getIncomingLetters(graph);
	}
	
	/** Returns outgoing letters of a state. */
	public Collection<Object> getOutgoingLetters(Object s) {
		assertCorrectState(s);
		return ((DFAVertex)s).getOutgoingLetters(graph);
	}
	
	/** Adapts. */
	public <T> Object adapt(Class<T> c) {
		if (IDirectedGraph.class.equals(c)) {
			return graph;
		}
		return AdaptUtils.externalAdapt(this,c);
	}

}
