package be.uclouvain.jail.algo.induct.utils;

import be.uclouvain.jail.algo.induct.internal.IWork;
import be.uclouvain.jail.algo.induct.internal.MappingUtils;
import be.uclouvain.jail.algo.induct.internal.PTAEdge;
import be.uclouvain.jail.algo.induct.internal.PTAState;

/** Decorates a KStateGain with utilities. */
public final class KStateGainD extends WorkDecorator implements IGainD {

	/** Creates a decorator instance. */
	public KStateGainD(IWork work) {
		super(work);
	}

	/** Returns the kernel state. */
	public Object kState() {
		return work.target();
	}

	/** Returns the letter of the gained edge. */
	public Object letter() {
		return edgeGain().letter();
	}

	/** Returns the PTAState which gains. */
	public PTAState targetInPTA() {
		Object kState = kState();
		assert (kState != null) : "kState of a KStateGain is never null.";
		PTAState mapped = MappingUtils.pRepresentor(simulation().getRunningAlgo(),kState);
		assert (mapped != null) : "Mapping of a kState is never null.";
		return mapped;
	}
	
	/** Returns the gained edge. */
	public PTAEdge edgeGain() {
		Object victim = work.victim();
		assert (victim instanceof PTAEdge) : "Victim of a KStateGain is a PTAEdge.";
		return (PTAEdge) victim;
	}

	/** Returns the target of the gained edge (aka gained state). */
	public PTAState stateGain() {
		return edgeGain().target();
	}

}
