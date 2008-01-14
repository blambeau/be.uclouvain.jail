package be.uclouvain.jail.algo.induct.utils;

import be.uclouvain.jail.algo.induct.internal.IWork;
import be.uclouvain.jail.algo.induct.internal.PTAState;
import be.uclouvain.jail.algo.induct.internal.Simulation;
import be.uclouvain.jail.algo.induct.internal.WorkType;

/** Decorates a kernel state merge. */
public class KStateMergeD implements IMergeD {

	/** Decorated work. */
	private IWork work;

	/** Creates a decorator. */
	public KStateMergeD(IWork work) {
		if (!WorkType.KStateMerge.equals(work.type())) {
			throw new IllegalArgumentException("KStateMerge work expected.");
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

	/** Returns the kernel state. */
	public Object kState() {
		return work.target();
	}

	/** Returns the target of the gained edge (aka gained state). */
	public PTAState stateGain() {
		return (PTAState) work.victim();
	}
	
}
