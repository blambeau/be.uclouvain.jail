package be.uclouvain.jail.algo.induct.internal;

import net.chefbe.javautils.comparisons.HashCodeUtils;

/** State-and-Letter pair. */
class SLPair {

	/** State. */
	private Object state;

	/** Letter. */
	private Object letter;

	/** Creates a pair instance. */
	public SLPair(Object state, Object letter) {
		if (state == null || letter == null) {
			throw new IllegalArgumentException("Parameters are all mandatory.");
		} else {
			this.state = state;
			this.letter = letter;
		}
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
		if (!(arg0 instanceof SLPair)) {
			return false;
		}
		SLPair other = (SLPair) arg0;
		return state.equals(other.state) && letter.equals(other.letter);
	}

	/** Computes an hash code. */
	public int hashCode() {
		int hash = HashCodeUtils.SEED;
		hash = HashCodeUtils.hash(hash, state);
		hash = HashCodeUtils.hash(hash, letter);
		return hash;
	}
	
}
