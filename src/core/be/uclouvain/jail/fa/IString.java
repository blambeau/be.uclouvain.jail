package be.uclouvain.jail.fa;

import net.chefbe.javautils.adapt.IAdaptable;

/**
 * Marker for sample string.
 * 
 * @author blambeau
 */
public interface IString<L> extends IAdaptable, Iterable<L>, Comparable {

	/** Returns alphabet which generated this word. */
	public IAlphabet<L> getAlphabet();
	
	/** Returns size of the word. */
	public int size();
	
	/** Returns true if the string is a positive one,
	 * that is, an example. */
	public boolean isPositive();
	
	/** Returns a substring. */
	public IString<L> subString(int start, int length);
	
	/** Walks a FA and returns walk information. */
	public IWalkInfo<L> walk(IDFA fa);
	
}
