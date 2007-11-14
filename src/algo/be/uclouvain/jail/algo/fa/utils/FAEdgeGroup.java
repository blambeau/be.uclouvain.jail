package be.uclouvain.jail.algo.fa.utils;

import java.util.Collection;

import be.uclouvain.jail.fa.IFA;
import be.uclouvain.jail.graph.IDirectedGraph;

/** 
 * Provides an implementation of edges group. 
 * 
 * @author blambeau
 */
public class FAEdgeGroup extends FAAbstractGroup {

	/** Creates an empty group instance. */
	public FAEdgeGroup(IFA fa) {
		super(fa,fa.getGraph().getEdgesTotalOrder());
	}
	
	/** Adds an edge in the group. */
	public void addEdge(Object edge) {
		super.addComponent(edge);
	}
	
	/** Adds some edges in the group. */
	public void addEdges(Object...edges) {
		super.addComponents(edges);
	}
	
	/** Adds some edges in the group. */
	public void addEdges(Collection<Object> edges) {
		super.addComponents(edges);
	}
	
	/** Creates a state group with sources of the edges. */
	public FAStateGroup getSourceStateGroup() {
		IDirectedGraph graph = fa.getGraph();
		FAStateGroup sources = new FAStateGroup(fa);
		for (Object edge: this) {
			sources.addComponent(graph.getEdgeSource(edge));
		}
		return sources;
	}
	
	/** Creates a state group with targets of the edges. */
	public FAStateGroup getTargetStateGroup() {
		IDirectedGraph graph = fa.getGraph();
		FAStateGroup sources = new FAStateGroup(fa);
		for (Object edge: this) {
			sources.addComponent(graph.getEdgeTarget(edge));
		}
		return sources;
	}
	
}
