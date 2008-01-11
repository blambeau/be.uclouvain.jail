package be.uclouvain.jail.common;

/**
 * Provides a predicate.
 * 
 * @author blambeau
 * @param <T> type of tested values.
 */
public interface IPredicate<T> {

	/** Evaluates the predicate. */
	public boolean evaluate(T value);
	
}
