package be.uclouvain.jail.vm.toolkits;

import be.uclouvain.jail.adapt.IAdapter;
import be.uclouvain.jail.algo.fa.compose.DFAComposerAlgo;
import be.uclouvain.jail.algo.fa.compose.DefaultDFAComposerInput;
import be.uclouvain.jail.algo.fa.compose.DefaultDFAComposerResult;
import be.uclouvain.jail.algo.fa.determinize.NFADeterminizer;
import be.uclouvain.jail.algo.fa.minimize.DFAMinimizer;
import be.uclouvain.jail.algo.fa.tmoves.ITauInformer;
import be.uclouvain.jail.algo.fa.tmoves.TauRemover;
import be.uclouvain.jail.algo.graph.copy.match.GMatchAggregator;
import be.uclouvain.jail.dialect.seqp.SEQPGraphDialect;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.INFA;
import be.uclouvain.jail.fa.impl.AttributeGraphFAInformer;
import be.uclouvain.jail.fa.impl.GraphDFA;
import be.uclouvain.jail.fa.impl.GraphNFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.vm.JailReflectionToolkit;
import be.uclouvain.jail.vm.JailVM;
import be.uclouvain.jail.vm.JailVMException;

/** Installs automaton toolkit. */
public class AutomatonToolkit extends JailReflectionToolkit implements IAdapter {

	/** Installs the toolkit on the virtual machine. */
	public void install(JailVM vm) {
		vm.registerAdaptation(IDirectedGraph.class, IDFA.class, this);
		vm.registerAdaptation(IDirectedGraph.class, INFA.class, this);
		vm.registerAdaptation(INFA.class, IDirectedGraph.class, this);
		vm.registerAdaptation(IDFA.class, IDirectedGraph.class, this);
		
		vm.registerDialectLoader("seqp", new SEQPGraphDialect());
	}

	/** Determinizes a NDA. */
	public IDFA determinize(INFA nfa) {
		return new NFADeterminizer(nfa).getResultingDFA();
	}
	
	/** Minimizes a DDA. */
	public IDFA minimize(IDFA dfa) {
		return new DFAMinimizer(dfa).getMinimalDFA();
	}
	
	/** Minimizes a DDA. */
	public INFA tmoves(IDFA dfa) {
		return new TauRemover(dfa, new ITauInformer() {
			public boolean isEpsilon(Object letter) {
				return "tau".equals(letter);
			}
		}).getResultingNFA();
	}
	
	/** Composes two DFAs. */
	public IDFA compose(IDFA[] dfas) throws JailVMException {
		IDFA ret = new GraphDFA();
		DefaultDFAComposerInput input = new DefaultDFAComposerInput(dfas); 
		DefaultDFAComposerResult result = new DefaultDFAComposerResult(ret); 

		// add state populator
		if (hasOption("state")) {
			GMatchAggregator aggregator = getOptionValue("state",GMatchAggregator.class,null);
			result.getStateAggregator().addPopulator(aggregator);
		}
		
		new DFAComposerAlgo().execute(input,result);
		return ret;
	}

	/** Adapts an object. */
	public Object adapt(Object who, Class type) {
		if (type.equals(IDFA.class)) {
			return adaptToDFA(who);
		} else if (type.equals(INFA.class)) {
			return adaptToNFA(who);
		} else if (type.equals(IDirectedGraph.class)) {
			return adaptToDirectedGraph(who);
		}
		return null;
	}

	/** Adapts who to a DFA. */
	public IDirectedGraph adaptToDirectedGraph(Object who) {
		if (who instanceof INFA) {
			return ((INFA)who).getGraph();
		} else if (who instanceof IDFA) {
			return ((IDFA)who).getGraph();
		} else {
			throw new IllegalStateException("Unable to convert " + who + " to a IDirectedGraph");
		}
	}
	
	/** Adapts who to a DFA. */
	public IDFA adaptToDFA(Object who) {
		if (who instanceof IDirectedGraph) {
			return new GraphDFA((IDirectedGraph)who, new AttributeGraphFAInformer());
		} else {
			throw new IllegalStateException("Unable to convert " + who + " to a IDFA");
		}
	}
	
	/** Adapts who to a NFA. */
	public INFA adaptToNFA(Object who) {
		if (who instanceof IDirectedGraph) {
			return new GraphNFA((IDirectedGraph)who, new AttributeGraphFAInformer());
		} else {
			throw new IllegalStateException("Unable to convert " + who + " to a INFA");
		}
	}
	
}
