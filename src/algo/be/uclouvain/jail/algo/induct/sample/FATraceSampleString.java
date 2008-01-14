package be.uclouvain.jail.algo.induct.sample;

import java.util.Iterator;

import be.uclouvain.jail.fa.IFATrace;
import be.uclouvain.jail.fa.utils.FATrace;
import be.uclouvain.jail.graph.IDirectedGraphWriter;

/** Decorates a trace as a string. */
public class FATraceSampleString<L> implements IFAAwareString<L> {

	/** Decorated trace. */
	private FATrace<L> trace;
	
	/** Creates a string. */
	@SuppressWarnings("unchecked")
	public FATraceSampleString(IFATrace<L> trace) {
		this.trace = (FATrace<L>) trace.adapt(FATrace.class);
	}
	
	/** Returns true if the string is negative. */
	public boolean isPositive() {
		return trace.isAccepted();
	}

	/** Returns true if the string is negative. */
	public boolean isNegative() {
		return !isPositive();
	}

	/** Returns size of the string. */
	public int size() {
		return trace.size();
	}

	/** Returns an iterator on letters. */
	public Iterator<L> iterator() {
		return trace.iterator();
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