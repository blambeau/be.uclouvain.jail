package be.uclouvain.jail.fa;

import java.util.Collection;

import be.uclouvain.jail.fa.impl.GraphNFA;

/** 
 * Non Deterministic Finite Automaton (NDFA) contract.
 * 
 * <p>Please check the package.html to get some important informations
 * about the implementation of this contract.</p>
 * 
 * <p>This interface may be implemented. A default implementation on top
 * of a graph is also provided by {@link GraphNFA}.</p>
 * 
 * @author blambeau
 */
public interface INFA extends IFA {

	/** Returns NFA initial states. */
	public Iterable<Object> getInitialStates();
	
	/** Returns the outgoing edges of s labeled by the given letter, an
	 * empty iterable if no such edge. */
	public Collection<Object> getOutgoingEdges(Object s, Object letter);
	
}
