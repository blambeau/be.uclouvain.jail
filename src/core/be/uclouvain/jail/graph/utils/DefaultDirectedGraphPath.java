package be.uclouvain.jail.graph.utils;

import java.util.ArrayList;
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

	/** Appends an edge in the trace. */
	public void append(Object edge) {
		addEdge(edge);
	}
	
	/** Appends with another path. */
	public IDirectedGraphPath append(IDirectedGraphPath path) {
		List<Object> newEdges = new ArrayList<Object>();
		
		// copy my edges
		for (Object edge: edges()) {
			newEdges.add(edge);
		}
		
		// copy other's edge
		for (Object edge: path.edges()) {
			newEdges.add(edge);
		}
		
		if (newEdges.isEmpty()) {
			return new DefaultDirectedGraphPath(graph, getRootVertex());
		} else {
			return new DefaultDirectedGraphPath(graph, newEdges);
		}
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

	/** Creates a subtrace. */
	public IDirectedGraphPath subPath(int start, int length) {
		List<Object> edges = new ArrayList<Object>();
		for (int i=start; i<start+length; i++) {
			edges.add(this.edges.get(i));
		}
		return new DefaultDirectedGraphPath(graph,edges);
	}
	
	/** Flushes inside a writer and returns the corresponding path. */
	public IDirectedGraphPath flush(IDirectedGraphWriter writer) {
		// create vertices and save it
		Object[] vertices = new Object[size()+1];
		int i=0;
		for (Object vertex: vertices()) {
			vertices[i++] = writer.createVertex(graph.getVertexInfo(vertex));
		}
		
		// create edges
		i=0;
		List<Object> edgeCopy = new ArrayList<Object>();
		if (edges != null) {
			for (Object edge: edges) {
				Object e = writer.createEdge(vertices[i], vertices[i+1], graph.getEdgeInfo(edge));
				edgeCopy.add(e);
				i++;
			}
		}
		
		IDirectedGraph g = (IDirectedGraph) writer.adapt(IDirectedGraph.class);
		return (g == null) ? null : 
			   edgeCopy.isEmpty() ? new DefaultDirectedGraphPath(g,vertices[0]) :
			   new DefaultDirectedGraphPath(g,edgeCopy);
	}
	
	/** Adapts to another type. */
	public <T> Object adapt(Class<T> c) {
		if (c.isAssignableFrom(this.getClass())) {
			return this;
		}
		return AdaptUtils.externalAdapt(this,c);
	}

}
