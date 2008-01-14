package be.uclouvain.jail.algo.induct.utils;

import be.uclouvain.jail.algo.induct.internal.IWork;
import be.uclouvain.jail.algo.induct.internal.PTAState;

/** Decorates a other state merge. */
public final class OStateMergeD extends WorkDecorator implements IMergeD {

	/** Creates a decorator. */
	public OStateMergeD(IWork work) {
		super(work);
	}

	/** Returns the target state. */
	public PTAState oState() {
		return (PTAState) work.target();
	}

	/** Returns the gained state (target of the gained edge). */
	public PTAState stateGain() {
		return (PTAState) work.victim();
	}

}
