package be.uclouvain.jail.algo.fa.determinize;

import java.util.HashMap;
import java.util.Map;

import net.chefbe.javautils.adapt.AdaptUtils;
import be.uclouvain.jail.algo.fa.utils.FAEdgeGroup;
import be.uclouvain.jail.algo.fa.utils.FAStateGroup;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.functions.FAStateKindFunction;
import be.uclouvain.jail.fa.impl.AttributeGraphFAInformer;
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

	/** Resulting states. */
	private Map<FAStateGroup,Object> rStates;
	
	/** Aggregator to use for states. */
	private UserInfoAggregator stateAggregator;
	
	/** Aggregator to use for edges. */
	private UserInfoAggregator edgeAggregator;
	
	/** Creates a result instance. */
	public DefaultNFADeterminizerResult() {
		this.stateAggregator = new UserInfoAggregator();
		this.edgeAggregator = new UserInfoAggregator();
		stateAggregator.onFirst(AttributeGraphFAInformer.STATE_INITIAL_KEY,true,false);
		stateAggregator.stateKind(AttributeGraphFAInformer.STATE_KIND_KEY,
                FAStateKindFunction.OR,
                FAStateKindFunction.OR,true);
		edgeAggregator.first(AttributeGraphFAInformer.EDGE_LETTER_KEY);
	}

	/** Creates a result instance. */
	public DefaultNFADeterminizerResult(IDFA dfa) {
		this();
		this.dfa = dfa;
	}
	
	/** Returns the state aggregator. */
	public UserInfoAggregator getStateAggregator() {
		return stateAggregator;
	}

	/** Returns the edge aggregator. */
	public UserInfoAggregator getEdgeAggregator() {
		return edgeAggregator;
	}

	/** "Algorithm started" event. */
	public void started(INFADeterminizerInput input) {
		if (dfa == null) {
			dfa = new GraphDFA(input.getNFA().getAlphabet());
		}
		rStates = new HashMap<FAStateGroup,Object>();
	}

	/** "Algorithm ended" event. */
	public void ended() {
	}

	/** Creates a state in the resulting DFA. */
	public void createState(FAStateGroup state) {
		if (rStates.containsKey(state)) { throw new AssertionError("State not yet created."); }
		IDirectedGraph graph = dfa.getGraph();
		
		// create user info
		IUserInfo info = stateAggregator.create(state.getUserInfos());
		
		// create state and save it
		Object vertex = graph.createVertex(info);
		rStates.put(state,vertex);
	}
	
	/** Ensures that a target state has been created. */
	private Object ensure(FAStateGroup state) {
		if (!rStates.containsKey(state)) {
			throw new AssertionError("State previously created.");
		}
		return rStates.get(state);
	}
	
	/** Creates a result transition between two target states. */
	public void createTargetTransitions(FAStateGroup source, FAStateGroup target, FAEdgeGroup edges) {
		IUserInfo info = edgeAggregator.create(edges.getUserInfos());
		dfa.getGraph().createEdge(ensure(source), ensure(target), info);
	}

	/** Adapts to some types. */
	public <T> Object adapt(Class<T> c) {
		if (c.isAssignableFrom(getClass())) {
			return this;
		}
		
		// natural adaptation to a DFA
		if (IDFA.class.equals(c)) {
			return dfa;
		}
		
		return AdaptUtils.externalAdapt(this,c);
	}

}
