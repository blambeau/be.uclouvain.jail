package be.uclouvain.jail.algo.fa.utils;

import java.util.Collection;

import be.uclouvain.jail.algo.graph.utils.GraphMemberGroup;
import be.uclouvain.jail.algo.graph.utils.GraphMemberGroupDecorator;
import be.uclouvain.jail.algo.graph.utils.IGraphMemberGroup;
import be.uclouvain.jail.fa.IFA;
import be.uclouvain.jail.graph.IDirectedGraph;

/** 
 * Provides an implementation of edges group. 
 * 
 * @author blambeau
 */
public class FAEdgeGroup extends GraphMemberGroupDecorator {

	/** Finite automaton. */
	private IFA fa;
	
	/** Creates an empty group. */
	public FAEdgeGroup(IFA fa) {
		this(fa,new GraphMemberGroup(fa.getGraph(),fa.getGraph().getEdgesTotalOrder()));
	}
	
	/** Creates an empty group instance. */
	public FAEdgeGroup(IFA fa, IGraphMemberGroup group) {
		super(group);
		this.fa = fa;
	}
	
	/** Adds an edge in the group. */
	public void addEdge(Object edge) {
		group.addMember(edge);
	}
	
	/** Adds some edges in the group. */
	public void addEdges(Object...edges) {
		group.addMembers(edges);
	}
	
	/** Adds some edges in the group. */
	public void addEdges(Collection<Object> edges) {
		group.addMembers(edges);
	}
	
	/** Creates a state group with sources of the edges. */
	public FAStateGroup getSourceStateGroup() {
		IDirectedGraph graph = fa.getGraph();
		FAStateGroup states = new FAStateGroup(fa);
		for (Object edge: this) {
			states.addMember(graph.getEdgeSource(edge));
		}
		return states;
	}
	
	/** Creates a state group with targets of the edges. */
	public FAStateGroup getTargetStateGroup() {
		IDirectedGraph graph = fa.getGraph();
		FAStateGroup states = new FAStateGroup(fa);
		for (Object edge: this) {
			states.addMember(graph.getEdgeTarget(edge));
		}
		return states;
	}
	
}
