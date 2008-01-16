package be.uclouvain.jail.fa.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Stack;

import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.IFATrace;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.utils.DefaultDirectedGraphPath;

/**
 * Depth-first visit of a PTA.
 * 
 * @author blambeau
 */
public class PTADepthFirst implements Iterator<Object> {

	/** PTA. */
	private IDFA pta;

	/** Underlying graph. */
	private IDirectedGraph graph;
	
	/** Root state. */
	private Object root;
	
	/** Edgs stack. */
	private Stack<Object> edges;

	/** Stack of iterators. */
	private Stack<Iterator<Object>> its;
	
	/** Creates a visit instance. */
	public PTADepthFirst(IDFA pta) {
		this.pta = pta;
		this.graph = pta.getGraph();
		this.edges = new Stack<Object>();
		this.its = new Stack<Iterator<Object>>();
	}
	
	/** Flushes the current state as a trace. */
	public <T> IFATrace<T> flushTrace() {
		// before first next
		if (root == null) {
			throw new IllegalStateException();
		}
		
		// creates an empty path from root
		DefaultDirectedGraphPath path = new DefaultDirectedGraphPath(graph,root);
		
		// add edges is any
		for (Object edge: edges) {
			path.addEdge(edge);
		}
		
		return new DefaultFATrace<T>(pta,path);
	}

	/** Has next state? */
	public boolean hasNext() {
		// not yet started?
		if (root == null) { return true; }
		
		// one iterator has next?
		for (Iterator<Object> it: its) {
			if (it.hasNext()) {
				return true;
			}
		}
		return false;
	}

	/** Returns the next state. */
	public Object next() {
		// starting case
		if (root == null) {
			root = pta.getInitialState();
			its.push(graph.getOutgoingEdges(root).iterator());
			return root;
		}
		
		// Find last non empty iterator
		Iterator<Object> lasti = its.peek();
		while (!lasti.hasNext()) { 
			its.pop();
			edges.pop();
			lasti = its.peek(); 
		}
		Object nextEdge = lasti.next();
		
		// push the edge
		edges.push(nextEdge);

		// get its target state (which is the one to return) 
		Object nextState = graph.getEdgeTarget(nextEdge);
		
		// if the next state has outgoing edges, push it
		Collection<Object> outEdges = graph.getOutgoingEdges(nextState);
		if (!outEdges.isEmpty()) {
			its.push(outEdges.iterator());
		}
		
		return nextState;
	}

	/** Throws an exception. */
	public void remove() {
		throw new UnsupportedOperationException();
	}
	
}
