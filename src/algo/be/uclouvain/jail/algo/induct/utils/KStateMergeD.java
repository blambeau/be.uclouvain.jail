package be.uclouvain.jail.algo.induct.utils;

import be.uclouvain.jail.algo.induct.internal.IWork;
import be.uclouvain.jail.algo.induct.internal.PTAState;

/** Decorates a kernel state merge. */
public final class KStateMergeD extends WorkDecorator implements IMergeD {

	/** Creates a decorator. */
	public KStateMergeD(IWork work) {
		super(work);
	}

	/** Returns the kernel state. */
	public Object kState() {
		return work.target();
	}

	/** Returns the target of the gained edge (aka gained state). */
	public PTAState stateGain() {
		return (PTAState) work.victim();
	}
	
}
