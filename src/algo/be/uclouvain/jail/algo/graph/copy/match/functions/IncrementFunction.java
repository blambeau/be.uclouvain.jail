package be.uclouvain.jail.algo.graph.copy.match.functions;

/**
 * Provides natural increment.
 * 
 * @author blambeau
 */
public class IncrementFunction implements IGMatchFunction {

	/** Next identifier. */
	private int i = 0;
	
	/** Return next i. */
	public Object execute(Object... args) {
		return i++;
	}

}
