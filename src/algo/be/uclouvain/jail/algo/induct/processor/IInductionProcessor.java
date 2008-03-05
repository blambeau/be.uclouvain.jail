package be.uclouvain.jail.algo.induct.processor;

import be.uclouvain.jail.algo.induct.internal.InductionAlgo;

/**
 * Allows pre and post processing of input PTA and DFA result.
 * 
 * @author blambeau
 */
public interface IInductionProcessor {

	/** Process the algorithm. */
	public void process(InductionAlgo algo);

}
