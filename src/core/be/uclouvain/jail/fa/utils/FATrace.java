package be.uclouvain.jail.fa.utils;

import java.util.Iterator;

import be.uclouvain.jail.fa.FAStateKind;
import be.uclouvain.jail.fa.IAlphabet;
import be.uclouvain.jail.fa.IFA;
import be.uclouvain.jail.fa.IFATrace;
import be.uclouvain.jail.fa.IWord;
import be.uclouvain.jail.graph.IDirectedGraphPath;
import be.uclouvain.jail.graph.IDirectedGraphWriter;
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
public class FATrace<T> implements IFATrace<T> {

	/** Decorated trace. */
	private IFATrace trace;
	
	/** Creates a decorator instance. */
	public FATrace(IFATrace trace) {
		this.trace = trace;
	}
	
	/** Creates a decorator from fa and path. */
	public FATrace(IFA fa, IDirectedGraphPath path) {
		this(new DefaultFATrace(fa,path));
	}
	
	/** Returns the dfa from which this trace has been extracted. */
	public IFA getFA() {
		return trace.getFA();
	}

	/** Returns the underlying graph path. */
	public IDirectedGraphPath getGraphPath() {
		return trace.getGraphPath();
	}
	
	/** Returns size of the trace. */
	public int size() {
		return getGraphPath().size();
	}

	/** Returns an iterator on trace letters. */
	@SuppressWarnings("unchecked")
	public Iterator<T> iterator() {
		return trace.iterator();
	}

	/** Converts the trace to a word. */
	public IWord<T> getWord() {
		IAlphabet<T> alphabet = trace.getFA().getAlphabet();
		return alphabet.word(this);
	}
	
	/** Returns the last state of this trace. */
	public Object getLastState() {
		DirectedGraphPath path = (DirectedGraphPath) getGraphPath().adapt(DirectedGraphPath.class);
		return path.getLastVertex();
	}

	/** Returns true if this trace is accepted by the DFA. Acceptation is
	 * defined as the fact that the trace ends in an accepting state that 
	 * is not marked as error. */
	public boolean isAccepted() {
		IFA dfa = trace.getFA();
		Object endState = getLastState();
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
		IFA dfa = trace.getFA();
		Object endState = getLastState();
		return FAStateKind.ERROR.equals(dfa.getStateKind(endState));
	}
	
	/** Returns true if this trace reachs a state that is marked as an error
	 * one. */
	public boolean isAvoid() {
		IFA dfa = trace.getFA();
		Object endState = getLastState();
		return FAStateKind.AVOID.equals(dfa.getStateKind(endState));
	}
	
	/** Flushes the trace in a writer. */
	public Object[] flush(IDirectedGraphWriter writer) {
		return getGraphPath().flush(writer);
	}
	
	/** Adapts this trace to some type. */
	public <S> Object adapt(Class<S> c) {
		if (c.isAssignableFrom(this.getClass())) {
			return this;
		}
		
		// adapt to a word
		if (IWord.class.equals(c)) {
			return getWord();
		}
		
		return trace.adapt(c);
	}
	
}
