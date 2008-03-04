package be.uclouvain.jail.vm.toolkits;

import be.uclouvain.jail.algo.fa.uncomplement.FAUncomplementorAlgo;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.impl.GraphDFA;
import be.uclouvain.jail.graph.utils.DirectedGraphWriter;
import be.uclouvain.jail.uinfo.UserInfoHandler;

/** Provides an facade for using algorithms. */
public class AutomatonFacade {

	/** Uncomplements a DFA. */
	public static IDFA uncomplement(IDFA dfa) {
		UserInfoHandler handler = new UserInfoHandler();
		handler.keepAll(false, true, true);
		DirectedGraphWriter writer = new DirectedGraphWriter(handler);
		new FAUncomplementorAlgo().execute(dfa,writer);
		return new GraphDFA(writer.getGraph());
	}
	
}
