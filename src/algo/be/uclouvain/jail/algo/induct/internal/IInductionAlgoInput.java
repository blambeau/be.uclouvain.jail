package be.uclouvain.jail.algo.induct.internal;

import be.uclouvain.jail.algo.commons.IAlgoInput;
import be.uclouvain.jail.algo.induct.extension.IInductionAlgoExtension;
import be.uclouvain.jail.algo.induct.oracle.IOracle;
import be.uclouvain.jail.algo.induct.utils.IEvaluator;
import be.uclouvain.jail.fa.ISample;

/**
 * Defines the contract to be input of an induction algorithm.
 * 
 * @author blambeau
 */
public interface IInductionAlgoInput extends IAlgoInput {

	/** Returns input of the induction. */
	public <L> ISample<L> getInput();

	/** Returns the oracle to use, if any. */
	public IOracle getOracle();

	/** Returns evaluator. */
	public IEvaluator getEvaluator();

	/** Returns the representor attribute. */
	public String getRepresentorAttr();
	
	/** Returns unknown representor value. */
	public Object getUnknown();
	
	/** Returns the consolidation threshold. */
	public int getConsolidationThreshold();

	/** Returns extension to install. */
	public IInductionAlgoExtension getExtension();
	
}