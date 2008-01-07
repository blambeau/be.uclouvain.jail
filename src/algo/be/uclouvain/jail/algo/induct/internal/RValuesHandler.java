package be.uclouvain.jail.algo.induct.internal;

import be.uclouvain.jail.uinfo.IUserInfo;

/** Real values handler, working directly on states and edges. */
class RValuesHandler extends AbstractValuesHandler {

	/** Creates a values handler instance. */
	public RValuesHandler(InductionAlgo algo) {
		super(algo);
	}

	/** Extracts kernel state values. */
	public IUserInfo kStateUserInfo(Object kState) {
		return dfag.getVertexInfo(kState);
	}

	/** Extracts kernel edge values. */
	public IUserInfo kEdgeUserInfo(Object kEdge) {
		return dfag.getEdgeInfo(kEdge);
	}

	/** Extracts white state values. */
	public IUserInfo oStateUserInfo(PTAState oState) {
		return oState.getUserInfo();
	}

	/** Extracts white edge values. */
	public IUserInfo oEdgeUserInfo(PTAEdge oEdge) {
		return oEdge.getUserInfo();
	}

	/** Updates kernel state values. */
	public void updateKState(Object kState, IUserInfo values) {
		dfag.setVertexInfo(kState, values);
	}

	/** Updates kernel edge values. */
	public void updateKEdge(Object kEdge, IUserInfo values) {
		dfag.setEdgeInfo(kEdge, values);
	}

	/** Updates white state values. */
	public void updateOState(PTAState oState, IUserInfo values) {
		oState.setUserInfo(values);
	}

	/** Updates white edge values. */
	public void updateOEdge(PTAEdge oEdge, IUserInfo values) {
		oEdge.setUserInfo(values);
	}
}
