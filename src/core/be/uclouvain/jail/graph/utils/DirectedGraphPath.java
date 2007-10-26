package be.uclouvain.jail.graph.utils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.IDirectedGraphPath;
import be.uclouvain.jail.graph.IDirectedGraphWriter;

/**
 * Provides a decorator of IDirectedGraphPath with a query API.
 * 
 * @author blambeau
 */
public class DirectedGraphPath implements IDirectedGraphPath {

	/** Decorated path. */
	private IDirectedGraphPath path;

	/** Creates a decorator instance. */
	public DirectedGraphPath(IDirectedGraphPath path) {
		this.path = path;
	}
	
	/** Returns the graph from which the path is extracted. */
	public IDirectedGraph getGraph() {
		return path.getGraph();
	}
	
	/** Returns the size of the path, defined as the number of used edges in 
	 * the path. The number of visited vertices is equal to getPathSize()+1
	 * by definition. */
	public int size() {
		return path.size();
	}

	/** Returns an iteraor on path edges. */
	public Iterator<Object> iterator() {
		return path.iterator();
	}
	
	/** Returns an iterator on path edges. */
	public List<Object> edges() {
		return path.edges();
	}

	/** Returns an iterator on path vertices. */
	public List<Object> vertices() {
		return path.vertices();
	}

	/** Accepts a visitor. */
	public void accept(IVisitor visitor) {
		path.accept(visitor);
	}
	
	/** Flushes this path in a graph writer. */
	public void flush(IDirectedGraphWriter writer) {
		path.flush(writer);
	}

	/** Adapts this path to another type. */
	public <T> Object adapt(Class<T> c) {
		if (c.isAssignableFrom(this.getClass())) {
			return this;
		}
		return path.adapt(c);
	}

	/** Returns true if the path is empty. */
	public boolean isEmpty() {
		return size() == 0;
	}
	
	/** Returns the end vertex of the path. */
	public Object getEndVertex() {
		if (size()==0) { return null; }
		List<Object> vertices = vertices();
		if (vertices instanceof LinkedList) {
			return ((LinkedList)vertices).getLast();
		} else {
			return vertices.get(vertices.size()-1);
		}
	}
	
}
