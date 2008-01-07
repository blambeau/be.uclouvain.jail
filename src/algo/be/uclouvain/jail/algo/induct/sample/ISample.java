package be.uclouvain.jail.algo.induct.sample;

import net.chefbe.javautils.adapt.IAdaptable;
import be.uclouvain.jail.fa.IAlphabet;

/**
 * Provides a marker for induction sample.
 * 
 * <p>An induction sample is typically a set of positive and 
 * negative strings on a given alphabet.</p>
 */
public interface ISample<L> extends IAdaptable, Iterable<ISampleString<L>> {

	/** Returns the sample alphabet. */
	public IAlphabet<L> getAlphabet();
	
	/** Returns the size of the sample (number of strings). */
	public int size();
	
}
