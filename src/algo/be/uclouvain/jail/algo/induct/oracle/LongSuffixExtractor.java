package be.uclouvain.jail.algo.induct.oracle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import be.uclouvain.jail.algo.induct.internal.PTAEdge;
import be.uclouvain.jail.algo.induct.internal.PTAState;
import be.uclouvain.jail.algo.induct.utils.IWalker;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.IFATrace;
import be.uclouvain.jail.fa.utils.DefaultFATrace;
import be.uclouvain.jail.graph.utils.DefaultDirectedGraphPath;

/**
 * Extracts long suffixes.
 * 
 * @author blambeau
 * @param <T>
 */
public class LongSuffixExtractor implements ISuffixExtractor, IWalker {

	/** Requesting root. */
	private PTAState state;
	
	/** Traces. */
	private List traces;
	
	/** Walk stack. */
	private Stack<PTAEdge> stack;
	
	/** Extracts suffixes of the state. */
	@SuppressWarnings("unchecked")
	public <T> Iterator<IFATrace<T>> extract(PTAState state) {
		this.state = state;
		traces = new ArrayList<IFATrace<T>>();
		stack = new Stack<PTAEdge>();

		// walk the state
		state.accept(this);
		
		// return traces
		return traces.iterator();
	}

	/** Flushes a suffix. */
	@SuppressWarnings("unchecked")
	private void flush() {
		IDFA pta = state.getRunningAlgo().getPTA();
		
		// creates an empty path on the state
		DefaultDirectedGraphPath path = factorPath(pta,state);
		
		// add edges
		for (PTAEdge edge: stack) {
			Object ptaEdge = edge.representor();
			path.addEdge(ptaEdge);
		}
		
		// create a trace and add it to traces
		IFATrace trace = new DefaultFATrace(pta,path);
		traces.add(trace);
	}
	
	/** Factors a path. */
	protected DefaultDirectedGraphPath factorPath(IDFA pta, PTAState root) {
		return new DefaultDirectedGraphPath(pta.getGraph(),root.representor());
	}
	
	/** On left push the edge. */
	public boolean walksLeftOf(PTAEdge edge, PTAState state) {
		if (edge != null) { stack.push(edge); }
		return true;
	}

	/** On right. */
	public boolean walksRightOf(PTAEdge edge, PTAState state, boolean flag) {
		if (state.letters().isEmpty()) { flush(); }
		if (!stack.isEmpty()) { stack.pop(); }
		return false;
	}

}
