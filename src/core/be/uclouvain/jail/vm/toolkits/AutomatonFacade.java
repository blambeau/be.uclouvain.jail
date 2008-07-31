package be.uclouvain.jail.vm.toolkits;

import java.io.IOException;
import java.io.PrintWriter;

import be.uclouvain.jail.algo.fa.compose.DefaultFAComposerInput;
import be.uclouvain.jail.algo.fa.compose.DefaultFAComposerResult;
import be.uclouvain.jail.algo.fa.compose.FAComposerAlgo;
import be.uclouvain.jail.algo.fa.compose.IFAComposerInput;
import be.uclouvain.jail.algo.fa.compose.IFAComposerResult;
import be.uclouvain.jail.algo.fa.determinize.DefaultNFADeterminizerInput;
import be.uclouvain.jail.algo.fa.determinize.DefaultNFADeterminizerResult;
import be.uclouvain.jail.algo.fa.determinize.NFADeterminizerAlgo;
import be.uclouvain.jail.algo.fa.minimize.DFAMinimizerAlgo;
import be.uclouvain.jail.algo.fa.minimize.DefaultDFAMinimizerInput;
import be.uclouvain.jail.algo.fa.minimize.DefaultDFAMinimizerResult;
import be.uclouvain.jail.algo.fa.tmoves.DefaultTauRemoverInput;
import be.uclouvain.jail.algo.fa.tmoves.DefaultTauRemoverResult;
import be.uclouvain.jail.algo.fa.tmoves.ITauInformer;
import be.uclouvain.jail.algo.fa.tmoves.TauRemoverAlgo;
import be.uclouvain.jail.algo.fa.uncomplement.FAUncomplementorAlgo;
import be.uclouvain.jail.algo.fa.utils.FAUtils;
import be.uclouvain.jail.dialect.dot.DOTGraphDialect;
import be.uclouvain.jail.dialect.dot.JDotty;
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
import be.uclouvain.jail.uinfo.IUserInfoPopulator;
import be.uclouvain.jail.uinfo.UserInfoHandler;

/** Provides an facade for using algorithms. */
public final class AutomatonFacade {

	/** Shows a FA using JDotty. 
	 * @throws IOException */
	public static void show(IFA...fas) throws IOException {
		JDotty jdotty = new JDotty();
		for (IFA fa: fas) {
			jdotty.present(FAUtils.copyForDot(fa),null);
		}
	}
	
	/** Debugs an automaton using dot. */
	public static void debug(IFA...fas) throws IOException {
		PrintWriter w = new PrintWriter(System.out);
		for (IFA fa: fas) {
			new DOTGraphDialect().print(fa.getGraph(), w, null);
		}
		w.flush();
	}
	
	/** Uncomplements a DFA. */
	@SuppressWarnings("unchecked")
	public static <T extends IFA> T uncomplement(T dfa) {
		UserInfoHandler handler = new UserInfoHandler();
		handler.keepAll(false, true, true);
		DirectedGraphWriter writer = new DirectedGraphWriter(handler);
		new FAUncomplementorAlgo().execute(dfa,writer);
		return (T) ((dfa instanceof IDFA) ? new GraphDFA(writer.getGraph()) : new GraphNFA(writer.getGraph()));
	}
	
	/** Deep copies a DFA. */
	public static IDFA copy(IDFA dfa, IUserInfoPopulator<IUserInfo> vertex, IUserInfoPopulator<IUserInfo> edge) {
		IDirectedGraph g = dfa.getGraph();
		IDirectedGraph copy = GraphFacade.copy(g, vertex, edge);
		return new GraphDFA(copy);
	}
	
	/** Copies a DFA marking all states of acceptation. */
	@SuppressWarnings("unchecked")
	public static <T extends IFA> T allaccepting(T dfa) {
		IDirectedGraph g = dfa.getGraph();
		IDirectedGraph copy = GraphFacade.copy(g,new IUserInfoPopulator<IUserInfo>() {
			public void populate(IUserInfo target, IUserInfo source) {
				FAStateKind kind = (FAStateKind) source.getAttribute(AttributeGraphFAInformer.STATE_KIND_KEY);
				FAStateKind other = null;
				if (FAStateKind.ACCEPTING.equals(kind)) {
					other = FAStateKind.ACCEPTING;
				} else if (FAStateKind.PASSAGE.equals(kind)) {
					other = FAStateKind.ACCEPTING;
				} else if (FAStateKind.ERROR.equals(kind)) {
					other = FAStateKind.AVOID;
				} else if (FAStateKind.AVOID.equals(kind)) {
					other = FAStateKind.AVOID;
				} 
				target.setAttribute(AttributeGraphFAInformer.STATE_KIND_KEY, other);
			}
		},null);
		return (T)((dfa instanceof IDFA) ? new GraphDFA(copy) : new GraphNFA(copy));
		
	}

	/** Determinizes a DFA. */
	public static IDFA determinize(INFA nfa) {
		DefaultNFADeterminizerInput input = new DefaultNFADeterminizerInput(nfa);
		DefaultNFADeterminizerResult result = new DefaultNFADeterminizerResult();
		new NFADeterminizerAlgo().execute(input,result);
		return (IDFA) result.adapt(IDFA.class);
	}
	
	/** Minimizes a DFA. */
	public static IDFA minimize(IDFA dfa) {
		DefaultDFAMinimizerInput input = new DefaultDFAMinimizerInput(dfa);
		DefaultDFAMinimizerResult result = new DefaultDFAMinimizerResult();
		new DFAMinimizerAlgo().execute(input,result);
		return (IDFA) result.adapt(IDFA.class);
	}
	
	/** Tau moves. */
	public static INFA tmoves(IDFA fa, final Object tau) {
		ITauInformer<Object> informer = new ITauInformer<Object>() {
			public boolean isEpsilon(Object letter) {
				return tau.equals(letter);
			}
		}; 
		DefaultTauRemoverInput input = new DefaultTauRemoverInput(fa,informer);
		DefaultTauRemoverResult result = new DefaultTauRemoverResult();
		// execute and returns
		new TauRemoverAlgo().execute(input,result);
		return (INFA) result.adapt(INFA.class);
	}
	
	/** Compose finite state automata. */
	public static IFAComposerResult compose(IFA[] automata) {
		// create composition as a tester
		IFAComposerInput input = new DefaultFAComposerInput(automata);
		IFAComposerResult result = new DefaultFAComposerResult();
		new FAComposerAlgo().execute(input,result);
		return result;
	}
	
}
