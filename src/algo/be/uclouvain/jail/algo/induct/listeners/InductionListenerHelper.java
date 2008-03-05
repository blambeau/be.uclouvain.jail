package be.uclouvain.jail.algo.induct.listeners;

import be.uclouvain.jail.algo.induct.internal.IValuesHandler;
import be.uclouvain.jail.algo.induct.internal.InductionAlgo;
import be.uclouvain.jail.algo.induct.internal.PTAEdge;
import be.uclouvain.jail.algo.induct.internal.PTAState;
import be.uclouvain.jail.algo.induct.internal.Simulation;
import be.uclouvain.jail.uinfo.IUserInfo;

/** Provides an adapter for listeners. */
public class InductionListenerHelper implements IInductionListener {

	/** Values handler. */
	private IValuesHandler handler;
	
	/** Index attribute. */
	private String indexAttr;
	
	/** Returns the index of a PTA state. */
	public int oStateIndex(PTAState s) {
		IUserInfo info = handler.oStateUserInfo(s);
		return indexOf(info);
	}
	
	/** Returns the index of a kernel state. */
	public int kStateIndex(Object s) {
		IUserInfo info = handler.kStateUserInfo(s);
		return indexOf(info);
	}
	
	/** Returns the index of a PTA edge. */
	public int edgeIndex(PTAEdge s) {
		IUserInfo info = handler.oEdgeUserInfo(s);
		return indexOf(info);
	}
	
	/** Returns the index of a kernel edge. */
	public int edgeIndex(Object s) {
		IUserInfo info = handler.kEdgeUserInfo(s);
		return indexOf(info);
	}
	
	/** Extracts the index from an info. */
	public int indexOf(IUserInfo info) {
		Object i = info.getAttribute(indexAttr);
		assert (i instanceof Integer) : "Valid index in infos.";
		return (Integer) i;
	}
	
	/** On initialization. */
	public void initialize(InductionAlgo algo) {
		this.indexAttr = algo.getInfo().getRepresentorAttr();
	}

	/** On a new step. */
	public void newStep(Simulation simu) {
		this.handler = simu.getValuesHandler();
	}

	/** On new try ... */
	public void startTry(PTAEdge edge, Object kState) {
	}

	/** On edge consolidation ... */
	public void consolidate(PTAEdge edge) {
	}

	/** On state consolidation ... */
	public void consolidate(PTAState state) {
	}

	/** On kernel state gain ... */
	public void gain(Object kState, PTAEdge edge) {
	}

	/** On PTA state gain ... */
	public void gain(PTAState state, PTAEdge edge) {
	}

	/** On merge ... */
	public void merge(PTAState victim, Object target) {
	}

	/** On merge ... */
	public void merge(PTAState victim, PTAState target) {
	}

	/** On merge ... */
	public void merge(PTAEdge victim, Object target) {
	}

	/** On merge ... */
	public void merge(PTAEdge victim, PTAEdge target) {
	}

	/** On try commit ... */
	public void commit(Simulation simu) {
	}

	/** On try rollback ... */
	public void rollback(Simulation simu) {
	}

}
