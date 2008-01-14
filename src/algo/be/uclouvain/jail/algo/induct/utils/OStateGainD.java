package be.uclouvain.jail.algo.induct.utils;

import be.uclouvain.jail.algo.induct.internal.IWork;
import be.uclouvain.jail.algo.induct.internal.PTAEdge;
import be.uclouvain.jail.algo.induct.internal.PTAState;
import be.uclouvain.jail.algo.induct.internal.Simulation;
import be.uclouvain.jail.algo.induct.internal.WorkType;

/** Decorates another state gain. */
public class OStateGainD implements IGainD {

	/** Decorated work. */
	private IWork work;

	/** Creates a decorator. */
	public OStateGainD(IWork work) {
		if (!WorkType.OStateGain.equals(work.type())) {
			throw new IllegalArgumentException("OStateGain work expected.");
		} else {
			this.work = work;
			return;
		}
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

	/** Returns the target state. */
	public PTAState oState() {
		return (PTAState) work.target();
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
