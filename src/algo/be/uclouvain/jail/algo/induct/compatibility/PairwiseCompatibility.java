package be.uclouvain.jail.algo.induct.compatibility;

import java.util.HashSet;
import java.util.Set;

import be.uclouvain.jail.algo.induct.internal.InductionAlgo;

/**
 * Extensible compatibility for pairs of incompatible states.
 * 
 * @author blambeau
 */
public class PairwiseCompatibility extends AbstractCompatibility {

	/** A state pair. */
	class StatePair {
		
		/** Two states. */
		protected Object s1, s2;
		
		/** Hash code. */
		private int hash;
		
		/** Creates a state pair. */
		public StatePair(Object s1, Object s2) {
			this.s1 = s1;
			this.s2 = s2;
			hash = s1.hashCode() + s2.hashCode();
		}

		/** Returns the hasInductionAlgo algoh code. */
		public int hashCode() {
			return hash;
		}
		
		/** Checks equality. */
		public boolean equals(Object o) {
			if (o instanceof StatePair == false) { return false; }
			StatePair other = (StatePair) o;
			return (s1.equals(other.s1) && s2.equals(other.s2))
			    || (s1.equals(other.s2) && s2.equals(other.s1));
		}
		
	}
	
	/** Incompatible state pairs. */
	private Set<StatePair> incompatibles;
	
	/** Creates a layer instance. */
	public PairwiseCompatibility() {
		this.incompatibles = new HashSet<StatePair>();
	}

	/** Initializes the layer. */
	@Override
	public void initialize(InductionAlgo algo) {
		this.incompatibles.clear();
	}

	/** Checks compatibility of two states. */
	@Override
	public boolean isCompatible(Object s, Object t) {
		return incompatibles.contains(new StatePair(s,t));
	}

	/** Returns true. */
	@Override
	public boolean isExtensible() {
		return true;
	}

	/** Mark states as incompatibles. */
	@Override
	public void markAsIncompatible(Object s, Object t) {
		incompatibles.add(new StatePair(s,t));
	}
	
}
