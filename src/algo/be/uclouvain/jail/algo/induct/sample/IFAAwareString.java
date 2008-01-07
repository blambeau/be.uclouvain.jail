package be.uclouvain.jail.algo.induct.sample;

import be.uclouvain.jail.graph.IDirectedGraph;

/**
 * Extension of ISampleString for those that are able to participate
 * in DFA and NFA construction.
 * 
 * @author blambeau
 * @param <L> type of the string letters.
 */
public interface IFAAwareString<L> extends ISampleString<L> {

	/** Fills a graph. */
	public void fill(IDirectedGraph g);
	
}
