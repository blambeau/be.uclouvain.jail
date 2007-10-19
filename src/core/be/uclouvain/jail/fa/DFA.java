package be.uclouvain.jail.fa;

import java.util.Collection;

import be.uclouvain.jail.adapt.IAdaptable;
import be.uclouvain.jail.graph.IDirectedGraph;

/** 
 * Specialization of IFiniteAutomaton to be a deterministic one.
 * 
 * @author blambeau
 */
public interface DFA extends IAdaptable {

	/** Retruns underlying graph. */
	public IDirectedGraph getGraph();
	
	/** Checks if a state is the initial state. */
	public boolean isInitial(Object s);
	
	/** Checks if a state is marked as accepting. */
	public boolean isAccepting(Object s);
	
	/** Checks if a state is marked as error. */
	public boolean isError(Object s);
	
	/** Returns DFA initial state. */
	public Object initialState();
	
	/** Returns an edge letter. */
	public Object edgeLetter(Object s);
	
	/** Returns the target state reached from using a given letter, null
	 * if on such state. */
	public Object target(Object s, Object letter);
	
	/** Returns outgoing letters of a state. */
	public Collection<Object> getIncomingLetters(Object s);
	
	/** Returns outgoing letters of a state. */
	public Collection<Object> getOutgoingLetters(Object s);
	
}
