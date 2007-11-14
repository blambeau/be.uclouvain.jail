package be.uclouvain.jail.fa;

import java.util.Collection;

import be.uclouvain.jail.adapt.IAdaptable;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.uinfo.IUserInfo;

/**
 * Provides common methods of all finite state automaton classes.
 *  
 * @author blambeau
 */
public interface IFA extends IAdaptable {

	/** Returns the automaton alphabet. */
	public <T> IAlphabet<T> getAlphabet();
	
	/** Retruns underlying graph. */
	public IDirectedGraph getGraph();
	
	/** Checks if a state is the initial state. */
	public boolean isInitial(Object s);
	
	/** Checks if a state is marked as accepting. */
	public boolean isAccepting(Object s);
	
	/** Checks if a state is marked as error. */
	public boolean isError(Object s);
	
	/** Extracts the letter from an edge info. */
	public Object getEdgeLetter(IUserInfo info);
	
	/** Returns an edge letter. */
	public Object getEdgeLetter(Object s);
	
	/** Returns outgoing letters of a state. */
	public Collection<Object> getIncomingLetters(Object s);
	
	/** Returns outgoing letters of a state. */
	public Collection<Object> getOutgoingLetters(Object s);
	
}
