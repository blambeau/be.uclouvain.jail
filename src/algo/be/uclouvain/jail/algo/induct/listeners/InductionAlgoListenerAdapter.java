package be.uclouvain.jail.algo.induct.listeners;

import be.uclouvain.jail.algo.induct.internal.InductionAlgo;
import be.uclouvain.jail.algo.induct.internal.PTAEdge;
import be.uclouvain.jail.algo.induct.internal.PTAState;
import be.uclouvain.jail.algo.induct.internal.Simulation;

/** Provides an adapter for listeners. */
public class InductionAlgoListenerAdapter implements IInductionAlgoListener {

	public void initialize(InductionAlgo algo) {
	}

	public void startTry(PTAEdge edge, Object kState) {
	}

	public void consolidate(PTAEdge edge) {
	}

	public void consolidate(PTAState state) {
	}

	public void gain(Object kState, PTAEdge edge) {
	}

	public void gain(PTAState state, PTAEdge edge) {
	}

	public void merge(PTAState victim, Object target) {
	}

	public void merge(PTAState victim, PTAState target) {
	}

	public void merge(PTAEdge victim, Object target) {
	}

	public void merge(PTAEdge victim, PTAEdge target) {
	}

	public void commit(Simulation simu) {
	}

	public void rollback(Simulation simu) {
	}

}
