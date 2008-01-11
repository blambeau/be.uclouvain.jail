package be.uclouvain.jail.algo.induct.open;

import be.uclouvain.jail.algo.induct.internal.IWork;
import be.uclouvain.jail.algo.induct.internal.Simulation;
import be.uclouvain.jail.algo.induct.utils.FilteredSimuVisitor;

/** 
 * Allows simulation to be visited.
 *
 * <p>A visitor may be used to check some works of interest in order 
 * to implement oracles or debugging tools (for example). When visiting
 * a simulation, the onWork method is called for each work in the natural
 * work ordering.</p>
 * 
 * <p>This interface may be implemented. {@link FilteredSimuVisitor} may
 * be used as a useful base class.</p>
 */
public interface ISimuVisitor {

	/** Fired when the visitor visits a given work. */
	public abstract void onWork(Simulation simulation, IWork iwork);

}
