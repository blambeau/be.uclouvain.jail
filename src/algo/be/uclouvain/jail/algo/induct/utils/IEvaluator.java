package be.uclouvain.jail.algo.induct.utils;

import be.uclouvain.jail.algo.induct.internal.Simulation;

/** Evaluates a merging simulation. 
 * 
 * <p>This interface is introduced for BlueFringe like algorithm
 * that relay on state merging evaluations.</p>
 * 
 */
public interface IEvaluator {

	/** 
	 * Evaluates the current simulation. 
	 *
	 * @return an evaluation integer.
	 */
	public abstract int evaluate(Simulation simulation);

}
