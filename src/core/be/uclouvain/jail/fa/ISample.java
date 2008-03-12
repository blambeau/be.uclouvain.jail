package be.uclouvain.jail.fa;

import net.chefbe.javautils.adapt.IAdaptable;
import be.uclouvain.jail.uinfo.IUserInfoHandler;

/**
 * Provides a marker for induction sample.
 * 
 * <p>An induction sample is typically a set of positive and 
 * negative strings on a given alphabet.</p>
 */
public interface ISample<L> extends IAdaptable, Iterable<IString<L>> {

	/** Returns used handler. */
	public IUserInfoHandler getUserInfoHandler();
	
	/** Returns the sample alphabet. */
	public IAlphabet<L> getAlphabet();
	
	/** Returns the size of the sample (number of strings). */
	public int size();
	
	/** Checks if the sample contains the given string. */
	public boolean contains(IString<L> s);
	
	/** Walks a string inside the sample. */
	public IWalkInfo walk(IString<L> s);
	
	/** Adds a sample string. 
	 * Returns true if the string is a new one, false otherwise. */
	public boolean addString(IString<L> string);

}
