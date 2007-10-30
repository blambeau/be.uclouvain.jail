package be.uclouvain.jail.algo.graph.copy.match.functions;

/** 
 * Provides a GMatch function.
 * 
 * @author blambeau
 */
public interface IGMatchFunction<T> {

	/** Executes the function on some arguments. */
	public T execute(Object...args);
	
}
