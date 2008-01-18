package be.uclouvain.jail.algo.induct.internal;

import be.uclouvain.jail.algo.commons.IAlgoInput;
import be.uclouvain.jail.algo.induct.open.ICompatibility;
import be.uclouvain.jail.algo.induct.open.IEvaluator;
import be.uclouvain.jail.algo.induct.open.IOracle;
import be.uclouvain.jail.fa.ISample;

/**
 * Defines the contract to be input of an induction algorithm.
 * 
 * @author blambeau
 */
public interface IInductionAlgoInput extends IAlgoInput {

	/** Returns input of the induction. */
	public <L> ISample<L> getInput();

	/** Returns compatibility. */
	public ICompatibility getCompatibility();

	/** Returns the oracle to use, if any. */
	public IOracle getOracle();

	/** Returns evaluator. */
	public IEvaluator getEvaluator();

	/** Returns the consolidation threshold. */
	public int getConsolidationThreshold();

}