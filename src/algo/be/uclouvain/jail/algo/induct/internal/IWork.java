package be.uclouvain.jail.algo.induct.internal;

import be.uclouvain.jail.algo.induct.utils.WorkDecorator;

/** 
 * Simulated step in a simulation. 
 *
 * <p>Each step of the induction process is a collection of works
 * to be applied if the simulation is commited. This interface 
 * provides the common contract for all works. A work always encapsulate
 * a victim and a target, whose semantics depends on the kind of work.</p>
 * 
 * <p>Please note that this interface is not intended to be used directly. 
 * Decorators for each kind of work are provided in the utils package.</p>
 */
public interface IWork {

	/** Returns the simulation. */
	public Simulation simulation();

	/** Returns the work type. */
	public WorkType type();

	/** Returns merge victim. */
	public Object victim();

	/** Returns merge target. */
	public Object target();
	
	/** Decorates the work. */
	public WorkDecorator decorate();
	
}
