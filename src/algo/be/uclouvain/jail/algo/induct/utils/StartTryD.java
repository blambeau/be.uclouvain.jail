package be.uclouvain.jail.algo.induct.utils;

import be.uclouvain.jail.algo.induct.internal.IWork;
import be.uclouvain.jail.algo.induct.internal.PTAEdge;
import be.uclouvain.jail.algo.induct.internal.PTAState;

/**
 * Decorates a start try.
 * 
 * @author blambeau
 */
public class StartTryD extends WorkDecorator {

	/** Creates a start try decorator. */
	public StartTryD(IWork work) {
		super(work);
	}

	/** Returns the victim edge. */
	public PTAEdge getVictimEdge() {
		return (PTAEdge) super.victim();
	}

	/** Returns the victim state. */
	public PTAState getVictimState() {
		return getVictimEdge().target();
	}

	/** Returns the target kernel state. */
	public Object getTargetState() {
		return target();
	}
	
	/** Returns the source kernel state of the merge. */
	public Object getSourceKernelState() {
		return getVictimEdge().getSourceKernelState();
	}
	
}
