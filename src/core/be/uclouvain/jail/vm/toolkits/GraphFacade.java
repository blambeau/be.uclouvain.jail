package be.uclouvain.jail.vm.toolkits;

import be.uclouvain.jail.algo.graph.decorate.DefaultIdentifyAlgoInput;
import be.uclouvain.jail.algo.graph.decorate.IdentifyAlgo;
import be.uclouvain.jail.graph.IDirectedGraph;

/**
 * Provides a graph facade.
 * 
 * @author blambeau
 */
public final class GraphFacade {

	/** Identifies a graph. */
	public static IDirectedGraph identify(IDirectedGraph g, String vAttr, String eAttr) {
		DefaultIdentifyAlgoInput input = new DefaultIdentifyAlgoInput(g);
		input.setEdgeAttr(eAttr);
		input.setVertexAttr(vAttr);
		new IdentifyAlgo().execute(input);
		return g;
	}
	
}
