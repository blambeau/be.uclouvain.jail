package be.uclouvain.jail.algo.induct.listener;

import be.uclouvain.jail.algo.induct.internal.PTAEdge;
import be.uclouvain.jail.algo.induct.internal.PTAState;
import be.uclouvain.jail.algo.induct.internal.Simulation;
import be.uclouvain.jail.algo.induct.processor.ForwardLabelProcessor;

/** Logs all induction steps. */
public class InductionLogger extends InductionListenerHelper {

	/** Creates a logger instance. */
	public InductionLogger() {
		super("index");
	}
	
	/** Debugs a kState. */
	public String d(Object kState) {
		return "(" + kStateAttr(kState,"index") + " " 
		     + kStateAttr(kState, ForwardLabelProcessor.DEFAULT_TARGET_ATTR) + ")";
	}

	/** Debugs a kState. */
	public String d(PTAState kState) {
		return "(" + oStateAttr(kState,"index") + " " 
		     + oStateAttr(kState, ForwardLabelProcessor.DEFAULT_TARGET_ATTR) + ")";
	}
	
	public void startTry(PTAEdge edge, Object kState) {
		System.out.println("Trying " + d(edge.target()) + " with " + d(kState));
	}

	public void consolidate(PTAState state) {
		System.out.println("Consolidating " + d(state));
	}

	public void merge(PTAState victim, Object target) {
		System.out.println("\tMerging " + d(victim) + " with " + d(target));
	}

	public void merge(PTAState victim, PTAState target) {
		System.out.println("\tMerging " + d(victim) + " with " + d(target));
	}

	public void commit(Simulation simu) {
		System.out.println("ok.");
		super.commit(simu);
	}

	public void rollback(Simulation simu, boolean incompatibility) {
		System.out.println("rollback (" + (incompatibility ? "INC" : "TRY") +")!");
		super.rollback(simu, incompatibility);
	}

}
