package be.uclouvain.jail.fa;

import net.chefbe.javautils.adapt.IAdaptable;

/**
 * Provides a word abstraction.
 */
public interface IWord<T> extends IAdaptable, Iterable<T>, Comparable {

	/** Returns alphabet which generated this word. */
	public IAlphabet<T> getAlphabet();
	
	/** Returns size of the word. */
	public int size();
	
}
