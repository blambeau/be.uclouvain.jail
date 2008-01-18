package be.uclouvain.jail.vm.toolkits;

import net.chefbe.javautils.adapt.IAdapter;
import net.chefbe.javautils.adapt.StringAdaptationTool;
import be.uclouvain.jail.algo.fa.complement.DFAComplementorAlgo;
import be.uclouvain.jail.algo.fa.complement.DFAComplementorHeuristic;
import be.uclouvain.jail.algo.fa.complement.DefaultDFAComplementorInput;
import be.uclouvain.jail.algo.fa.complement.DefaultDFAComplementorResult;
import be.uclouvain.jail.algo.fa.compose.DFAComposerAlgo;
import be.uclouvain.jail.algo.fa.compose.DefaultDFAComposerInput;
import be.uclouvain.jail.algo.fa.compose.DefaultDFAComposerResult;
import be.uclouvain.jail.algo.fa.determinize.DefaultNFADeterminizerInput;
import be.uclouvain.jail.algo.fa.determinize.DefaultNFADeterminizerResult;
import be.uclouvain.jail.algo.fa.determinize.NFADeterminizerAlgo;
import be.uclouvain.jail.algo.fa.minimize.DFAMinimizerAlgo;
import be.uclouvain.jail.algo.fa.minimize.DefaultDFAMinimizerInput;
import be.uclouvain.jail.algo.fa.minimize.DefaultDFAMinimizerResult;
import be.uclouvain.jail.algo.fa.rand.DefaultRandomStringsInput;
import be.uclouvain.jail.algo.fa.rand.DefaultRandomStringsResult;
import be.uclouvain.jail.algo.fa.rand.IRandomStringsResult;
import be.uclouvain.jail.algo.fa.rand.RandomDFAInput;
import be.uclouvain.jail.algo.fa.rand.RandomDFAResult;
import be.uclouvain.jail.algo.fa.rand.RandomStringsAlgo;
import be.uclouvain.jail.algo.fa.tmoves.DefaultTauRemoverInput;
import be.uclouvain.jail.algo.fa.tmoves.DefaultTauRemoverResult;
import be.uclouvain.jail.algo.fa.tmoves.ITauInformer;
import be.uclouvain.jail.algo.fa.tmoves.TauRemoverAlgo;
import be.uclouvain.jail.algo.fa.uncomplement.FAUncomplementorAlgo;
import be.uclouvain.jail.algo.fa.walk.DFARandomWalkInput;
import be.uclouvain.jail.algo.fa.walk.DFARandomWalkResult;
import be.uclouvain.jail.algo.graph.copy.match.GMatchAggregator;
import be.uclouvain.jail.algo.graph.copy.match.GMatchPopulator;
import be.uclouvain.jail.algo.graph.rand.RandomGraphAlgo;
import be.uclouvain.jail.algo.graph.walk.IRandomWalkResult;
import be.uclouvain.jail.algo.graph.walk.RandomWalkAlgo;
import be.uclouvain.jail.dialect.seqp.SEQPGraphDialect;
import be.uclouvain.jail.fa.FAStateKind;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.IFA;
import be.uclouvain.jail.fa.INFA;
import be.uclouvain.jail.fa.impl.AttributeGraphFAInformer;
import be.uclouvain.jail.fa.impl.GraphDFA;
import be.uclouvain.jail.fa.impl.GraphNFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.utils.DirectedGraphWriter;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.UserInfoHandler;
import be.uclouvain.jail.vm.JailReflectionToolkit;
import be.uclouvain.jail.vm.JailVM;
import be.uclouvain.jail.vm.JailVMException;
import be.uclouvain.jail.vm.JailVMOptions;

/** Installs automaton toolkit. */
public class AutomatonToolkit extends JailReflectionToolkit implements IAdapter {

	/** Installs the toolkit on the virtual machine. */
	public void install(JailVM vm) {
		vm.registerAdaptation(IDirectedGraph.class, IFA.class, this);
		vm.registerAdaptation(IDirectedGraph.class, IDFA.class, this);
		vm.registerAdaptation(IDirectedGraph.class, INFA.class, this);
		vm.registerAdaptation(INFA.class, IDirectedGraph.class, this);
		vm.registerAdaptation(IDFA.class, IDirectedGraph.class, this);
		
		vm.registerDialectLoader("seqp", new SEQPGraphDialect());
		
		vm.getUserInfoHelper().register(AttributeGraphFAInformer.STATE_INITIAL_KEY, Boolean.class);
		vm.getUserInfoHelper().addAdapter(FAStateKind.class, StringAdaptationTool.TO_ENUM);
		vm.getUserInfoHelper().register(AttributeGraphFAInformer.STATE_KIND_KEY, FAStateKind.class);
	}

	/** Determinizes a NDA. */
	public IDFA determinize(INFA nfa, JailVMOptions opt) throws JailVMException {
		DefaultNFADeterminizerInput input = new DefaultNFADeterminizerInput(nfa);
		DefaultNFADeterminizerResult result = new DefaultNFADeterminizerResult();
	
		// state aggregator
		if (opt.hasOption("state")) {
			GMatchAggregator aggregator = opt.getOptionValue("state",GMatchAggregator.class,null);
			result.getStateAggregator().addPopulator(aggregator);
		}
		
		// edge aggregator
		if (opt.hasOption("edge")) {
			GMatchAggregator aggregator = opt.getOptionValue("state",GMatchAggregator.class,null);
			result.getEdgeAggregator().addPopulator(aggregator);
		}
		
		new NFADeterminizerAlgo().execute(input,result);
		return (IDFA) result.adapt(IDFA.class);
	}
	
	/** Minimizes a DDA. */
	public IDFA minimize(IDFA dfa, JailVMOptions opt) throws JailVMException {
		DefaultDFAMinimizerInput input = new DefaultDFAMinimizerInput(dfa);
		DefaultDFAMinimizerResult result = new DefaultDFAMinimizerResult();
		
		// state aggregator
		if (opt.hasOption("state")) {
			GMatchAggregator aggregator = opt.getOptionValue("state",GMatchAggregator.class,null);
			result.getStateAggregator().addPopulator(aggregator);
		}
		
		// edge aggregator
		if (opt.hasOption("edge")) {
			GMatchAggregator aggregator = opt.getOptionValue("state",GMatchAggregator.class,null);
			result.getEdgeAggregator().addPopulator(aggregator);
		}
		
		new DFAMinimizerAlgo().execute(input,result);
		//System.out.println(result.getStatePartition().toString(dfa.getGraph().getVerticesTotalOrder()));
		return (IDFA) result.adapt(IDFA.class);
	}
	
	/** Removes tau-transitions of a FA. */
	public INFA tmoves(IFA fa, JailVMOptions opt) throws JailVMException {
		// find tau letter
		String tau = "";
		if (opt.hasOption("tau")) {
			tau = opt.getOptionValue("tau", String.class, null);
		}
		
		// install algorithm
		final String tauF = tau;
		ITauInformer informer = new ITauInformer() {
			public boolean isEpsilon(Object letter) {
				return tauF.equals(letter);
			}
		}; 
		DefaultTauRemoverInput input = new DefaultTauRemoverInput(fa,informer);
		DefaultTauRemoverResult result = new DefaultTauRemoverResult();
		
		if (opt.hasOption("state")) {
			GMatchPopulator<IUserInfo> populator = opt.getOptionValue("state",GMatchPopulator.class,null);
			result.getStateCopier().addPopulator(populator);
		}
		
		// edge aggregator
		if (opt.hasOption("edge")) {
			GMatchAggregator aggregator = opt.getOptionValue("state",GMatchAggregator.class,null);
			result.getEdgeAggregator().addPopulator(aggregator);
		}
		
		// execute and returns
		new TauRemoverAlgo().execute(input,result);
		return (INFA) result.adapt(INFA.class);
	}
	
	/** Composes two DFAs. */
	public IDFA compose(IDFA[] dfas, JailVMOptions options) throws JailVMException {
		IDFA ret = new GraphDFA();
		DefaultDFAComposerInput input = new DefaultDFAComposerInput(dfas); 
		DefaultDFAComposerResult result = new DefaultDFAComposerResult(ret); 

		// add state populator
		if (options.hasOption("state")) {
			GMatchAggregator aggregator = options.getOptionValue("state",GMatchAggregator.class,null);
			result.getStateAggregator().addPopulator(aggregator);
		}

		// add edge populator
		if (options.hasOption("edge")) {
			GMatchAggregator aggregator = options.getOptionValue("edge",GMatchAggregator.class,null);
			result.getEdgeAggregator().addPopulator(aggregator);
		}
		
		new DFAComposerAlgo().execute(input,result);
		return ret;
	}

	/** Complements a DFA. */
	public IDFA complement(IDFA dfa, JailVMOptions options, JailVM vm) throws JailVMException {
		DefaultDFAComplementorInput input = new DefaultDFAComplementorInput(dfa);
		DefaultDFAComplementorResult result = new DefaultDFAComplementorResult();
		result.keepAll(false, true, true);
		
		String heuristic = options.getOptionValue("heuristic", String.class, "error");
		if ("error".equals(heuristic)) {
			input.setHeuristic(DFAComplementorHeuristic.ERROR_STATE);
		} else if ("same".equals(heuristic)) {
			input.setHeuristic(DFAComplementorHeuristic.SAME_STATE);
		} else {
			throw new JailVMException(JailVMException.ERROR_TYPE.BAD_COMMAND_USAGE,null,
					"Unknown completion heuristic: " + heuristic);
		}
		
		new DFAComplementorAlgo().execute(input,result);
		return (IDFA) result.adapt(IDFA.class);
	}
	
	public IDFA uncomplement(IDFA dfa, JailVMOptions options) throws JailVMException {
		UserInfoHandler handler = new UserInfoHandler();
		handler.keepAll(false, true, true);
		DirectedGraphWriter writer = new DirectedGraphWriter(handler);
		new FAUncomplementorAlgo().execute(dfa,writer);
		return (IDFA) writer.adapt(IDFA.class);
	}
	
	/** Generates a random DFA. */
	public IDFA randdfa(JailVMOptions options, JailVM vm) throws JailVMException {
		RandomDFAInput input = new RandomDFAInput();
		RandomDFAResult result = new RandomDFAResult();
		input.setOptions(options);
		result.setOptions(options);
		new RandomGraphAlgo().execute(input,result);
		return (IDFA) result.adapt(IDFA.class);
	}
	
	/** Generates a random DFA. */
	public IRandomWalkResult randsample(IDFA dfa, JailVMOptions options, JailVM vm) throws JailVMException {
		DFARandomWalkInput input = new DFARandomWalkInput(dfa);
		DFARandomWalkResult result = new DFARandomWalkResult();
		input.setOptions(options);
		result.setOptions(options);
		new RandomWalkAlgo().execute(input,result);
		return result;
	}
	
	/** Randomize strings. */
	public IRandomStringsResult randstrings(JailVMOptions options, JailVM vm) throws JailVMException {
		DefaultRandomStringsInput<Object> input = new DefaultRandomStringsInput<Object>(); 
		DefaultRandomStringsResult<Object> result = new DefaultRandomStringsResult<Object>();
		input.setOptions(options);
		result.setOptions(options);
		new RandomStringsAlgo().execute(input,result);
		return result;
	}
	
	/** Adapts an object. */
	public Object adapt(Object who, Class type) {
		if (type.equals(IDFA.class)) {
			return adaptToDFA(who);
		} else if (type.equals(INFA.class) || type.equals(IFA.class)) {
			return adaptToNFA(who);
		} else if (type.equals(IDirectedGraph.class)) {
			return adaptToDirectedGraph(who);
		}
		return super.adapt(who, type);
	}

	/** Adapts who to a DFA. */
	private IDirectedGraph adaptToDirectedGraph(Object who) {
		if (who instanceof INFA) {
			return ((INFA)who).getGraph();
		} else if (who instanceof IDFA) {
			return ((IDFA)who).getGraph();
		} else {
			throw new IllegalStateException("Unable to convert " + who + " to a IDirectedGraph");
		}
	}
	
	/** Adapts who to a DFA. */
	private IDFA adaptToDFA(Object who) {
		if (who instanceof IDirectedGraph) {
			return new GraphDFA((IDirectedGraph)who, new AttributeGraphFAInformer());
		} else {
			throw new IllegalStateException("Unable to convert " + who + " to a IDFA");
		}
	}
	
	/** Adapts who to a NFA. */
	private INFA adaptToNFA(Object who) {
		if (who instanceof IDirectedGraph) {
			return new GraphNFA((IDirectedGraph)who, new AttributeGraphFAInformer());
		} else {
			throw new IllegalStateException("Unable to convert " + who + " to a INFA");
		}
	}

	/** Returns name of the toolkit. */
	public String toString() {
		return "AutomatonToolkit";
	}
	
}
