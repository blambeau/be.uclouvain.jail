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
	
	/** Walks a string inside the sample. */
	public IWalkInfo walk(IString<L> s);
	
	/** Returns the size of the sample (number of strings). */
	public int size();
	
	/** Adds a sample string. */
	public void addString(IString<L> string);

}
