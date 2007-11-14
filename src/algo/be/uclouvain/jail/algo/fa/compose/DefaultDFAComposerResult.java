package be.uclouvain.jail.algo.fa.compose;

import be.uclouvain.jail.algo.fa.utils.DFAEdgeGroup;
import be.uclouvain.jail.algo.fa.utils.DFAStateGroup;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.UserInfoAggregator;

/**
 * Provides a default implementation of IDFAComposerResult.
 * 
 * @author blambeau
 */
public class DefaultDFAComposerResult implements IDFAComposerResult {

	/** DFA under construction. */
	private IDFA dfa;

	/** Underlying graph. */
	private IDirectedGraph graph;

	/** Aggregator to use for states. */
	private UserInfoAggregator stateAggregator;
	
	/** Aggregator to use for edges. */
	private UserInfoAggregator edgeAggregator;
	
	/** Creates a result instance. */
	private DefaultDFAComposerResult() {
		this.stateAggregator = new UserInfoAggregator();
		this.edgeAggregator = new UserInfoAggregator();
		stateAggregator.boolAnd("isInitial");
		stateAggregator.boolAnd("isAccepting");
		stateAggregator.boolOr("isError");
		edgeAggregator.first("letter");
	}

	/** Creates a result instance. */
	public DefaultDFAComposerResult(IDFA dfa) {
		this();
		this.dfa = dfa;
		this.graph = dfa.getGraph();
	}

	/** Returns state aggregator. */
	public UserInfoAggregator getStateAggregator() {
		return stateAggregator;
	}
	
	/** Returns edge aggregator. */
	public UserInfoAggregator getEdgeAggregator() {
		return edgeAggregator;
	}
	
	/** Creates a state in the target DFA. */
	public Object createState(DFAStateGroup sources) {
		IUserInfo info = stateAggregator.create(sources.getUserInfos());
		return graph.createVertex(info);
	}

	/** Creates an edge in the target DFA. */
	public Object createEdge(Object source, Object target, DFAEdgeGroup edges) {
		IUserInfo info = edgeAggregator.create(edges.getUserInfos());
		return graph.createEdge(source,target,info);
	}

	/** Returns resulting DFA. */
	public IDFA getResultingDFA() {
		return dfa;
	}
	
}
