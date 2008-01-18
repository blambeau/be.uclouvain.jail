package be.uclouvain.jail.algo.induct.internal;

import be.uclouvain.jail.algo.commons.Avoid;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.IUserInfoHandler;
import be.uclouvain.jail.uinfo.UserInfoAggregator;

/** 
 * Helper to implement IValuesHandler. 
 * 
 * <p>This helper already install utilities to make the job. In particular it 
 * encapsulates state and edge aggregators used by the induction algorithm to
 * let user informations be kept in the resulting DFA.</p>
 */
abstract class AbstractValuesHandler implements IValuesHandler {

	/** Induction algorithm. */ 
	protected InductionAlgo algo;

	/** Target DFA under construction. */
	protected IDFA dfa;

	/** Underlying graph. */
	protected IDirectedGraph dfag;
	
	/** Functions installed on states. */
	protected UserInfoAggregator stateAggregator;

	/** Functions installed on edges. */
	protected UserInfoAggregator edgeAggregator;

	/** Creates an handler instance. */
	public AbstractValuesHandler(InductionAlgo algo) {
		this.algo = algo;
		this.dfa = algo.getDFA();
		this.dfag = dfa.getGraph();
		IUserInfoHandler handler = algo.getInfo().getInput().getUserInfoHandler();
		this.stateAggregator = handler.getVertexAggregator();
		this.edgeAggregator = handler.getEdgeAggregator();
	}

	/** Merges some state values. */
	public IUserInfo mergeStateUserInfo(Object s, Object t) throws Avoid {
		IUserInfo v = (s instanceof PTAState) ? oStateUserInfo((PTAState) s)
				: kStateUserInfo(s);
		IUserInfo w = (t instanceof PTAState) ? oStateUserInfo((PTAState) t)
				: kStateUserInfo(t);
		return stateAggregator.create(v,w);
	}

	/** Merges some edge values. */
	public IUserInfo mergeEdgeUserInfo(Object s, Object t) throws Avoid {
		IUserInfo v = (s instanceof PTAEdge) ? oEdgeUserInfo((PTAEdge) s)
				: kEdgeUserInfo(s);
		IUserInfo w = (t instanceof PTAEdge) ? oEdgeUserInfo((PTAEdge) t)
				: kEdgeUserInfo(t);
		return edgeAggregator.create(v,w);
	}
	
	/** Checks that a kState is a correct kState. */
	protected boolean correctKState(Object kState) {
		return dfag.getVertexInfo(kState) != null;
	}
	
	/** Checks that a kEdge is a correct kEdge. */
	protected boolean correctKEdge(Object kEdge) {
		return dfag.getEdgeInfo(kEdge) != null;
	}
	
}
