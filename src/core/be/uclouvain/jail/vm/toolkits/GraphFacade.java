package be.uclouvain.jail.vm.toolkits;

import java.io.IOException;
import java.io.PrintWriter;

import be.uclouvain.jail.algo.graph.decorate.DefaultIdentifyAlgoInput;
import be.uclouvain.jail.algo.graph.decorate.IdentifyAlgo;
import be.uclouvain.jail.dialect.dot.DOTGraphDialect;
import be.uclouvain.jail.graph.IDirectedGraph;

/**
 * Provides a graph facade.
 * 
 * @author blambeau
 */
public final class GraphFacade {

	/** Debugs an automaton using dot. */
	public static void debug(IDirectedGraph g) throws IOException {
		PrintWriter w = new PrintWriter(System.out);
		new DOTGraphDialect().print(g, w, null);
		w.flush();
	}
	
	/** Identifies a graph. */
	public static IDirectedGraph identify(IDirectedGraph g, String vAttr, String eAttr) {
		DefaultIdentifyAlgoInput input = new DefaultIdentifyAlgoInput(g);
		input.setEdgeAttr(eAttr);
		input.setVertexAttr(vAttr);
		new IdentifyAlgo().execute(input);
		return g;
	}
	
}
