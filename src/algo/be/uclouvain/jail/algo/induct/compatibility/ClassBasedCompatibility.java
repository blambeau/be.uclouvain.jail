package be.uclouvain.jail.algo.induct.compatibility;

import be.uclouvain.jail.algo.induct.internal.InductionAlgo;

/**
 * Provides a compatibility informer based on state class.
 * 
 * @author blambeau
 */
public class ClassBasedCompatibility extends AbstractCompatibility {

	/** State class attribute. */
	private String classAttr;
	
	/** Creates a compatibility layer instance. */
	public ClassBasedCompatibility(String classAttr) {
		this.classAttr = classAttr;
	}

	/** Initializes the layer. */
	public void initialize(InductionAlgo algo) {
	}

	/** Returns true by default. */
	public boolean isCompatible(Object s, Object t) {
		return true;
	}

	/** Returns false. */
	public boolean isExtensible() {
		return true;
	}

	/** Throws an UnsupportedOperationException. */
	public void markAsIncompatible(Object s, Object t) {
	}

}
