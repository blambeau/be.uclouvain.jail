package be.uclouvain.jail.algo.induct.listeners;

import be.uclouvain.jail.algo.induct.internal.InductionAlgo;
import be.uclouvain.jail.algo.induct.internal.PTAEdge;
import be.uclouvain.jail.algo.induct.internal.PTAState;
import be.uclouvain.jail.algo.induct.internal.Simulation;

/** Logs all induction steps. */
public class InductionLogger implements IInductionAlgoListener {

	public void initialize(InductionAlgo algo) {
	}

	public void startTry(PTAEdge edge, Object kState) {
		System.out.println("Trying " + edge.target() + " with " + kState);
	}

	public void consolidate(PTAEdge edge) {
		System.out.println("Consolidating " + edge);
	}

	public void consolidate(PTAState state) {
		System.out.println("Consolidating " + state);
	}

	public void gain(Object kState, PTAEdge edge) {
	}

	public void gain(PTAState state, PTAEdge edge) {
	}

	public void merge(PTAState victim, Object target) {
		System.out.println("\tMerging " + victim + " with " + target);
	}

	public void merge(PTAState victim, PTAState target) {
		System.out.println("\tMerging " + victim + " with " + target);
	}

	public void merge(PTAEdge victim, Object target) {
		System.out.println("\tMerging " + victim + " with " + target);
	}

	public void merge(PTAEdge victim, PTAEdge target) {
		System.out.println("\tMerging " + victim + " with " + target);
	}

	public void commit(Simulation simu) {
		System.out.println("Committing simu !!!!!!!!!!!!!");
	}

	public void rollback(Simulation simu) {
		System.out.println("Rollbacking simu !!!!!!!!!!!!!");
	}


}
