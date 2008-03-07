package be.uclouvain.jail.algo.induct.listener;

import be.uclouvain.jail.algo.induct.internal.PTAEdge;
import be.uclouvain.jail.algo.induct.internal.PTAState;
import be.uclouvain.jail.algo.induct.internal.Simulation;

/**
 * Provides listening facilities on induction algorithms.
 * 
 * @author blambeau
 */
public interface IInductionListener {

	/** Fired when a new step begins. */
	public void newStep(Simulation simu);
	
	/** Consolidates an edge. */
	public void consolidate(PTAEdge edge);

	/** Consolidates an edge. */
	public void consolidate(PTAState state);

	/** Starts a try. */
	public void startTry(PTAEdge edge, Object kState);
	
	/** Merges a victim with a target state. */
	public void merge(PTAState victim, Object target);
	
	/** Merges a victim with a target state. */
	public void merge(PTAState victim, PTAState target);
	
	/** Merges a victim with a target edge. */
	public void merge(PTAEdge victim, Object target);
	
	/** Merges a victim with a target edge. */
	public void merge(PTAEdge victim, PTAEdge target);
	
	/** A kernel state gains a letter. */
	public void gain(Object kState, PTAEdge edge);
	
	/** A PTA state gains a letter. */
	public void gain(PTAState state, PTAEdge edge);
	
	/** Commits the try. */
	public void commit(Simulation simu);

	/** Rollbacks the try. 
	 * @param incompatibility TODO*/
	public void rollback(Simulation simu, boolean incompatibility);

}
