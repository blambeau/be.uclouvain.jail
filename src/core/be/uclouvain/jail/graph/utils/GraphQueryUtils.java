package be.uclouvain.jail.graph.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.IDirectedGraphPath;
import be.uclouvain.jail.uinfo.functions.IAggregateFunction;

/**
 * Provides many utilities to query graphs and graph components.
 * 
 * @author blambeau
 */
public class GraphQueryUtils {

	/** Returns the first vertex of a graph path. */
	public static Object getFirstVertex(IDirectedGraphPath path) {
		return path.size() == 0 ? null : path.vertices().iterator().next();
	}
	
	/** Returns the first vertex of a graph path. */
	public static Object getLastVertex(IDirectedGraphPath path) {
		Iterator i = path.vertices().iterator();
		Object current = null;
		while (i.hasNext()) {
			current = i.next();
		}
		return current;
	}

	/** Project an edge/vertex iterable on some attribute. */
	@SuppressWarnings("unchecked")
	public static <T> Iterable<T> project(IDirectedGraph graph, Iterable<Object> it, String attr) {
		List<T> values = new ArrayList<T>();
		for (Object i: it) {
			values.add((T)graph.getUserInfoOf(i).getAttribute(attr));
		}
		return values;
	}
	
	/** Computes some aggregate function on a path. */
	public static <T> T compute(IDirectedGraph graph, Iterable<Object> it, String attr, IAggregateFunction<T> f)  {
		Iterable<T> values = project(graph,it,attr);
		return f.compute(values);
	}
	
}
