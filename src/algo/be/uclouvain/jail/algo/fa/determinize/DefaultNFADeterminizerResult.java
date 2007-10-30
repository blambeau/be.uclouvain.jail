package be.uclouvain.jail.algo.fa.determinize;

import java.util.Set;

import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.INFA;
import be.uclouvain.jail.fa.impl.GraphDFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.UserInfoAggregator;

/**
 * Provides a default implementation of {@link INFADeterminizerResult}.
 * 
 * @author blambeau
 */
public class DefaultNFADeterminizerResult implements INFADeterminizerResult {

	/** DFA under construction. */
	private IDFA dfa;

	/** Underlying graph. */
	private IDirectedGraph graph;

	/** Aggregator to use for states. */
	private UserInfoAggregator stateAggregator;
	
	/** Aggregator to use for edges. */
	private UserInfoAggregator edgeAggregator;
	
	/** Creates a result instance. */
	public DefaultNFADeterminizerResult() {
		this.stateAggregator = new UserInfoAggregator();
		this.edgeAggregator = new UserInfoAggregator();
		stateAggregator.boolAnd("isInitial");
		stateAggregator.boolOr("isAccepting");
		stateAggregator.boolOr("isError");
		edgeAggregator.first("letter");
	}

	/** Creates a result instance. */
	public DefaultNFADeterminizerResult(IDFA dfa) {
		this();
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

	/**
	 * "Algorithm started" event.
	 * 
	 * @param source the source NFA which is being determinized.
	 */
	public void started(INFA nfa) {
		if (dfa == null) {
			dfa = new GraphDFA(nfa.getAlphabet());
			graph = dfa.getGraph();
		}
	}

	/** "Algorithm ended" event. */
	public void ended() {
	}

	/** Debus a state def. */
	@SuppressWarnings("unused")
	private String debugStateDef(Set<IUserInfo> def) {
		StringBuffer sb = new StringBuffer();
		sb.append('[');
		int i=0;
		for (Object d: def) {
			if (i++ != 0) { sb.append(','); }
			sb.append(d);
		}
		sb.append(']');
		return sb.toString();
	}
	
	/** Debugs an edge def. */
	@SuppressWarnings("unused")
	private String debugEdge(IUserInfo info) {
		return info.getAttribute("letter").toString();
	}
	
	/**
	 * Creates a result state from a definition (a collection of source NFA states).
	 * 
	 * <p>This method may return an object identifying the state created in the equivalent DFA
	 * under construction. When creating the DFA on the fly, returning such an identifier is a 
	 * efficient memory solution, as the algorithm must keep the <code>def</code> for internal
	 * implementation reasons.</p>
	 * 
	 * @param def a definition of result state.
	 * @return an identifier of the resulting state in the equivalent DFA. 
	 */
	public Object createTargetState(Set<IUserInfo> def) {
		//System.out.println("Creating target state for: " + debugStateDef(def));
		IUserInfo info = stateAggregator.create(def);
		Object vertex = graph.createVertex(info);
		return vertex;
	}

	/**
	 * Creates a result transition between two target states.
	 * 
	 * @param source the source of the transition 
	 * (a result state identifier, previously returned by {@link #createTargetState(Set<Object>)} method). 
	 * @param edges the set of NFA edges that merge.
	 * @param target the target of the transition
	 * (a result state identifier, previously returned by {@link #createTargetState(Set<Object>)} method). 
	 */
	public void createTargetTransitions(Object source, Object target, Set<IUserInfo> edges) {
		//System.out.println("Creating transition between: " + source + " and " + target);
		IUserInfo info = edgeAggregator.create(edges);
		graph.createEdge(source, target, info);
	}

	/** Returns the computed DFA. */
	public IDFA getResultingDFA() {
		return dfa;
	}

}
