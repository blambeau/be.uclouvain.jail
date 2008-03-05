package be.uclouvain.jail.vm.toolkits;

import java.io.IOException;

import be.uclouvain.jail.algo.fa.uncomplement.FAUncomplementorAlgo;
import be.uclouvain.jail.algo.fa.utils.FAUtils;
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
	
	/** Uncomplements a DFA. */
	public static IDFA uncomplement(IDFA dfa) {
		UserInfoHandler handler = new UserInfoHandler();
		handler.keepAll(false, true, true);
		DirectedGraphWriter writer = new DirectedGraphWriter(handler);
		new FAUncomplementorAlgo().execute(dfa,writer);
		return new GraphDFA(writer.getGraph());
	}
	
}
