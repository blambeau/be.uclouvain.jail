package be.uclouvain.jail.algo.induct.utils;

import be.uclouvain.jail.algo.induct.internal.IWork;
import be.uclouvain.jail.algo.induct.internal.PTAState;
import be.uclouvain.jail.algo.induct.internal.Simulation;
import be.uclouvain.jail.algo.induct.internal.WorkType;

/** Decorates a other state merge. */
public class OStateMergeD implements IMergeD {

	/** Decorated work. */
	private IWork work;

	/** Creates a decorator. */
	public OStateMergeD(IWork work) {
		if (!WorkType.OStateMerge.equals(work.type())) {
			throw new IllegalArgumentException("OStateMerge work expected.");
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

	/** Returns the gained state (target of the gained edge). */
	public PTAState stateGain() {
		return (PTAState) work.victim();
	}

}
