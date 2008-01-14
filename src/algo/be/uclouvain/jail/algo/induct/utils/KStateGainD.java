package be.uclouvain.jail.algo.induct.utils;

import be.uclouvain.jail.algo.induct.internal.IWork;
import be.uclouvain.jail.algo.induct.internal.PTAEdge;
import be.uclouvain.jail.algo.induct.internal.PTAState;
import be.uclouvain.jail.algo.induct.internal.Simulation;
import be.uclouvain.jail.algo.induct.internal.WorkType;

/** Decorates a KStateGain with utilities. */
public class KStateGainD implements IGainD {

	/** Decorated work. */
	private IWork work;

	/** Creates a decorator instance. */
	public KStateGainD(IWork work) {
		if (!WorkType.KStateGain.equals(work.type())) {
			throw new IllegalArgumentException("KStateGain work expected.");
		}
		this.work = work;
	}

	/** Returns the simulation. */
	public Simulation simulation() {
		return work.simulation();
	}

	/** Returns work type. */
	public WorkType type() {
		return work.type();
	}

	/** Returns work's target. */
	public Object target() {
		return work.target();
	}

	/** Returns work's victim. */
	public Object victim() {
		return work.victim();
	}

	/** Returns the kernel state. */
	public Object kState() {
		return work.target();
	}

	/** Returns the letter of the gained edge. */
	public Object letter() {
		return edgeGain().letter();
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
