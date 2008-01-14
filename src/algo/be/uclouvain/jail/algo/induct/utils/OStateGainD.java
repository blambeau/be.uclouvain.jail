package be.uclouvain.jail.algo.induct.utils;

import be.uclouvain.jail.algo.induct.internal.IWork;
import be.uclouvain.jail.algo.induct.internal.PTAEdge;
import be.uclouvain.jail.algo.induct.internal.PTAState;

/** Decorates another state gain. */
public final class OStateGainD extends WorkDecorator implements IGainD {

	/** Creates a decorator. */
	public OStateGainD(IWork work) {
		super(work);
	}

	/** Returns the target state. */
	public PTAState oState() {
		return (PTAState) work.target();
	}

	/** Returns the PTAState which gains. */
	public PTAState targetInPTA() {
		return oState();
	}
	
	/** Returns gained letter. */
	public Object letter() {
		return edgeGain().letter();
	}

	/** Returns gained edge. */
	public PTAEdge edgeGain() {
		Object victim = work.victim();
		assert (victim instanceof PTAEdge) : "Victim of a KStateGain is a PTAEdge.";
		return (PTAEdge) victim;
	}

	/** Returns the gained state (target of the gained edge). */
	public PTAState stateGain() {
		return edgeGain().target();
	}

}
