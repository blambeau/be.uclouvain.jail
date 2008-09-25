package be.uclouvain.jail.algo.fa.compose;

import java.util.HashMap;
import java.util.Map;

import be.uclouvain.jail.algo.commons.Avoid;
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

	/** Explored flag. */
	private static final String EXPLORED = "DefaultFAComposerResult.EXPLORED";
	
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

	/** Returns state aggregator. */
	public UserInfoAggregator getStateAggregator() {
		return getUserInfoHandler().getVertexAggregator();
	}
	
	/** Returns edge aggregator. */
	public UserInfoAggregator getEdgeAggregator() {
		return getUserInfoHandler().getEdgeAggregator();
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
	public Object ensure(MultiFAStateGroup state) {
		Object vertex = vertices.get(state);
		IUserInfo info = null;

		// create vertex if required
		if (vertex != null) { 
			info = g.getVertexInfo(vertex); 
		} else {
			info = super.getUserInfoHandler().vertexAggregate(state.getUserInfos());
			info.setAttribute(EXPLORED, false);
			vertex = g.createVertex(info);
			vertices.put(state, vertex);
		}
		
		return vertex;
	}

	/** Returns true if this state has already been explored by 
	 * the algorithm, false otherwise. */
	public boolean isExplored(MultiFAStateGroup source) {
		Object vertex = vertices.get(source);
		if (vertex == null) { return false; }
		Boolean explored = (Boolean) g.getVertexInfo(vertex).getAttribute(EXPLORED);
		return explored == null ? false : explored.booleanValue();
	}

	/** Fired when a state is explored for the first time. */
	public void exploring(MultiFAStateGroup source) {
		Object vertex = ensure(source);
		g.getVertexInfo(vertex).setAttribute(EXPLORED, true);
	}

	/** 
	 * Fired when the target state is reached from source through an edge. 
	 * 
	 * <p>Source state has always been explored (exploring method has been previously called).
	 * Target state may be already explored but not necesserally.</p>  
	 * 
	 * <p>This method may throw an Avoid exception to let the algorithm know that the
	 * target state is in fact unreachable due to edge specific informations.</p>
	 * 
	 * <p>This method is expected to return !isExplored(target), that is, a boolean indicating
	 * if the target state must be further explored or not.</p> 
	 */
	public boolean reached(MultiFAStateGroup source, MultiFAEdgeGroup edge, MultiFAStateGroup target) throws Avoid {
		// aggregate edge (Avoid may be thrown by user functions)
		IUserInfo info = super.getUserInfoHandler().edgeAggregate(edge.getUserInfos());
		
		// ensure source and target states
		Object s = ensure(source); 
		Object t = ensure(target);
		
		// create edge between them
		g.createEdge(s, t, info);
		
		// explore target if not done yet  
		return !isExplored(target);
	}

	/** 
	 * Fired when a state has been entirely explored.
	 */
	public void endexplore(MultiFAStateGroup source) {
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
