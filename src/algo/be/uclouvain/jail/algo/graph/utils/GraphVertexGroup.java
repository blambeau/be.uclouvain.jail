package be.uclouvain.jail.algo.graph.utils;

import java.util.Collection;

import be.uclouvain.jail.graph.IDirectedGraph;

/** 
 * Provides a decorator on graph group for cases where the groups
 * contains vertices.  
 * 
 * @author blambeau
 */
public class GraphVertexGroup extends GraphMemberGroupDecorator {

	/** Creates an empty group instance. */
	public GraphVertexGroup(IGraphMemberGroup group) {
		super(group);
	}

	/** Adds a vertex in the group. */
	public void addVertex(Object vertex) {
		group.addMember(vertex);
	}
	
	/** Adds some vertices in the group. */
	public void addVertices(Object...vertices) {
		group.addMembers(vertices);
	}
	
	/** Adds some vertices in the group. */
	public void addVertices(Collection<Object> vertices) {
		group.addMembers(vertices);
	}
	
	/** Returns all outgoing edges. */
	public GraphEdgeGroup getOutgoingEdges() {
		IDirectedGraph graph = getGraph();
		IGraphMemberGroup edges = new GraphMemberGroup(graph,graph.getEdgesTotalOrder());
		for (Object vertex: this) {
			edges.addMembers(graph.getOutgoingEdges(vertex));
		}
		return new GraphEdgeGroup(edges);
	}
	
	/** Returns all incoming edges. */
	public GraphEdgeGroup getIncomingEdges() {
		IDirectedGraph graph = getGraph();
		IGraphMemberGroup edges = new GraphMemberGroup(graph,graph.getEdgesTotalOrder());
		for (Object vertex: this) {
			edges.addMembers(graph.getIncomingEdges(vertex));
		}
		return new GraphEdgeGroup(edges);
	}
	
}
