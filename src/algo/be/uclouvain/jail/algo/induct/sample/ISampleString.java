package be.uclouvain.jail.algo.induct.sample;

import net.chefbe.javautils.adapt.IAdaptable;

/**
 * Marker for sample string.
 * 
 * @author blambeau
 */
public interface ISampleString<L> extends IAdaptable, Iterable<L> {

	/** Returns size of the string (number of letters). */
	public int size();
	
	/** Returns true if the string is a positive one,
	 * that is, an example. */
	public boolean isPositive();
	
	/** Returns true if the string is a negative one,
	 * that is, a counter-example. */
	public boolean isNegative();
	
}
