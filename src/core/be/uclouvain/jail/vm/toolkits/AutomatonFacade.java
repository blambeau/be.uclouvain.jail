package be.uclouvain.jail.vm.toolkits;

import java.io.IOException;
import java.io.PrintWriter;

import be.uclouvain.jail.algo.fa.uncomplement.FAUncomplementorAlgo;
import be.uclouvain.jail.algo.fa.utils.FAUtils;
import be.uclouvain.jail.dialect.dot.DOTGraphDialect;
import be.uclouvain.jail.dialect.dot.JDotty;
import be.uclouvain.jail.fa.FAStateKind;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.IFA;
import be.uclouvain.jail.fa.impl.AttributeGraphFAInformer;
import be.uclouvain.jail.fa.impl.GraphDFA;
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
	public static void debug(IFA fa) throws IOException {
		PrintWriter w = new PrintWriter(System.out);
		new DOTGraphDialect().print(fa.getGraph(), w, null);
		w.flush();
	}
	
	/** Uncomplements a DFA. */
	public static IDFA uncomplement(IDFA dfa) {
		UserInfoHandler handler = new UserInfoHandler();
		handler.keepAll(false, true, true);
		DirectedGraphWriter writer = new DirectedGraphWriter(handler);
		new FAUncomplementorAlgo().execute(dfa,writer);
		return new GraphDFA(writer.getGraph());
	}
	
	/** Deep copies a DFA. */
	public static IDFA copy(IDFA dfa, IUserInfoPopulator<IUserInfo> vertex, IUserInfoPopulator<IUserInfo> edge) {
		IDirectedGraph g = dfa.getGraph();
		IDirectedGraph copy = GraphFacade.copy(g, vertex, edge);
		return new GraphDFA(copy);
	}
	
	/** Copies a DFA marking all states of acceptation. */
	public static IDFA allaccepting(IDFA dfa) {
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
		return new GraphDFA(copy);
		
	}
	
}
