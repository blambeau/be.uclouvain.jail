package be.uclouvain.jail.fa;

import be.uclouvain.jail.uinfo.IUserInfoHandler;

/**
 * Provides information about acceptation of a string by a FA.
 * 
 * @author blambeau
 */
public interface IWalkInfo<L> {

	/** Returns true if the word is fully accepted 
	 * by the DFA. */
	public boolean isFullyIncluded();
	
	/** Returns true if the word is fully rejected 
	 * by the DFA. */
	public boolean isFullyExcluded();
	
	/** Returns the accepted part. */
	public IFATrace<L> getIncludedPart();
	
	/** Returns rejected word. */
	public IString<L> getExcludedString();

	/** Returns a normalized string using the 
	 * given handler. */
	public IFATrace<L> normalize(IUserInfoHandler handler);
	
}
