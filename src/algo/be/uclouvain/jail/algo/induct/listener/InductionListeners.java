package be.uclouvain.jail.algo.induct.listener;

import java.util.ArrayList;
import java.util.List;

import be.uclouvain.jail.algo.induct.internal.PTAEdge;
import be.uclouvain.jail.algo.induct.internal.PTAState;
import be.uclouvain.jail.algo.induct.internal.Simulation;

/**
 * Provides a collection of listeners.
 * 
 * @author blambeau
 */
public class InductionListeners implements IInductionListener {

	/** Listeners. */
	private List<IInductionListener> listeners = new ArrayList<IInductionListener>();
	
	/** Adds a listener. */
	public void addListener(IInductionListener l) {
		listeners.add(l);
	}
	
	/** On new step ... */
	public void newStep(Simulation simu) {
		for (IInductionListener l: listeners) {
			l.newStep(simu);
		}
	}

	/** Consolidates an edge. */
	public void consolidate(PTAEdge edge) {
		for (IInductionListener l: listeners) {
			l.consolidate(edge);
		}
	}

	/** Consolidates an edge. */
	public void consolidate(PTAState state) {
		for (IInductionListener l: listeners) {
			l.consolidate(state);
		}
	}

	/** Starts a try. */
	public void startTry(PTAEdge edge, Object kState) {
		for (IInductionListener l: listeners) {
			l.startTry(edge, kState);
		}
	}
	
	/** Merges a victim with a target state. */
	public void merge(PTAState victim, Object target) {
		for (IInductionListener l: listeners) {
			l.merge(victim, target);
		}
	}
	
	/** Merges a victim with a target state. */
	public void merge(PTAState victim, PTAState target) {
		for (IInductionListener l: listeners) {
			l.merge(victim, target);
		}
	}
	
	/** Merges a victim with a target edge. */
	public void merge(PTAEdge victim, Object target) {
		for (IInductionListener l: listeners) {
			l.merge(victim, target);
		}
	}
	
	/** Merges a victim with a target edge. */
	public void merge(PTAEdge victim, PTAEdge target) {
		for (IInductionListener l: listeners) {
			l.merge(victim, target);
		}
	}
	
	/** A kernel state gains a letter. */
	public void gain(Object kState, PTAEdge edge) {
		for (IInductionListener l: listeners) {
			l.gain(kState, edge);
		}
	}
	
	/** A PTA state gains a letter. */
	public void gain(PTAState state, PTAEdge edge) {
		for (IInductionListener l: listeners) {
			l.gain(state, edge);
		}
	}
	
	/** Commits the try. */
	public void commit(Simulation simu) {
		for (IInductionListener l: listeners) {
			l.commit(simu);
		}
	}

	/** Rollbacks the try. */
	public void rollback(Simulation simu, boolean incompatibility) {
		for (IInductionListener l: listeners) {
			l.rollback(simu, incompatibility);
		}
	}

}
