package be.uclouvain.jail.algo.fa.compose;

import be.uclouvain.jail.algo.fa.utils.MultiDFAEdgeGroup;
import be.uclouvain.jail.algo.fa.utils.MultiDFAStateGroup;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.impl.AttributeGraphFAInformer;
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
	protected IDFA dfa;

	/** Underlying graph. */
	protected IDirectedGraph graph;

	/** Aggregator to use for states. */
	protected UserInfoAggregator stateAggregator;
	
	/** Aggregator to use for edges. */
	protected UserInfoAggregator edgeAggregator;
	
	/** Creates a result instance. */
	private DefaultDFAComposerResult() {
		this.stateAggregator = new UserInfoAggregator();
		this.edgeAggregator = new UserInfoAggregator();
		stateAggregator.boolAnd(AttributeGraphFAInformer.STATE_INITIAL_KEY);
		stateAggregator.boolAnd(AttributeGraphFAInformer.STATE_ACCEPTING_KEY);
		stateAggregator.boolOr(AttributeGraphFAInformer.STATE_ERROR_KEY);
		edgeAggregator.first(AttributeGraphFAInformer.EDGE_LETTER_KEY);
	}

	/** Creates a result instance. */
	public DefaultDFAComposerResult(IDFA dfa) {
		this();
		this.dfa = dfa;
		this.graph = dfa.getGraph();
	}

	/** Creates a result instance. */
	public DefaultDFAComposerResult(IDirectedGraph graph) {
		this();
		this.graph = graph;
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
	public Object createState(MultiDFAStateGroup sources) {
		IUserInfo info = stateAggregator.create(sources.getUserInfos());
		return graph.createVertex(info);
	}

	/** Creates an edge in the target DFA. */
	public Object createEdge(Object source, Object target, MultiDFAEdgeGroup edges) {
		IUserInfo info = edgeAggregator.create(edges.getUserInfos());
		return graph.createEdge(source,target,info);
	}

	/** Returns resulting DFA. */
	public IDFA getResultingDFA() {
		return dfa;
	}
	
}
