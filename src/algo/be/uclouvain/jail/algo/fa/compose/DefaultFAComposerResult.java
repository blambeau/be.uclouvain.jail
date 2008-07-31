package be.uclouvain.jail.algo.fa.compose;

import java.util.HashMap;
import java.util.Map;

import be.uclouvain.jail.algo.fa.utils.MultiFAEdgeGroup;
import be.uclouvain.jail.algo.fa.utils.MultiFAStateGroup;
import be.uclouvain.jail.algo.graph.copy.match.GMatchAggregator;
import be.uclouvain.jail.algo.utils.AbstractAlgoResult;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.INFA;
import be.uclouvain.jail.fa.functions.FAStateKindFunction;
import be.uclouvain.jail.fa.impl.AttributeGraphFAInformer;
import be.uclouvain.jail.fa.impl.GraphDFA;
import be.uclouvain.jail.fa.impl.GraphNFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.adjacency.AdjacencyDirectedGraph;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.UserInfoAggregator;

/**
 * Default implementation of IFAComposerResult.
 * 
 * @author blambeau
 */
public class DefaultFAComposerResult extends AbstractAlgoResult implements IFAComposerResult {

	/** Graph reached. */
	private IDirectedGraph g;
	
	/** Vertices. */
	private Map<MultiFAStateGroup, Object> vertices;
	
	/** Creates a result instance. */
	public DefaultFAComposerResult() {
		UserInfoAggregator stateAggregator = getUserInfoHandler().getVertexAggregator();
		stateAggregator.boolAnd(AttributeGraphFAInformer.STATE_INITIAL_KEY);
		stateAggregator.stateKind(AttributeGraphFAInformer.STATE_KIND_KEY,
				FAStateKindFunction.AND, FAStateKindFunction.OR, false);
		
		UserInfoAggregator edgeAggregator = getUserInfoHandler().getEdgeAggregator();
		edgeAggregator.allsame(AttributeGraphFAInformer.EDGE_LETTER_KEY);
	}
	
	/** Installs the options. */
	@Override
	protected void installOptions() {
		super.installOptions();
		super.addOption("state", "stateAggregator", false, GMatchAggregator.class, null);
		super.addOption("edge", "edgeAggregator", false, GMatchAggregator.class, null);
	}

	/** Adds a gmatch state populator. */
	public void setStateAggregator(GMatchAggregator populator) {
		getUserInfoHandler().getVertexAggregator().addPopulator(populator);
	}
	
	/** Adds a gmatch edge populator. */
	public void setEdgeAggregator(GMatchAggregator populator) {
		getUserInfoHandler().getEdgeAggregator().addPopulator(populator);
	}

	/** Fired when algorithm starts. */
	public void started(IFAComposerInput input) {
		this.g = new AdjacencyDirectedGraph();
		this.vertices = new HashMap<MultiFAStateGroup, Object>();
	}
	
	/** Fired when algorithm ends. */
	public void ended() {
		this.vertices = null;
	}
	
	/** Fired when a new state is found. */
	public void ensure(MultiFAStateGroup state) {
		if (vertices.containsKey(state)) return;
		IUserInfo info = super.getUserInfoHandler().vertexAggregate(state.getUserInfos());
		Object vertex = g.createVertex(info);
		vertices.put(state, vertex);
	}

	/** Fired when a state is reached. */
	public void stateReached(MultiFAStateGroup source, MultiFAEdgeGroup edge, MultiFAStateGroup target) {
		IUserInfo info = super.getUserInfoHandler().edgeAggregate(edge.getUserInfos());
		ensure(source); ensure(target);
		g.createEdge(vertices.get(source), vertices.get(target), info);
	}

	/** Adapts to another type. */
	@Override
	public <T> Object adapt(Class<T> c) {
		if (c.isInstance(this)) { return this; }
		else if (IDirectedGraph.class.equals(c)) {
			return g;
		} else if (IDFA.class.equals(c)) {
			return new GraphDFA(g);
		} else if (INFA.class.equals(c)) {
			return new GraphNFA(g);
		} else {
			return super.adapt(c);
		}
	}
	
}
