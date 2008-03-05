package be.uclouvain.jail.algo.induct.compatibility;

import be.uclouvain.jail.algo.induct.internal.InductionAlgo;

/** 
 * Provides information about states compatibility inside a PTA.
 * 
 * <p>This interface is introduced to help induction algorithms
 * to avoid merging incompatible states.</p>
 */
public interface ICompatibility {

	/** Initializes the information when the induction algrotihm
	 * starts. */
	public void initialize(InductionAlgo algo);

	/** Checks if two states are compatible. */
	public boolean isCompatible(Object s, Object t);

	/** Returns true if the compatibility is extensible. */
	public boolean isExtensible();
	
	/** Adds a pair of incompatible states. */
	public void markAsIncompatible(Object s, Object t);
	
}
