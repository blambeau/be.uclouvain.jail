package be.uclouvain.jail.algo.induct.internal;

import be.uclouvain.jail.algo.graph.copy.match.GMatchAggregator;
import be.uclouvain.jail.algo.induct.open.ICompatibility;
import be.uclouvain.jail.algo.induct.open.IEvaluator;
import be.uclouvain.jail.algo.induct.open.IMembershipQueryTester;
import be.uclouvain.jail.algo.induct.open.IOracle;
import be.uclouvain.jail.algo.induct.utils.AbstractMembershipOracle;
import be.uclouvain.jail.algo.induct.utils.ClassicEvaluator;
import be.uclouvain.jail.algo.utils.AbstractAlgoInput;
import be.uclouvain.jail.fa.ISample;
import be.uclouvain.jail.fa.functions.FAStateKindFunction;
import be.uclouvain.jail.fa.impl.AttributeGraphFAInformer;
import be.uclouvain.jail.uinfo.UserInfoAggregator;

/** 
 * Input information for induction algorithms. 
 */
public class DefaultInductionAlgoInput extends AbstractAlgoInput implements IInductionAlgoInput {

	/** Input sample. */
	private ISample<?> input;

	/** Oracle (optional). */
	private IOracle oracle;

	/** Merge evaluator (BlueFringe only) */ 
	private IEvaluator evaluator;

	/** Consolidation threshold (BlueFringe only). */
	private int cThreshold;

	/** Compatibility informer. */
	private ICompatibility compatibility;

	/** Creates a induction info. */
	public DefaultInductionAlgoInput(ISample<?> input) {
		this.input = input;
		oracle = null;
		evaluator = new ClassicEvaluator();
		cThreshold = -1;

		UserInfoAggregator stateAggregator = input.getUserInfoHandler().getVertexAggregator();
		stateAggregator.boolOr(AttributeGraphFAInformer.STATE_INITIAL_KEY);
		stateAggregator.stateKind(AttributeGraphFAInformer.STATE_KIND_KEY,
                FAStateKindFunction.OR,
                FAStateKindFunction.OR,true);
		stateAggregator.first(MappingUtils.KDP_REPRESENTOR);
		stateAggregator.first(MappingUtils.KP_REPRESENTOR);
		
		UserInfoAggregator edgeAggregator = input.getUserInfoHandler().getEdgeAggregator();
		edgeAggregator.first(AttributeGraphFAInformer.EDGE_LETTER_KEY);
	}

	/** Sets options. */
	@Override
	protected void installOptions() {
		super.installOptions();
		super.addOption("threshold", "consolidationThreshold", false, Integer.class, null);
		super.addOption("state", "statePopulator", false, GMatchAggregator.class, null);
		super.addOption("edge", "edgePopulator", false, GMatchAggregator.class, null);
		super.addOption("oracle", false, IOracle.class, null);
		super.addOption("querier",false, IMembershipQueryTester.class, null);
	}

	/** Returns input PTA. */
	@SuppressWarnings("unchecked")
	public <L> ISample<L> getInput() {
		return (ISample<L>) input;
	}

	/** Returns compatibility informer. */
	public ICompatibility getCompatibility() {
		return compatibility;
	}

	/** Sets the compatibility informer. */
	public void setCompatibility(ICompatibility compatibility) {
		this.compatibility = compatibility;
	}

	/** Returns oracle. */
	public IOracle getOracle() {
		return oracle;
	}
	
	/** Sets oracle to use. */
	public void setOracle(IOracle oracle) {
		this.oracle = oracle;
	}
	
	/** Sets the querier to use. */
	public void setQuerier(IMembershipQueryTester tester) {
		if (oracle instanceof AbstractMembershipOracle == false) {
			throw new IllegalArgumentException("Oracle does not accept querier.");
		}
		((AbstractMembershipOracle)oracle).setTester(tester);
	}
	
	/** Returns evaluator. */
	public IEvaluator getEvaluator() {
		return evaluator;
	}
	
	/** Sets blue-fringe evaluator. */
	public void setEvaluator(IEvaluator evaluator) {
		this.evaluator = evaluator;
	}

	/** Sets consolidation threshold. */
	public int getConsolidationThreshold() {
		return cThreshold;
	}
	
	/** Returns the consolidation threshold. */
	public void setConsolidationThreshold(int cThreshold) {
		this.cThreshold = cThreshold;
	}
	
	/** Adds a gmatch state populator. */
	public void setStatePopulator(GMatchAggregator populator) {
		input.getUserInfoHandler().getVertexAggregator().addPopulator(populator);
	}
	
	/** Adds a gmatch edge populator. */
	public void setEdgePopulator(GMatchAggregator populator) {
		input.getUserInfoHandler().getEdgeAggregator().addPopulator(populator);
	}
	
}
