package be.uclouvain.jail.fa;

import java.util.Collection;

import be.uclouvain.jail.adapt.IAdaptable;
import be.uclouvain.jail.fa.impl.GraphDFA;
import be.uclouvain.jail.graph.IDirectedGraph;

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
public interface IDFA extends IAdaptable {

	/** Retruns underlying graph. */
	public IDirectedGraph getGraph();
	
	/** Checks if a state is the initial state. */
	public boolean isInitial(Object s);
	
	/** Checks if a state is marked as accepting. */
	public boolean isAccepting(Object s);
	
	/** Checks if a state is marked as error. */
	public boolean isError(Object s);
	
	/** Returns DFA initial state. */
	public Object getInitialState();
	
	/** Returns an edge letter. */
	public Object getEdgeLetter(Object s);
	
	/** Returns the outgoing edge of s labeled by the given letter, null
	 * if no such edge. */
	public Object getOutgoingEdge(Object s, Object letter);
	
	/** Returns outgoing letters of a state. */
	public Collection<Object> getIncomingLetters(Object s);
	
	/** Returns outgoing letters of a state. */
	public Collection<Object> getOutgoingLetters(Object s);
	
}
