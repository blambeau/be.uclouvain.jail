package be.uclouvain.jail.fa;

/**
 * Marker for sample string.
 * 
 * @author blambeau
 */
public interface IString<L> extends IWord<L> {

	/** Returns true if the string is a positive one,
	 * that is, an example. */
	public boolean isPositive();
	
}
