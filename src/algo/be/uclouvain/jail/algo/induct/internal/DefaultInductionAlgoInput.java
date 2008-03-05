package be.uclouvain.jail.algo.induct.internal;

import be.uclouvain.jail.algo.graph.copy.match.GMatchAggregator;
import be.uclouvain.jail.algo.induct.compatibility.Compatibilities;
import be.uclouvain.jail.algo.induct.compatibility.ICompatibility;
import be.uclouvain.jail.algo.induct.listener.IInductionListener;
import be.uclouvain.jail.algo.induct.listener.InductionListeners;
import be.uclouvain.jail.algo.induct.oracle.AbstractMembershipOracle;
import be.uclouvain.jail.algo.induct.oracle.IMembershipQueryTester;
import be.uclouvain.jail.algo.induct.oracle.IOracle;
import be.uclouvain.jail.algo.induct.processor.IInductionProcessor;
import be.uclouvain.jail.algo.induct.processor.InductionProcessors;
import be.uclouvain.jail.algo.induct.utils.ClassicEvaluator;
import be.uclouvain.jail.algo.induct.utils.IEvaluator;
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

	/** Compatibility informer (optional). */
	private ICompatibility compatibility;

	/** Oracle (optional). */
	private IOracle oracle;

	/** Listener (optional). */
	private IInductionListener listener;

	/** Merge evaluator (BlueFringe only) */ 
	private IEvaluator evaluator;

	/** Pre-processor to use. */
	private IInductionProcessor preProcessor;
	
	/** Post-processor to use. */
	private IInductionProcessor postProcessor;
	
	/** Consolidation threshold (BlueFringe only). */
	private int cThreshold;

	/** Representor attribute. */
	private String repAttr = "representor";
	
	/** Creates a induction info. */
	public DefaultInductionAlgoInput(ISample<?> input) {
		this.input = input;
		oracle = null;
		evaluator = new ClassicEvaluator();
		cThreshold = -1;

		UserInfoAggregator stateAggregator = input.getUserInfoHandler().getVertexAggregator();
		stateAggregator.boolOr(AttributeGraphFAInformer.STATE_INITIAL_KEY);
		stateAggregator.stateKind(AttributeGraphFAInformer.STATE_KIND_KEY,FAStateKindFunction.OR,FAStateKindFunction.OR,true);
		stateAggregator.min(repAttr);
		stateAggregator.first(MappingUtils.KDP_REPRESENTOR);
		stateAggregator.first(MappingUtils.KP_REPRESENTOR);
		
		UserInfoAggregator edgeAggregator = input.getUserInfoHandler().getEdgeAggregator();
		edgeAggregator.first(AttributeGraphFAInformer.EDGE_LETTER_KEY);
		edgeAggregator.min(repAttr);
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

	/** Returns the induction listener to use. */
	public IInductionListener getListener() {
		return listener;
	}
	
	/** Sets the induction listener. */
	public void setInductionListener(IInductionListener listener) {
		this.listener = listener;
	}
	
	/** Adds an induction listener. */
	public void addInductionListener(IInductionListener l) {
		if (listener == null) { listener = new InductionListeners(); }
		if (listener instanceof InductionListeners == false) {
			throw new IllegalStateException("Unable to add listener on " + listener);
		}
		((InductionListeners)listener).addListener(l);
	}
	
	/** Returns pre-processor to use. */
	public IInductionProcessor getPreProcessor() {
		return preProcessor;
	}
	
	/** Sets the pre-processor. */
	public void setPreProcessos(IInductionProcessor p) {
		this.preProcessor = p;
	}
	
	/** Adds a pre-processor. */
	public void addPreProcessor(IInductionProcessor p) {
		if (preProcessor == null) { preProcessor = new InductionProcessors(); }
		if (preProcessor instanceof InductionProcessors == false) {
			throw new IllegalStateException("Unable to add processor on " + preProcessor);
		}
		((InductionProcessors)preProcessor).addProcessor(p);
	}
	
	/** Returns listener to use. */
	public IInductionProcessor getPostProcessor() {
		return postProcessor;
	}
	
	/** Sets the post-processor. */
	public void setPostProcessos(IInductionProcessor p) {
		this.postProcessor = p;
	}
	
	/** Adds a post-processor. */
	public void addPostProcessor(IInductionProcessor p) {
		if (postProcessor == null) { postProcessor = new InductionProcessors(); }
		if (postProcessor instanceof InductionProcessors == false) {
			throw new IllegalStateException("Unable to add processor on " + postProcessor);
		}
		((InductionProcessors)postProcessor).addProcessor(p);
	}

	/** Returns compatibility informer. */
	public ICompatibility getCompatibility() {
		return compatibility;
	}

	/** Adds a compatibility informer. */
	public void setCompatibility(ICompatibility compatibility) {
		this.compatibility = compatibility;
	}

	/** Adds an incompatibility layer. */
	public void addCompatibility(ICompatibility c) {
		if (compatibility == null) { compatibility = new Compatibilities(); } 
		if (compatibility instanceof Compatibilities == false) {
			throw new IllegalStateException("Unable to add incompatibility on " + compatibility);
		}
		((Compatibilities)compatibility).addCompatibility(c);
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
	
	/** Returns representor attribute. */
	public String getRepresentorAttr() {
		return repAttr;
	}
	
	/** Sets representor attribute. */
	public void setRepresentorAttr(String repAttr) {
		this.repAttr = repAttr;
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
