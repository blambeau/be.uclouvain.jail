package be.uclouvain.jail.algo.fa.merge;

import java.util.HashMap;
import java.util.Map;

import be.uclouvain.jail.algo.graph.merge.IGraphMergingResult;
import be.uclouvain.jail.algo.graph.utils.GraphEdgeGroup;
import be.uclouvain.jail.algo.graph.utils.GraphVertexGroup;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.impl.AttributeGraphFAInformer;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.UserInfoAggregator;

/**
 * Provides an implementation of {@link IGraphMergingResult} to be use
 * when merging a DFA.
 * 
 * @author blambeau
 */
public class DefaultDFAMergingResult implements IGraphMergingResult {

	/** DFA under construction. */
	private IDFA dfa;

	/** Underlying graph. */
	private IDirectedGraph graph;

	/** Aggregator to use for states. */
	private UserInfoAggregator stateAggregator;
	
	/** Aggregator to use for edges. */
	private UserInfoAggregator edgeAggregator;
	
	/** States. */
	private Map<GraphVertexGroup, Object> dfaStates;
	
	/** Creates a result instance. */
	public DefaultDFAMergingResult() {
		this.stateAggregator = new UserInfoAggregator();
		this.edgeAggregator = new UserInfoAggregator();
		stateAggregator.boolOr(AttributeGraphFAInformer.STATE_INITIAL_KEY);
		stateAggregator.boolOr(AttributeGraphFAInformer.STATE_ACCEPTING_KEY);
		stateAggregator.boolOr(AttributeGraphFAInformer.STATE_ERROR_KEY);
		edgeAggregator.first(AttributeGraphFAInformer.EDGE_LETTER_KEY);
	}

	/** Creates a result instance. */
	public DefaultDFAMergingResult(IDFA dfa) {
		this();
		this.dfa = dfa;
		this.graph = dfa.getGraph();
	}
	
	/** Sets the DFA. */
	public void setDFA(IDFA dfa) {
		this.dfa = dfa;
		this.graph = dfa.getGraph();
	}

	/** Returns the state aggregator. */
	public UserInfoAggregator getStateAggregator() {
		return stateAggregator;
	}

	/** Returns the edge aggregator. */
	public UserInfoAggregator getEdgeAggregator() {
		return edgeAggregator;
	}

	/** Ensures that a target state has been created. */
	private Object ensure(GraphVertexGroup state) {
		if (dfaStates == null) {
			dfaStates = new HashMap<GraphVertexGroup,Object>();
		}
		if (!dfaStates.containsKey(state)) {
			IDirectedGraph graph = dfa.getGraph();
			IUserInfo info = stateAggregator.create(state.getUserInfos());
			Object vertex = graph.createVertex(info);
			dfaStates.put(state,vertex);
		}
		return dfaStates.get(state);
	}
	
	/** Creates an edge between source and target. */
	public void createEdge(GraphVertexGroup sources, GraphVertexGroup targets, GraphEdgeGroup edges) {
		IUserInfo info = edgeAggregator.create(edges.getUserInfos());
		graph.createEdge(ensure(sources), ensure(targets), info);
	}

}
