package be.uclouvain.jail.fa.impl;

import net.chefbe.javautils.adapt.AdaptUtils;
import net.chefbe.javautils.adapt.IAdapter;
import be.uclouvain.jail.fa.FAStateKind;
import be.uclouvain.jail.fa.IAlphabet;
import be.uclouvain.jail.fa.IFA;
import be.uclouvain.jail.fa.constraints.FAGraphConstraint;
import be.uclouvain.jail.fa.utils.AutoAlphabet;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.uinfo.IUserInfo;

/**
 * Super class of GraphDFA and GraphNFA.
 * 
 * <p>Please read documentation of these two classes in order to get important
 * information about contributors of this class.</p> 
 * 
 * @author blambeau
 */
public abstract class GraphFA implements IFA {

	/** Automaton alphabet. */
	protected IAlphabet alphabet;
	
	/** Decorated graph. */
	protected IDirectedGraph graph;
	
	/** Decoration informer. */
	protected IGraphFAInformer informer;
	
	/** 
	 * Creates a FA instance. 
	 * 
	 * <p>This constructor can be used to decorate any graph as an automaton using
	 * the informer provided.</p>
	 */
	public GraphFA(IDirectedGraph graph, IGraphFAInformer informer, IAlphabet alphabet) {
		assert (new FAGraphConstraint(informer).isRespectedBy(graph)) : "Valid underlying graph.";
		this.graph = graph;
		this.informer = informer;
		this.alphabet = alphabet;
		this.graph.addAdaptation(IGraphFAInformer.class, new IAdapter() {
			/** Adapts the graph to the informer. */
			public Object adapt(Object who, Class type) {
				return GraphFA.this.informer;
			}
		});
	}

	/** 
	 * Creates a FA instance. 
	 * 
	 * <p>This constructor can be used to decorate any graph as an automaton using
	 * the informer provided.</p>
	 */
	public GraphFA(IDirectedGraph graph, IGraphFAInformer informer) {
		this(graph,informer,null);
	}
	
	/** Returns the automaton alphabet. */
	@SuppressWarnings("unchecked")
	public <T> IAlphabet<T> getAlphabet() {
		if (alphabet == null) {
			alphabet = AutoAlphabet.inferAlphabet(this,null);
		}
		return alphabet;
	}
	
	/** Returns underlying graph. */
	public IDirectedGraph getGraph() {
		return graph;
	}
	
	/** Returns edges of the automaton. */
	public Iterable<Object> getEdges() {
		return graph.getEdges();
	}

	/** Returns states of the automaton. */
	public Iterable<Object> getStates() {
		return graph.getVertices();
	}

	/** Extracts the initial flag from a UserInfo */
	public boolean isInitial(IUserInfo s) {
		return informer.isInitial(s);
	}

	/** Checks if a state is the initial state. */
	public boolean isInitial(Object s) {
		return isInitial(graph.getVertexInfo(s));
	}
	
	/** Returns state kind (accepting/error). */
	public FAStateKind getStateKind(IUserInfo s) {
		return informer.getStateKind(s);
	}
	
	/** Returns state kind (accepting/error). */
	public FAStateKind getStateKind(Object s) {
		return getStateKind(graph.getVertexInfo(s));
	}
	
	/** Extracts the edge letter from a UserInfo */
	public Object getEdgeLetter(IUserInfo s) {
		return informer.edgeLetter(s);
	}

	/** Returns the letter attached to an edge. */
	public Object getEdgeLetter(Object edge) {
		return getEdgeLetter(graph.getEdgeInfo(edge));
	}
	
	/** Adapts. */
	public <T> Object adapt(Class<T> c) {
		if (c.isAssignableFrom(this.getClass())) {
			return this;
		}
		if (IDirectedGraph.class.equals(c)) {
			return graph;
		}
		return AdaptUtils.externalAdapt(this,c);
	}

}
