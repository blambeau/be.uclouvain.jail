package be.uclouvain.jail.algo.graph.utils;

import java.util.Collection;

import be.uclouvain.jail.graph.IDirectedGraph;

/** 
 * Provides a decorator on graph group for cases where the groups
 * contains edges.  
 * 
 * @author blambeau
 */
public class GraphEdgeGroup extends GraphMemberGroupDecorator {

	/** Creates an empty group instance. */
	public GraphEdgeGroup(IGraphMemberGroup group) {
		super(group);
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
	
	/** Creates a vertex group with sources of the edges. */
	public GraphVertexGroup getSourceVertexGroup() {
		IDirectedGraph graph = getGraph();
		IGraphMemberGroup vertices = new GraphMemberGroup(graph,graph.getVerticesTotalOrder());
		for (Object edge: this) {
			vertices.addMember(graph.getEdgeSource(edge));
		}
		return new GraphVertexGroup(vertices);
	}
	
	/** Creates a vertex group with targets of the edges. */
	public GraphVertexGroup getTargetVertexGroup() {
		IDirectedGraph graph = getGraph();
		IGraphMemberGroup vertices = new GraphMemberGroup(graph,graph.getVerticesTotalOrder());
		for (Object edge: this) {
			vertices.addMember(graph.getEdgeTarget(edge));
		}
		return new GraphVertexGroup(vertices);
	}
	
}
