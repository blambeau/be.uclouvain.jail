package be.uclouvain.jail.fa.impl;

import be.uclouvain.jail.uinfo.IUserInfo;

/** 
 * Informer for finite automata. 
 * 
 * <p>This interface abstract the decoration that allows JAIL to see a graph as a 
 * finite automaton. It provides the methods to extract state mandatory attributes 
 * as well as edge letters from IUserInfo installed on graph vertices and edges.</p>
 * 
 * <p>Note: 'state' and 'edge' in the method's javadoc below must be interpreted as
 * 'state (or edge) from which the user info provided has been extracted'.</p>  
 * 
 * <p>This interface may be implemented. If required nformations are simply provided 
 * by attributes of IUserInfo, you should use {@link AttributeGraphFAInformer}.</p> 
 */
public interface IGraphFAInformer {

	/** Checks if a state is the initial state. */
	public boolean isInitial(IUserInfo s);
	
	/** Checks if a state is marked as accepting. */
	public boolean isAccepting(IUserInfo s);
	
	/** Checks if a state is marked as error. */
	public boolean isError(IUserInfo s);
	
	/** Returns an edge letter. */
	public Object edgeLetter(IUserInfo s);

}