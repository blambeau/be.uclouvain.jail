package be.uclouvain.jail.graph.utils;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.chefbe.javautils.adapt.AdaptUtils;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.IDirectedGraphPath;
import be.uclouvain.jail.graph.IDirectedGraphWriter;

/**
 * Provides an implementation of {@link IDirectedGraphPath}.
 * 
 * @author blambeau
 */
public class DefaultDirectedGraphPath implements IDirectedGraphPath {

	/** Graph from which path has been extracted. */
	private IDirectedGraph graph;
	
	/** Root of the path. */
	private Object root;
	
	/** Edges of the path. */
	private List<Object> edges;
	
	/** Vertices of the path. */
	private List<Object> vertices;
	
	/** Creates a path from a graph and some edges. */
	public DefaultDirectedGraphPath(IDirectedGraph graph, List<Object> edges) {
		if (edges.isEmpty()) { 
			throw new IllegalArgumentException("Edge list cannot be empty, use other constructor."); 
		}
		this.graph = graph;
		this.edges = edges;
	}
	
	/** Creates an empty path from a root node. */
	public DefaultDirectedGraphPath(IDirectedGraph graph, Object root) {
		this.graph = graph;
		this.edges = null;
		this.root = root;
	}
	
	/** Returns the root vertex. */
	public Object getRootVertex() {
		if (root != null) { return root; }
		Object firstEdge = edges.get(0);
		root = graph.getEdgeSource(firstEdge);
		return root;
	}
	
	/** Returns the last vertex. */
	public Object getLastVertex() {
		if (edges == null) { return root; }
		Object edge = null;
		if (edges instanceof LinkedList) {
			edge = ((LinkedList)edges).getLast();
		} else {
			edge = edges.get(edges.size()-1);
		}
		return graph.getEdgeTarget(edge);
	}
	
	/** Adds an edge at end of the path. */
	public void addEdge(Object edge) {
		if (edges == null) { edges = new LinkedList<Object>(); }
		
		// add edge
		edges.add(edge);
		
		// if vertices has been created, add target vertex
		if (vertices != null) {
			Object target = graph.getEdgeTarget(edge);
			vertices.add(target);
		}
	}
	
	/** Returns the graph from which the path is extracted. */
	public IDirectedGraph getGraph() {
		return graph;
	}
	
	/** Returns the size of the path, defined as the number of used edges in 
	 * the path. The number of visited vertices is equal to getPathSize()+1
	 * by definition. */
	public int size() {
		return edges == null ? 0 : edges.size();
	}

	/** Returns an iterator on path edges. */
	public List<Object> edges() {
		return edges == null ? Collections.emptyList() : edges;
	}

	/** Returns an iterator on path edges. */ 
	public Iterator<Object> iterator() {
		return edges().iterator();
	}
	
	/** Returns an iterator on path vertices. */
	public List<Object> vertices() {
		if (vertices != null) { return vertices; }
		if (edges == null) {
			vertices = Collections.singletonList(root);
		} else {
			vertices = new LinkedList<Object>();
			vertices.add(getRootVertex());
			for (Object edge: edges) {
				vertices.add(graph.getEdgeTarget(edge));
			}
		}
		return vertices;
	}

	/** Accepts a visitor. */
	public void accept(IVisitor visitor) {
		visitor.visit(null, getRootVertex());
		if (edges != null) {
			for (Object edge: edges) {
				Object target = graph.getEdgeTarget(edge);
				visitor.visit(edge, target);
			}
		}
	}

	/** Flushes this path in a graph writer. */
	public Object[] flush(IDirectedGraphWriter writer) {
		// create vertices and save it
		Object[] vertices = new Object[size()+1];
		int i=0;
		for (Object vertex: vertices()) {
			vertices[i++] = writer.createVertex(graph.getVertexInfo(vertex));
		}
		
		// create edges
		i=0;
		if (edges != null) {
			for (Object edge: edges) {
				writer.createEdge(vertices[i], vertices[i+1], graph.getEdgeInfo(edge));
				i++;
			}
		}
		
		return vertices;
	}
	
	/** Adapts to another type. */
	public <T> Object adapt(Class<T> c) {
		if (c.isAssignableFrom(this.getClass())) {
			return this;
		}
		return AdaptUtils.externalAdapt(this,c);
	}

}
