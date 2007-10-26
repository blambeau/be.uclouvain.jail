package be.uclouvain.jail.algo.fa.tmoves;

import java.util.Set;

import be.uclouvain.jail.fa.IDFA;
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

	/** NFA under construction. */
	private INFA nfa;

	/** Graph under construction. */
	private IDirectedGraph graph;

	/** State copier. */
	private UserInfoCopier stateCopier;

	/** Edge aggregator. */
	private UserInfoAggregator edgeAggregator;

	/** Creates a result that uses a existing nfa. */
	public DefaultTauRemoverResult(INFA nfa) {
		this();
		this.nfa = nfa;
	}

	/** Creates a result with a default NFA. */
	public DefaultTauRemoverResult() {
		stateCopier = new UserInfoCopier();
		stateCopier.keepAll();
		edgeAggregator = new UserInfoAggregator();
		edgeAggregator.first("letter");
	}

	/** "Algorithm started" event. */
	public void started(IDFA dfa) {
		if (nfa == null) {
			nfa = new GraphNFA(dfa.getAlphabet());
			graph = nfa.getGraph();
		}
	}

	/** "Algorithm ended" event. */
	public void ended() {
	}

	/**
	 * Creates a result target state mapped to a source state.
	 * 
	 * @param sourceState a source NFA state.
	 * @return an identifier for created target state (when id support is enabled). 
	 */
	public Object createTargetState(IUserInfo sourceState) {
		return graph.createVertex(stateCopier.create(sourceState));
	}

	/**
	 * Creates transitions in the result.
	 */
	public void createTargetTransitions(Set<Object> sources,
			Set<Object> targets, Set<IUserInfo> edges) {
		IUserInfo agg = edgeAggregator.create(edges);
		for (Object source : sources) {
			for (Object target : targets) {
				graph.createEdge(source, target, agg.copy());
			}
		}
	}

	/** Returns resulting NFA. */
	public INFA getResultingNFA() {
		return nfa;
	}

}
