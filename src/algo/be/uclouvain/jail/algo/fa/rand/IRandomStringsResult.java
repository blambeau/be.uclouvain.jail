package be.uclouvain.jail.algo.fa.rand;

import be.uclouvain.jail.algo.commons.IAlgoResult;
import be.uclouvain.jail.fa.IString;

/**
 * Result of IRandomStringsAlgo. 
 */
public interface IRandomStringsResult<L> extends IAlgoResult {

	/** Fired when algo is started. */
	public void started(IRandomStringsInput<L> input);
	
	/** Fired when algo is ended. */
	public void ended();
	
	/** Returns the number of words in result. */
	public int size();
	
	/** Adds a word inside the result. */
	public void addString(IString<L> word);
	
}
