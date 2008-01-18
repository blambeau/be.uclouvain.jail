package be.uclouvain.jail.fa.utils;

import java.util.Iterator;

import be.uclouvain.jail.fa.IAlphabet;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.IFATrace;
import be.uclouvain.jail.fa.IString;
import be.uclouvain.jail.fa.IWalkInfo;
import be.uclouvain.jail.graph.IDirectedGraphWriter;

/** Decorates a trace as a string. */
public class FATraceString<L> implements IString<L> {

	/** Decorated trace. */
	private IFATrace<L> trace;
	
	/** Creates a string. */
	@SuppressWarnings("unchecked")
	public FATraceString(IFATrace<L> trace) {
		if (trace == null) { 
			throw new IllegalArgumentException("Trace cannot be null"); 
		}
		this.trace = trace;
	}
	
	/** Returns the alphabet. */
	public IAlphabet<L> getAlphabet() {
		return trace.getFA().getAlphabet();
	}

	/** Returns size of the string. */
	public int size() {
		return trace.size();
	}

	/** Returns an iterator on letters. */
	public Iterator<L> iterator() {
		return trace.iterator();
	}
	
	/** Returns true if the string is negative. */
	public boolean isPositive() {
		return trace.isAccepted();
	}

	/** Returns true if the string is negative. */
	public boolean isNegative() {
		return !isPositive();
	}

	/** Compares with another string. */
	public int compareTo(IString<L> other) {
		return getAlphabet().getStringComparator().compare(this, other);
	}

	/** Compares with another string. */
	@SuppressWarnings("unchecked")
	public int compareTo(Object who) {
		if (who == this) { return 0; }
		if (who instanceof IString == false) { return 1; }
		try { return compareTo((IString<L>)who); } 
		catch (ClassCastException ex) { return 1; }
	}

	/** Walks a DFA. */
	public IWalkInfo<L> walk(IDFA fa) {
		return trace.walk(fa);
	}

	/** Creates a substring. */
	public IString<L> subString(int start, int length) {
		return new FATraceString<L>(trace.subTrace(start,length));
	}

	/** Flushes in a writer and return the equivalent trace. */
	public IFATrace<L> flush(IDirectedGraphWriter writer) {
		return trace.flush(writer);
	}

	/** Returns a string representation. */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(isPositive() ? "+ " : "- ");
		int i =0;
		for (L letter: this) {
			if (i++ != 0) { sb.append(" "); }
			sb.append(letter);
		}
		return sb.toString();
	}
	
	/** Provide adaptations. */
	public <T> Object adapt(Class<T> c) {
		if (c.isAssignableFrom(getClass())) {
			return this;
		}
		
		// my adaptations
		if (IFATrace.class.equals(c)) {
			return trace;
		}
		
		return trace.adapt(c);
	}

}