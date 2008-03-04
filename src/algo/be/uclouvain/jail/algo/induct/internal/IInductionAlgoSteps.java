package be.uclouvain.jail.algo.induct.internal;

/**
 * Provides listening facilities on induction algorithms.
 * 
 * @author blambeau
 */
public interface IInductionAlgoSteps {

	/** Consolidates an edge. */
	public void consolidate(PTAEdge edge);

	/** Consolidates a state. */
	public void consolidate(PTAState state);

	/** Starts a try. */
	public void startry(PTAEdge edge, Object kState);
	
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
	public void commit(PTAEdge edge, Object kState);

	/** Rollbacks the try. */
	public void rollback(PTAEdge edge, Object kState);

}
