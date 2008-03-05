package be.uclouvain.jail.algo.induct.compatibility;

import java.util.ArrayList;
import java.util.List;

import be.uclouvain.jail.algo.induct.internal.InductionAlgo;

/**
 * Provides a handler to keep information about state compatibility.
 * 
 * @author blambeau
 */
public class Compatibilities implements ICompatibility {

	/** Compatibility layers. */
	private List<ICompatibility> layers;
	
	/** Found extensible compatibility. */
	private ICompatibility extensible;
	
	/** Creates an handler instance. */
	public Compatibilities() {
		layers = new ArrayList<ICompatibility>();
	}
	
	/** Adds a compatibility. */
	public void addCompatibility(ICompatibility c) {
		layers.add(c);
	}
	
	/** Initializes the handler. */
	public void initialize(InductionAlgo algo) {
		for (ICompatibility c: layers) {
			c.initialize(algo);
		}
	}

	/** Checks if two states are compatible. */ 
	public boolean isCompatible(Object s, Object t) {
		for (ICompatibility c: layers) {
			if (!c.isCompatible(s, t)) {
				return false;
			}
		}
		return true;
	}

	/** Checks if one layer is extensible. */
	public boolean isExtensible() {
		if (extensible != null) { return true; }
		for (ICompatibility c: layers) {
			if (c.isExtensible()) {
				extensible = c;
				return true;
			}
		}
		return false;
	}

	/** Delegates to the extensible layer. */
	public void markAsIncompatible(Object s, Object t) {
		if (extensible == null && !isExtensible()) {
			throw new IllegalStateException("No extensible ICompatibility found.");
		}
		extensible.markAsIncompatible(s, t);
	}

}
