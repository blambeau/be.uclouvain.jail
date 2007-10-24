package be.uclouvain.jail.graph.utils;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import be.uclouvain.jail.adapt.AdaptUtils;
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
	
	/** Edges of the path. */
	private List<Object> edges;
	
	/** Creates a path from a graph and some edges. */
	public DefaultDirectedGraphPath(IDirectedGraph graph, List<Object> edges) {
		this.graph = graph;
		this.edges = edges;
	}
	
	/** Returns the root vertex. */
	protected Object getRootVertex() {
		if (size() == 0) {
			return null;
		}
		Object firstEdge = edges.get(0);
		return graph.getEdgeSource(firstEdge);
	}
	
	/** Returns the graph from which the path is extracted. */
	public IDirectedGraph getGraph() {
		return graph;
	}
	
	/** Returns the size of the path, defined as the number of used edges in 
	 * the path. The number of visited vertices is equal to getPathSize()+1
	 * by definition. */
	public int size() {
		return edges.size();
	}

	/** Returns an iterator on path edges. */
	public Iterable<Object> edges() {
		return edges;
	}

	/** Returns an iterator on path edges. */ 
	public Iterator<Object> iterator() {
		return edges.iterator();
	}
	
	/** Returns an iterator on path vertices. */
	public Iterable<Object> vertices() {
		if (size() == 0) {
			return Collections.emptyList();
		}
		return new Iterable<Object>() {
			public Iterator<Object> iterator() {
				return new Iterator<Object>() {

					/** Next edge to visit. */
					private Iterator<Object> it = null;
					
					/** Returns true if some vertices has to be iterated. */
					public boolean hasNext() {
						return (it == null) || it.hasNext();
					}

					/** Returns the next vertex to iterate. */
					public Object next() {
						Object toReturn = null;
						if (it == null) {
							// iterator just started
							toReturn = getRootVertex();
							it = edges.iterator();
						} else {
							// get next edge target
							Object edge = it.next();					

							toReturn = graph.getEdgeTarget(edge);
						}
						return toReturn;
					}

					/** Throws an UnsupportedOperationException. */
					public void remove() {
						throw new UnsupportedOperationException("Cannot remove from this iterator.");
					}
				};
			}
		};
	}

	/** Accepts a visitor. */
	public void accept(IVisitor visitor) {
		if (size() == 0) { return; }
		visitor.visit(null, getRootVertex());
		for (Object edge: edges) {
			Object target = graph.getEdgeTarget(edge);
			visitor.visit(edge, target);
		}
	}

	/** Flushes this path in a graph writer. */
	public void flush(IDirectedGraphWriter writer) {
		if (size()==0) { return; }

		// create vertices and save it
		Object[] vertices = new Object[size()+1];
		int i=0;
		for (Object vertex: vertices()) {
			vertices[i++] = writer.createVertex(graph.getVertexInfo(vertex));
		}
		
		// create edges
		i=0;
		for (Object edge: edges) {
			writer.createEdge(vertices[i], vertices[i+1], graph.getEdgeInfo(edge));
			i++;
		}
	}
	
	/** Adapts to another type. */
	public <T> Object adapt(Class<T> c) {
		return AdaptUtils.externalAdapt(this,c);
	}

}
