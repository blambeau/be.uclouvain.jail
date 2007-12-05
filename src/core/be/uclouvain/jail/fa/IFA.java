package be.uclouvain.jail.fa;

import java.util.Collection;

import net.chefbe.javautils.adapt.IAdaptable;
import be.uclouvain.jail.fa.impl.GraphDFA;
import be.uclouvain.jail.fa.impl.GraphNFA;
import be.uclouvain.jail.graph.IDirectedGraph;

/**
 * Provides common methods of all finite state automaton classes.
 * 
 * <p>Please check the package.html to get some important informations
 * about the implementation of this contract.</p>
 * 
 * <p>This interface may be implemented. Implementations on top of graphs
 * are provided by {@link GraphDFA} and {@link GraphNFA}.</p>
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
	
	/** Returns an edge letter. */
	public Object getEdgeLetter(Object s);
	
	/** Returns outgoing letters of a state. */
	public Collection<Object> getIncomingLetters(Object s);
	
	/** Returns outgoing letters of a state. */
	public Collection<Object> getOutgoingLetters(Object s);
	
}
