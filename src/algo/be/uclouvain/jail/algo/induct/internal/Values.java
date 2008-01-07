package be.uclouvain.jail.algo.induct.internal;

import net.chefbe.javautils.collections.arrays.ArrayUtils;

/** Encapsulates values attached to edges and states. */
class Values {

	/** Attached values. */
	private Object values[];

	/** Creates values for a given size. */
	public Values(int size) {
		values = new Object[size];
	}

	/** Sets the i-th value. */
	public void setValue(int i, Object value) {
		if (i >= values.length) {
			throw new IllegalArgumentException("Index out of bounds.");
		}
		values[i] = value;
	}

	/** Returns the i-th value. */
	public Object valueAt(int i) {
		if (i >= values.length) {
			throw new IllegalArgumentException("Index out of bounds.");
		}
		return values[i];
	}

	/** Returns a string representation. */
	public String toString() {
		return ArrayUtils.toString(values, ",");
	}

}
