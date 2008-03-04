package be.uclouvain.jail.algo.induct.listeners;

import java.util.ArrayList;
import java.util.List;

import be.uclouvain.jail.algo.induct.internal.InductionAlgo;
import be.uclouvain.jail.algo.induct.internal.PTAEdge;
import be.uclouvain.jail.algo.induct.internal.PTAState;
import be.uclouvain.jail.algo.induct.internal.Simulation;

/**
 * Provides a collection of listeners.
 * 
 * @author blambeau
 */
public class InductionAlgoListeners implements IInductionAlgoListener {

	/** Listeners. */
	private List<IInductionAlgoListener> listeners = new ArrayList<IInductionAlgoListener>();
	
	/** Adds a listener. */
	public void addListener(IInductionAlgoListener l) {
		listeners.add(l);
	}
	
	/** Initializes the listener. */
	public void initialize(InductionAlgo algo) {
		for (IInductionAlgoListener l: listeners) {
			l.initialize(algo);
		}
	}
	
	/** Consolidates an edge. */
	public void consolidate(PTAEdge edge) {
		for (IInductionAlgoListener l: listeners) {
			l.consolidate(edge);
		}
	}

	/** Consolidates an edge. */
	public void consolidate(PTAState state) {
		for (IInductionAlgoListener l: listeners) {
			l.consolidate(state);
		}
	}

	/** Starts a try. */
	public void startTry(PTAEdge edge, Object kState) {
		for (IInductionAlgoListener l: listeners) {
			l.startTry(edge, kState);
		}
	}
	
	/** Merges a victim with a target state. */
	public void merge(PTAState victim, Object target) {
		for (IInductionAlgoListener l: listeners) {
			l.merge(victim, target);
		}
	}
	
	/** Merges a victim with a target state. */
	public void merge(PTAState victim, PTAState target) {
		for (IInductionAlgoListener l: listeners) {
			l.merge(victim, target);
		}
	}
	
	/** Merges a victim with a target edge. */
	public void merge(PTAEdge victim, Object target) {
		for (IInductionAlgoListener l: listeners) {
			l.merge(victim, target);
		}
	}
	
	/** Merges a victim with a target edge. */
	public void merge(PTAEdge victim, PTAEdge target) {
		for (IInductionAlgoListener l: listeners) {
			l.merge(victim, target);
		}
	}
	
	/** A kernel state gains a letter. */
	public void gain(Object kState, PTAEdge edge) {
		for (IInductionAlgoListener l: listeners) {
			l.gain(kState, edge);
		}
	}
	
	/** A PTA state gains a letter. */
	public void gain(PTAState state, PTAEdge edge) {
		for (IInductionAlgoListener l: listeners) {
			l.gain(state, edge);
		}
	}
	
	/** Commits the try. */
	public void commit(Simulation simu) {
		for (IInductionAlgoListener l: listeners) {
			l.commit(simu);
		}
	}

	/** Rollbacks the try. */
	public void rollback(Simulation simu) {
		for (IInductionAlgoListener l: listeners) {
			l.rollback(simu);
		}
	}

}
