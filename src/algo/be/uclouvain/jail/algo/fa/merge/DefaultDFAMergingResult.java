package be.uclouvain.jail.algo.fa.merge;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import be.uclouvain.jail.algo.graph.merge.IGraphMergingResult;
import be.uclouvain.jail.fa.IDFA;
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
	
	/** Creates a result instance. */
	public DefaultDFAMergingResult() {
		this.stateAggregator = new UserInfoAggregator();
		this.edgeAggregator = new UserInfoAggregator();
		stateAggregator.boolOr("isInitial");
		stateAggregator.boolOr("isAccepting");
		stateAggregator.boolOr("isError");
		edgeAggregator.first("letter");
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

	/** Creates a vertex. */
	public Object createVertex(Set<IUserInfo> infos) {
		IUserInfo info = stateAggregator.create(infos);
		return graph.createVertex(info);
	}

	/** Creates some edges. */
	public void createEdge(Object source, Object target, Set<IUserInfo> edgeInfo) {
		// create by letter aggregates
		Map<Object,Set<IUserInfo>> edges = new HashMap<Object,Set<IUserInfo>>();
		for (IUserInfo info: edgeInfo) {
			Object letter = dfa.getEdgeLetter(info);
			
			Set<IUserInfo> lInfo = edges.get(letter);
			if (lInfo==null) {
				lInfo = new HashSet<IUserInfo>();
				edges.put(letter, lInfo);
			}
			
			lInfo.add(info);
		}
		
		// create edges
		for (Object letter: edges.keySet()) {
			Set<IUserInfo> lInfo = edges.get(letter);
			graph.createEdge(source, target, edgeAggregator.create(lInfo));
		}
	}

}
