package be.uclouvain.jail.algo.graph.connex;

import be.uclouvain.jail.graph.IDirectedGraph;

/** Default implementation of IGraphConXDetectorInput. */
public class DefaultGraphConXDetectorInput implements IGraphConXDetectorInput {

	/** Input graph. */
	private IDirectedGraph graph;
	
	/** Creates an input instance. */
	public DefaultGraphConXDetectorInput(IDirectedGraph graph) {
		this.graph = graph;
	}

	/** Returns the input graph. */
	public IDirectedGraph getGraph() {
		return graph;
	}

}
