package be.uclouvain.jail.fa.impl;

import be.uclouvain.jail.adapt.AdaptUtils;
import be.uclouvain.jail.adapt.IAdaptable;
import be.uclouvain.jail.adapt.IAdapter;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.uinfo.IUserInfo;

/**
 * Common implementations of GraphDFA and GraphFA.
 * 
 * @author blambeau
 */
public abstract class GraphFA implements IAdaptable {

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
	public GraphFA(IDirectedGraph graph, IGraphFAInformer informer) {
		this.graph = graph;
		this.informer = informer;
		this.graph.addAdaptation(IGraphFAInformer.class, new IAdapter() {
			/** Adapts the graph to the informer. */
			public Object adapt(Object who, Class type) {
				return GraphFA.this.informer;
			}
		});
	}
	
	/** Returns underlying graph. */
	public IDirectedGraph getGraph() {
		return graph;
	}
	
	/** Extracts the edge letter from a UserInfo */
	public Object edgeLetter(IUserInfo s) {
		return informer.edgeLetter(s);
	}

	/** Extracts the accepting flag from a UserInfo */
	public boolean isAccepting(IUserInfo s) {
		return informer.isAccepting(s);
	}

	/** Extracts the error flag from a UserInfo */
	public boolean isError(IUserInfo s) {
		return informer.isError(s);
	}

	/** Extracts the initial flag from a UserInfo */
	public boolean isInitial(IUserInfo s) {
		return informer.isInitial(s);
	}

	/** Checks if a state is the initial state. */
	public boolean isInitial(Object s) {
		return isInitial(graph.getVertexInfo(s));
	}
	
	/** Checks if a state is marked as accepting. */
	public boolean isAccepting(Object s) {
		return isAccepting(graph.getVertexInfo(s));
	}
	
	/** Checks if a state is marked as error. */
	public boolean isError(Object s) {
		return isError(graph.getVertexInfo(s));
	}
	
	/** Returns the letter attached to an edge. */
	public Object getEdgeLetter(Object edge) {
		return edgeLetter(graph.getEdgeInfo(edge));
	}
	
	/** Adapts. */
	public <T> Object adapt(Class<T> c) {
		if (IDirectedGraph.class.equals(c)) {
			return graph;
		}
		return AdaptUtils.externalAdapt(this,c);
	}

}
