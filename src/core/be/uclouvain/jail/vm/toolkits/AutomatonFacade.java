package be.uclouvain.jail.vm.toolkits;

import java.io.IOException;
import java.io.PrintWriter;

import be.uclouvain.jail.algo.fa.uncomplement.FAUncomplementorAlgo;
import be.uclouvain.jail.algo.fa.utils.FAUtils;
import be.uclouvain.jail.dialect.dot.DOTGraphDialect;
import be.uclouvain.jail.dialect.dot.JDotty;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.IFA;
import be.uclouvain.jail.fa.impl.GraphDFA;
import be.uclouvain.jail.graph.utils.DirectedGraphWriter;
import be.uclouvain.jail.uinfo.UserInfoHandler;

/** Provides an facade for using algorithms. */
public final class AutomatonFacade {

	/** Shows a FA using JDotty. 
	 * @throws IOException */
	public static void show(IFA fa) throws IOException {
		JDotty jdotty = new JDotty();
		jdotty.present(FAUtils.copyForDot(fa),null);
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
	
}
