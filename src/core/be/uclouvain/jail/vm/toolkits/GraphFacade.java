package be.uclouvain.jail.vm.toolkits;

import java.io.IOException;
import java.io.PrintWriter;

import be.uclouvain.jail.algo.graph.copy.DirectedGraphCopier;
import be.uclouvain.jail.algo.graph.decorate.DefaultIdentifyAlgoInput;
import be.uclouvain.jail.algo.graph.decorate.IdentifyAlgo;
import be.uclouvain.jail.dialect.dot.DOTGraphDialect;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.utils.DirectedGraphWriter;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.IUserInfoHandler;
import be.uclouvain.jail.uinfo.IUserInfoPopulator;
import be.uclouvain.jail.uinfo.UserInfoHandler;

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
	
	/** Copies a graph. */
	public static IDirectedGraph copy(
			IDirectedGraph g, 
			IUserInfoPopulator<IUserInfo> vertex,
			IUserInfoPopulator<IUserInfo> edge) {

		// create a writer
		IUserInfoHandler handler = new UserInfoHandler(); 
		DirectedGraphWriter writer = new DirectedGraphWriter(handler);
		handler.getVertexCopier().keepAll();
		handler.getEdgeCopier().keepAll();
		if (vertex != null) {
			handler.getVertexCopier().addPopulator(vertex);
		}
		if (edge != null) {
			handler.getEdgeCopier().addPopulator(edge);
		}
		// copy the graph
		DirectedGraphCopier.copy(g, writer);
		
		return writer.getGraph();
	}
	
}
