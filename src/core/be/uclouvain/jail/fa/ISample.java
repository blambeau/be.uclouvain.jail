package be.uclouvain.jail.fa;

import net.chefbe.javautils.adapt.IAdaptable;

/**
 * Provides a marker for induction sample.
 * 
 * <p>An induction sample is typically a set of positive and 
 * negative strings on a given alphabet.</p>
 */
public interface ISample<L> extends IAdaptable, Iterable<IString<L>> {

	/** Returns the sample alphabet. */
	public IAlphabet<L> getAlphabet();
	
	/** Returns true if the sample contains a given string. */
	public boolean contains(IString<L> s);
	
	/** Returns the size of the sample (number of strings). */
	public int size();
	
}
