package be.uclouvain.jail.algo.induct.extension;

import be.uclouvain.jail.algo.induct.internal.InductionAlgo;

/**
 * Provides an extension for induction algorithm.
 * 
 * @author blambeau
 */
public interface IInductionAlgoExtension {

	/** Installs the extension. */
	public void install(InductionAlgo algo);
	
	/** Initializes the extension on the new PTA. */
	public void initialize(InductionAlgo algo);
	
}
