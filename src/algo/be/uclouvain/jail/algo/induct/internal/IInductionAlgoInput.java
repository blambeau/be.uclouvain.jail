package be.uclouvain.jail.algo.induct.internal;

import be.uclouvain.jail.algo.induct.open.ICompatibility;
import be.uclouvain.jail.algo.induct.open.IEvaluator;
import be.uclouvain.jail.algo.induct.open.IOracle;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.uinfo.UserInfoAggregator;

/**
 * Defines the contract to be input of an induction algorithm.
 * 
 * @author blambeau
 */
public interface IInductionAlgoInput {

	/** Returns the input PTA. */
	public IDFA getInputPTA();

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