package be.uclouvain.jail.algo.induct.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import be.uclouvain.jail.algo.induct.internal.PTAEdge;
import be.uclouvain.jail.algo.induct.internal.PTAState;
import be.uclouvain.jail.algo.induct.open.ISuffixExtractor;
import be.uclouvain.jail.algo.induct.open.IWalker;
import be.uclouvain.jail.fa.IDFATrace;

/**
 * Extracts long suffixes.
 * 
 * @author blambeau
 * @param <T>
 */
public class LongSuffixExtractor implements ISuffixExtractor, IWalker {

	/** Traces. */
	private List<IDFATrace> traces;
	
	/** Walk stack. */
	private Stack<PTAEdge> stack;
	
	/** Extracts suffixes of the state. */
	public IDFATrace[] extract(PTAState state) {
		traces = new ArrayList<IDFATrace>();
		stack = new Stack<PTAEdge>();

		// walk the state
		state.accept(this);
		
		// return traces
		IDFATrace[] result = new IDFATrace[traces.size()];
		return traces.toArray(result);
	}

	private void flush() {
		
	}
	
	/** On left push the edge. */
	public boolean walksLeftOf(PTAEdge edge, PTAState state) {
		if (edge != null) { stack.add(edge); }
		return true;
	}

	/** On right. */
	public boolean walksRightOf(PTAEdge edge, PTAState state, boolean flag) {
		if (state.letters().isEmpty()) { flush(); }
		return false;
	}

}
