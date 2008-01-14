package be.uclouvain.jail.algo.induct.utils;

import be.uclouvain.jail.algo.induct.internal.IWork;
import be.uclouvain.jail.algo.induct.internal.PTAEdge;
import be.uclouvain.jail.algo.induct.internal.PTAState;

/** Decorates Gain works to help creating membership queries. */ 
public interface IGainD extends IWork {

	/** Returns the PTAState which gains. */
	public PTAState targetInPTA();
	
	/** Returns gained edge. */
	public PTAEdge edgeGain();
	
	/** Returns letter on the gained edge. */
	public Object letter();
	
	/** Returns target of the gained edge. */
	public PTAState stateGain();
	
}
