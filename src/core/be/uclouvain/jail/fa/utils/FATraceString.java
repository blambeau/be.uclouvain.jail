package be.uclouvain.jail.fa.utils;

import java.util.Iterator;

import be.uclouvain.jail.fa.IAlphabet;
import be.uclouvain.jail.fa.IFATrace;
import be.uclouvain.jail.fa.IFlushableString;
import be.uclouvain.jail.fa.IString;
import be.uclouvain.jail.graph.IDirectedGraphWriter;

/** Decorates a trace as a string. */
public class FATraceString<L> implements IFlushableString<L> {

	/** Decorated trace. */
	private FATrace<L> trace;
	
	/** Creates a string. */
	@SuppressWarnings("unchecked")
	public FATraceString(IFATrace<L> trace) {
		this.trace = (FATrace<L>) trace.adapt(FATrace.class);
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
		int c = getAlphabet().getWordComparator().compare(this, other);

		// if not equal let return c
		if (c != 0) { return c; }
		
		// otherwise, positive strings are greater
		// than negative ones
		return new Boolean(isPositive()).compareTo(other.isPositive());
	}

	/** Compares with another string. */
	@SuppressWarnings("unchecked")
	public int compareTo(Object who) {
		if (who == this) { return 0; }
		if (who instanceof IString == false) { return 1; }
		try {
			return compareTo((IString<L>)who);
		} catch (ClassCastException ex) {
			return 1;
		}
	}

	/** Fills the string inside a graph. */
	public Object[] fill(IDirectedGraphWriter g) {
		return trace.flush(g);
	}

	/** Provide adaptations. */
	public <T> Object adapt(Class<T> c) {
		if (c.isAssignableFrom(getClass())) {
			return this;
		}
		return trace.adapt(c);
	}

}