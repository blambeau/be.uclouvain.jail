package be.uclouvain.jail.fa;

import be.uclouvain.jail.graph.IDirectedGraphWriter;

/**
 * Extension of IString for those that are able to participate
 * in DFA and NFA construction.
 * 
 * @author blambeau
 * @param <L> type of the string letters.
 */
public interface IFlushableString<L> extends IString<L> {

	/** Fills a graph. */
	public Object[] fill(IDirectedGraphWriter g);
	
}
