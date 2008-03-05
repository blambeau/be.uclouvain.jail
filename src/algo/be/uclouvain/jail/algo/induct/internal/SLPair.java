package be.uclouvain.jail.algo.induct.internal;

import net.chefbe.javautils.comparisons.HashCodeUtils;

/** State-and-Letter pair. */
public class SLPair {

	/** State. */
	private Object state;

	/** Letter. */
	private Object letter;

	/** Cached hash code. */
	private int hash = -1;
	
	/** Creates a pair instance. */
	public SLPair(Object state, Object letter) {
		if (state == null || letter == null) {
			throw new IllegalArgumentException("Parameters are all mandatory.");
		}
		this.state = state;
		this.letter = letter;
		
		// compute hash code once
		hash = HashCodeUtils.SEED;
		hash = HashCodeUtils.hash(hash, state);
		hash = HashCodeUtils.hash(hash, letter);
	}

	/** Returns the state. */
	public Object kState() {
		return state;
	}

	/** Returns the letter. */
	public Object letter() {
		return letter;
	}

	/** Checks equality with another pair. */
	public boolean equals(Object arg0) {
		if (arg0 instanceof SLPair == false) {
			return false;
		}
		SLPair other = (SLPair) arg0;
		return state.equals(other.state) && letter.equals(other.letter);
	}

	/** Computes an hash code. */
	public int hashCode() {
		return hash;
	}
	
}
