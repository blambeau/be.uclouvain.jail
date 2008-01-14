package be.uclouvain.jail.algo.induct.internal;

import net.chefbe.javautils.adapt.IAdaptable;
import be.uclouvain.jail.algo.commons.IAlgoInput;
import be.uclouvain.jail.algo.induct.open.ICompatibility;
import be.uclouvain.jail.algo.induct.open.IEvaluator;
import be.uclouvain.jail.algo.induct.open.IOracle;
import be.uclouvain.jail.uinfo.UserInfoAggregator;

/**
 * Defines the contract to be input of an induction algorithm.
 * 
 * @author blambeau
 */
public interface IInductionAlgoInput extends IAlgoInput {

	/** Returns input of the induction. */
	public IAdaptable getInput();

	/** Returns compatibility. */
	public ICompatibility getCompatibility();

	/** Returns the oracle to use, if any. */
	public IOracle getOracle();

	/** Returns evaluator. */
	public IEvaluator getEvaluator();

	/** Returns the consolidation threshold. */
	public int getConsolidationThreshold();

	/** Returns the state aggregator to use. */
	public UserInfoAggregator getStateAggregator();

	/** Returns the state aggregator to use. */
	public UserInfoAggregator getEdgeAggregator();

}