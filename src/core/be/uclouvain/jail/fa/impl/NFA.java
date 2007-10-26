package be.uclouvain.jail.fa.impl;

import java.util.Collection;

import be.uclouvain.jail.fa.IAlphabet;
import be.uclouvain.jail.fa.INFA;
import be.uclouvain.jail.graph.IDirectedGraph;

/**
 * Provides a useful decorator of INFA.
 * 
 * @author blambeau
 */
public class NFA {

	/** Decorated automaton. */
	private INFA nfa;

	/** Creates a decorator instance. */
	public NFA(INFA nfa) {
		this.nfa = nfa;
	}
	
	/** Returns the automaton alphabet. */
	public <T> IAlphabet<T> getAlphabet() {
		return nfa.getAlphabet();
	}
	
	/** Retruns underlying graph. */
	public IDirectedGraph getGraph() {
		return nfa.getGraph();
	}
	
	/** Checks if a state is the initial state. */
	public boolean isInitial(Object s) {
		return nfa.isInitial(s);
	}
	
	/** Checks if a state is marked as accepting. */
	public boolean isAccepting(Object s) {
		return nfa.isAccepting(s);
	}
	
	/** Checks if a state is marked as error. */
	public boolean isError(Object s) {
		return nfa.isError(s);
	}
	
	/** Returns NFA initial states. */
	public Iterable<Object> getInitialStates() {
		return nfa.getInitialStates();
	}
	
	/** Returns an edge letter. */
	public Object getEdgeLetter(Object s) {
		return nfa.getEdgeLetter(s);
	}
	
	/** Returns the outgoing edges of s labeled by the given letter, an
	 * empty iterable if no such edge. */
	public Collection<Object> getOutgoingEdges(Object s, Object letter) {
		return nfa.getOutgoingEdges(s, letter);
	}
	
	/** Returns outgoing letters of a state. */
	public Collection<Object> getIncomingLetters(Object s) {
		return nfa.getIncomingLetters(s);
	}
	
	/** Returns outgoing letters of a state. */
	public Collection<Object> getOutgoingLetters(Object s) {
		return nfa.getOutgoingLetters(s);
	}
	
}
