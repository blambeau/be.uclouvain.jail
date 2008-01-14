package be.uclouvain.jail.algo.induct.utils;

import be.uclouvain.jail.algo.induct.internal.IWork;
import be.uclouvain.jail.algo.induct.internal.PTAState;

/** Decorates Merge works to help creating membership queries. */ 
public interface IMergeD extends IWork {

	/** Returns gained state. */
	public PTAState stateGain();
	
}
