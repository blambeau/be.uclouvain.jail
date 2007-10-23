package be.uclouvain.jail.fa.impl;

import be.uclouvain.jail.uinfo.IUserInfo;

/** Informer for finite automata. */
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
