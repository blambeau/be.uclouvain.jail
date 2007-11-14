package be.uclouvain.jail.fa;

import be.uclouvain.jail.fa.impl.GraphDFA;

/** 
 * Deterministic Finite Automaton (DFA) contract.
 * 
 * <p>Please check the package.html to get some important informations
 * about the implementation of this contract.</p>
 * 
 * <p>This interface may be implemented. A default implementation on top
 * of a graph is also provided by {@link GraphDFA}.</p>
 * 
 * @author blambeau
 */
public interface IDFA extends IFA {

	/** Returns DFA initial state. */
	public Object getInitialState();
	
	/** Returns the outgoing edge of s labeled by the given letter, null
	 * if no such edge. */
	public Object getOutgoingEdge(Object s, Object letter);
	
}
