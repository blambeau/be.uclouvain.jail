package be.uclouvain.jail.algo.induct.internal;

import be.uclouvain.jail.algo.induct.open.ICompatibility;
import be.uclouvain.jail.algo.induct.open.IEvaluator;
import be.uclouvain.jail.algo.induct.open.IOracle;
import be.uclouvain.jail.algo.induct.utils.ClassicEvaluator;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.functions.FAStateKindFunction;
import be.uclouvain.jail.fa.impl.AttributeGraphFAInformer;
import be.uclouvain.jail.uinfo.UserInfoAggregator;

/** 
 * Input information for induction algorithms. 
 */
public class DefaultInductionAlgoInput implements IInductionAlgoInput {

	/** Input sample. */
	private IDFA pta;

	/** Oracle (optional). */
	private IOracle oracle;

	/** Merge evaluator (BlueFringe only) */ 
	private IEvaluator evaluator;

	/** Consolidation threshold (BlueFringe only). */
	private int cThreshold;

	/** Installed state functions. */
	private UserInfoAggregator stateAggregator;

	/** Installed edge functions. */
	private UserInfoAggregator edgeAggregator;

	/** Compatibility informer. */
	private ICompatibility compatibility;

	/** Creates a induction info. */
	public DefaultInductionAlgoInput(IDFA pta) {
		this.pta = pta;
		oracle = null;
		evaluator = new ClassicEvaluator();
		cThreshold = -1;
		stateAggregator = new UserInfoAggregator();
		edgeAggregator = new UserInfoAggregator();

		stateAggregator.boolOr(AttributeGraphFAInformer.STATE_INITIAL_KEY);
		stateAggregator.stateKind(AttributeGraphFAInformer.STATE_KIND_KEY,
                FAStateKindFunction.OR,
                FAStateKindFunction.OR,true);
		edgeAggregator.first(AttributeGraphFAInformer.EDGE_LETTER_KEY);
	}

	/* (non-Javadoc)
	 * @see be.uclouvain.jail.algo.induct.internal.IInductionAlgoInput#getInputPTA()
	 */
	public IDFA getInputPTA() {
		return pta;
	}

	/* (non-Javadoc)
	 * @see be.uclouvain.jail.algo.induct.internal.IInductionAlgoInput#getCompatibility()
	 */
	public ICompatibility getCompatibility() {
		return compatibility;
	}

	/** Sets the compatibility informer. */
	public void setCompatibility(ICompatibility compatibility) {
		this.compatibility = compatibility;
	}

	/* (non-Javadoc)
	 * @see be.uclouvain.jail.algo.induct.internal.IInductionAlgoInput#getOracle()
	 */
	public IOracle getOracle() {
		return oracle;
	}
	
	/** Sets oracle to use. */
	public void setOracle(IOracle oracle) {
		this.oracle = oracle;
	}
	
	/* (non-Javadoc)
	 * @see be.uclouvain.jail.algo.induct.internal.IInductionAlgoInput#getEvaluator()
	 */
	public IEvaluator getEvaluator() {
		return evaluator;
	}
	
	/** Sets blue-fringe evaluator. */
	public void setEvaluator(IEvaluator evaluator) {
		this.evaluator = evaluator;
	}

	/* (non-Javadoc)
	 * @see be.uclouvain.jail.algo.induct.internal.IInductionAlgoInput#getConsolidationThreshold()
	 */
	public int getConsolidationThreshold() {
		return cThreshold;
	}
	
	/** Returns the consolidation threshold. */
	public void setConsolidationThreshold(int cThreshold) {
		this.cThreshold = cThreshold;
	}
	
	/* (non-Javadoc)
	 * @see be.uclouvain.jail.algo.induct.internal.IInductionAlgoInput#getStateAggregator()
	 */
	public UserInfoAggregator getStateAggregator() {
		return stateAggregator;
	}
	
	/* (non-Javadoc)
	 * @see be.uclouvain.jail.algo.induct.internal.IInductionAlgoInput#getEdgeAggregator()
	 */
	public UserInfoAggregator getEdgeAggregator() {
		return edgeAggregator;
	}
	
}
