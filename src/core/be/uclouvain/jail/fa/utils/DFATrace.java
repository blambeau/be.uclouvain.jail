package be.uclouvain.jail.fa.utils;

import java.util.Iterator;

import be.uclouvain.jail.fa.FAStateKind;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.IDFATrace;
import be.uclouvain.jail.graph.IDirectedGraphPath;
import be.uclouvain.jail.graph.utils.DirectedGraphPath;

/**
 * Provides a decorator of DFA traces with a query API.
 * 
 * <p>This decorator provides many useful query method to know if the trace
 * ends in an error state, an accepting state, etc.</p> 
 * 
 * @author blambeau
 * @param <T> type of trace letters.
 */
public class DFATrace<T> implements IDFATrace<T> {

	/** Decorated trace. */
	private IDFATrace trace;
	
	/** Creates a decorator instance. */
	public DFATrace(IDFATrace trace) {
		this.trace = trace;
	}
	
	/** Returns the dfa from which this trace has been extracted. */
	public IDFA getDFA() {
		return trace.getDFA();
	}

	/** Returns the underlying graph path. */
	public IDirectedGraphPath getGraphPath() {
		return trace.getGraphPath();
	}

	/** Returns an iterator on trace letters. */
	@SuppressWarnings("unchecked")
	public Iterator<T> iterator() {
		return trace.iterator();
	}

	/** Returns the last state of this trace. */
	public Object getEndState() {
		DirectedGraphPath path = (DirectedGraphPath) getGraphPath().adapt(DirectedGraphPath.class);
		return path.getEndVertex();
	}

	/** Returns true if this trace is accepted by the DFA. Acceptation is
	 * defined as the fact that the trace ends in an accepting state that 
	 * is not marked as error. */
	public boolean isAccepted() {
		IDFA dfa = trace.getDFA();
		Object endState = getEndState();
		return FAStateKind.ACCEPTING.equals(dfa.getStateKind(endState));
	}

	/** Returns true if this trace is rejected by the DFA. Rejection is
	 * defined as the fact that the trace ends in an non accepting state 
	 * or a state that is marked as error. In other words, rejection is
	 * the boolean negation of acceptation. */
	public boolean isRejected() {
		return !isAccepted();
	}
	
	/** Returns true if this trace reachs a state that is marked as an error
	 * one. */
	public boolean isError() {
		IDFA dfa = trace.getDFA();
		Object endState = getEndState();
		return FAStateKind.ERROR.equals(dfa.getStateKind(endState));
	}
	
	/** Returns true if this trace reachs a state that is marked as an error
	 * one. */
	public boolean isAvoid() {
		IDFA dfa = trace.getDFA();
		Object endState = getEndState();
		return FAStateKind.AVOID.equals(dfa.getStateKind(endState));
	}
	
	/** Adapts this trace to some type. */
	public <S> Object adapt(Class<S> c) {
		if (c.isAssignableFrom(this.getClass())) {
			return this;
		}
		return trace.adapt(c);
	}
	
}
