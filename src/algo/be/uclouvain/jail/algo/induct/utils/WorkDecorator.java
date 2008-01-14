package be.uclouvain.jail.algo.induct.utils;

import be.uclouvain.jail.algo.induct.internal.IWork;
import be.uclouvain.jail.algo.induct.internal.Simulation;
import be.uclouvain.jail.algo.induct.internal.WorkType;

/**
 * Base decoration of works.
 * 
 * @author blambeau
 */
public abstract class WorkDecorator implements IWork {

	/** Decorated work. */
	protected IWork work;

	/** Creates a decorator instance. */
	public WorkDecorator(IWork work) {
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

	/** Returns this. */
	public WorkDecorator decorate() {
		return this;
	}

}
