package be.uclouvain.jail.algo.induct.internal;

import be.uclouvain.jail.algo.commons.Avoid;
import be.uclouvain.jail.uinfo.IUserInfo;

/** 
 * Handles IUserInfo on states and edges. 
 * 
 * <p>All accesses to user infos during an induction process MUST
 * be made through a values handler in order to avoid hurting the
 * transactional aspect of rpni-like induction algorithms. An handler 
 * thus encapsulates read/write of IUserInfo on states and edges of 
 * the target DFA as well as the decoration PTA. Moreover, it provides
 * a facade on user aggregation functions with the merge methods.</p> 
 */
public interface IValuesHandler {

	/** Reads the values of a kernel state. */
	public IUserInfo kStateUserInfo(Object s);

	/** Reads the values of a kernel edge. */
	public IUserInfo kEdgeUserInfo(Object e);

	/** Reads the values of a white PTA state. */
	public IUserInfo oStateUserInfo(PTAState s);

	/** Reads the values of a white PTA edge. */
	public IUserInfo oEdgeUserInfo(PTAEdge ptaedge);

	/** Computes values resulting of a state merge. */
	public IUserInfo mergeStateUserInfo(Object s, Object t) throws Avoid;

	/** Computes values resulting of an edge merge. */ 
	public IUserInfo mergeEdgeUserInfo(Object e, Object f) throws Avoid;

	/** Writes values of a kernel state. */
	public void updateKState(Object s, IUserInfo values);

	/** Writes values of a kernel edge. */
	public void updateKEdge(Object e, IUserInfo values);

	/** Writes values of a white state. */
	public void updateOState(PTAState state, IUserInfo values);

	/** Writes values of a white edge. */
	public void updateOEdge(PTAEdge e, IUserInfo values);

}
