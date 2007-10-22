package be.uclouvain.jail.algo.graph.shortest.dsp;

import be.uclouvain.jail.graph.IDirectedGraph;

/** Algorithm input. */
public interface IDSPInput<T> {

	/** Input graph. */
	public IDirectedGraph getGraph();

	/** Root vertex. */
	public Object getRootVertex();

	/** Returns weight informer to use. */
	public IWeightInformer<T> getWeightInformer();

}