package be.uclouvain.jail.algo.induct.open;

import be.uclouvain.jail.algo.induct.internal.InductionAlgo;

/** 
 * Provides information about states compatibility inside a PTA.
 * 
 * <p>This interface is introduced to help induction algorithms
 * to avoid merging incompatible states.</p>
 * 
 */
public interface ICompatibility {

	/** Initializes the information when the induction algrotihm
	 * starts. */
	public void initialize(InductionAlgo inductionalgo);

	/** Checks if two states are compatible. */
	public boolean isCompatible(Object s, Object t);
	
}
