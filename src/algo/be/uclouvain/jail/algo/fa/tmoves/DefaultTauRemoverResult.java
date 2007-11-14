package be.uclouvain.jail.algo.fa.tmoves;

import java.util.HashMap;
import java.util.Map;

import be.uclouvain.jail.adapt.AdaptUtils;
import be.uclouvain.jail.algo.fa.utils.FAEdgeGroup;
import be.uclouvain.jail.algo.fa.utils.FAStateGroup;
import be.uclouvain.jail.fa.IFA;
import be.uclouvain.jail.fa.INFA;
import be.uclouvain.jail.fa.impl.GraphNFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.UserInfoAggregator;
import be.uclouvain.jail.uinfo.UserInfoCopier;

/**
 * Provides a default implementation of {@link ITauRemoverResult}.
 * 
 * @author blambeau
 */
public class DefaultTauRemoverResult implements ITauRemoverResult {

	/** Source FA. */
	private IFA source;
	
	/** NFA under construction. */
	private INFA result;

	/** NFA target states by source states. */ 
	private Map<Object,Object> nfaStates;
	
	/** State copier. */
	private UserInfoCopier stateCopier;

	/** Edge aggregator. */
	private UserInfoAggregator edgeAggregator;

	/** Creates a result that uses a existing nfa. */
	public DefaultTauRemoverResult(INFA nfa) {
		this();
		this.result = nfa;
	}

	/** Creates a result with a default NFA. */
	public DefaultTauRemoverResult() {
		stateCopier = new UserInfoCopier();
		stateCopier.keepAll();
		edgeAggregator = new UserInfoAggregator();
		edgeAggregator.first("letter");
	}

	/** Returns the state copier. */
	public UserInfoCopier getStateCopier() {
		return stateCopier;
	}
	
	/** Returns the edge aggregator. */
	public UserInfoAggregator getEdgeAggregator() {
		return edgeAggregator;
	}
	
	/** "Algorithm started" event. */
	public void started(ITauRemoverInput input) {
		this.source = input.getFA();
		if (result == null) {
			result = new GraphNFA(source.getAlphabet());
		}
	}

	/** "Algorithm ended" event. */
	public void ended() {
	}

	/** Ensures that a target state has been created. */
	private Object ensure(Object state) {
		if (nfaStates == null) {
			nfaStates = new HashMap<Object,Object>();
		}
		if (nfaStates.containsKey(state)) {
			return nfaStates.get(state);
		} else {
			IUserInfo info = source.getGraph().getVertexInfo(state);
			info = stateCopier.create(info);
			Object target = result.getGraph().createVertex(info);
			nfaStates.put(state, target);
			return target;
		}
	}
	
	/** Creates transitions in the result. */
	public void createTargetTransitions(Object source, FAStateGroup targets, FAEdgeGroup edges) {
		IDirectedGraph graph = result.getGraph();
		Object nfaSource = ensure(source);
		IUserInfo agg = edgeAggregator.create(edges.getUserInfos());
		for (Object target : targets) {
			Object nfaTarget = ensure(target);
			graph.createEdge(nfaSource, nfaTarget, agg.copy());
		}
	}

	/** Adapts to some types. */
	public <T> Object adapt(Class<T> c) {
		if (c.isAssignableFrom(getClass())) {
			return this;
		}
		
		// natural adaptation to a NFA
		if (INFA.class.equals(c)) {
			return result;
		}
		
		return AdaptUtils.externalAdapt(this,c);
	}

}
