package be.uclouvain.jail.algo.induct.internal;

import java.util.HashMap;
import java.util.Map;

import be.uclouvain.jail.uinfo.IUserInfo;

/** 
 * Commitable values handler. 
 * 
 * <p>This class is introduced to provide transaction support on a 
 * IValuesHandler. The later is actually decorated in the sense that 
 * update methods are implemented as IUserInfo caching. The affected 
 * infos are only updated on real decorated handler when commit method 
 * is invoked. Read methods are also overrided to return the last known 
 * IUserInfoS.</p> 
 */
final class CValuesHandler extends AbstractValuesHandler {

	/** Parent values handler to use. */
	private IValuesHandler parent;

	/** Kernel state values. */
	private Map<Object,IUserInfo> kStateIUserInfo;

	/** Kernel edge values. */
	private Map<Object,IUserInfo> kEdgeIUserInfo;

	/** Other state values. */
	private Map<PTAState,IUserInfo> oStateIUserInfo;

	/** Other edge values. */
	private Map<PTAEdge,IUserInfo> oEdgeIUserInfo;

	/** Decorates a values handler with transactional support. */
	public CValuesHandler(InductionAlgo algo, IValuesHandler parent) {
		super(algo);
		kStateIUserInfo = new HashMap<Object,IUserInfo>();
		kEdgeIUserInfo = new HashMap<Object,IUserInfo>();
		oStateIUserInfo = new HashMap<PTAState,IUserInfo>();
		oEdgeIUserInfo = new HashMap<PTAEdge,IUserInfo>();
		this.parent = parent;
	}

	/** Returns values of a kernel state. */
	public IUserInfo kStateUserInfo(Object kState) {
		IUserInfo v = (IUserInfo) kStateIUserInfo.get(kState);
		if (v == null) {
			v = parent.kStateUserInfo(kState);
			kStateIUserInfo.put(kState, v);
		}
		return v;
	}

	/** Returns values of a kernel edge. */
	public IUserInfo kEdgeUserInfo(Object kEdge) {
		IUserInfo v = (IUserInfo) kEdgeIUserInfo.get(kEdge);
		if (v == null) {
			v = parent.kEdgeUserInfo(kEdge);
			kEdgeIUserInfo.put(kEdge, v);
		}
		return v;
	}

	/** Returns values of a white state. */
	public IUserInfo oStateUserInfo(PTAState oState) {
		IUserInfo v = (IUserInfo) oStateIUserInfo.get(oState);
		if (v == null) {
			v = parent.oStateUserInfo(oState);
			oStateIUserInfo.put(oState, v);
		}
		return v;
	}

	/** Returns values of a white edge. */
	public IUserInfo oEdgeUserInfo(PTAEdge oEdge) {
		IUserInfo v = (IUserInfo) oEdgeIUserInfo.get(oEdge);
		if (v == null) {
			v = parent.oEdgeUserInfo(oEdge);
			oEdgeIUserInfo.put(oEdge, v);
		}
		return v;
	}

	/** Updates values of a kernel state. */
	public void updateKState(Object kState, IUserInfo values) {
		kStateIUserInfo.put(kState, values);
	}

	/** Updates values of a kernel edge. */
	public void updateKEdge(Object kEdge, IUserInfo values) {
		kEdgeIUserInfo.put(kEdge, values);
	}

	/** Updates values of a white state. */
	public void updateOState(PTAState oState, IUserInfo values) {
		oStateIUserInfo.put(oState, values);
	}

	/** Updates values of a white edge. */
	public void updateOEdge(PTAEdge oEdge, IUserInfo values) {
		oEdgeIUserInfo.put(oEdge, values);
	}

	/** Commits the computed values. */
	public void commit(boolean clean) {
		// commit kState values
		for (Object kState : kStateIUserInfo.keySet()) {
			IUserInfo v = (IUserInfo) kStateIUserInfo.get(kState);
			parent.updateKState(kState, v);
		}

		// commit kEdge values
		for (Object kEdge : kEdgeIUserInfo.keySet()) {
			IUserInfo v = (IUserInfo) kEdgeIUserInfo.get(kEdge);
			parent.updateKEdge(kEdge, v);
		}

		// commit oState values
		for (PTAState oState : oStateIUserInfo.keySet()) {
			IUserInfo v = (IUserInfo) oStateIUserInfo.get(oState);
			parent.updateOState(oState, v);
		}

		// commit oEdge values
		for (PTAEdge oEdge : oEdgeIUserInfo.keySet()) {
			IUserInfo v = (IUserInfo) oEdgeIUserInfo.get(oEdge);
			parent.updateOEdge(oEdge, v);
		}

		// clean all if required
		if (clean) {
			kStateIUserInfo.clear();
			kEdgeIUserInfo.clear();
			oStateIUserInfo.clear();
			oEdgeIUserInfo.clear();
		}
	}
}
