package be.uclouvain.jail.algo.graph.copy.match.functions;

import be.uclouvain.jail.uinfo.functions.OnFirstPopulator;

/**
 * GMatch version of {@link OnFirstPopulator}.
 * 
 * @author blambeau
 */
public class OnFirstFunction implements IGMatchFunction {

	/** First call? */
	private boolean first = true;
	
	/** Returns arg[0] on first, arg[1] otherwise. */
	public Object execute(Object... args) {
		if (args == null || args.length==0) {
			throw new IllegalArgumentException("onFirst function takes at least one argument");
		}
		
		Object value = null;
		if (first) {
			value = args[0];
		} else if (args.length>1) {
			value = args[1];
		} 
		first = false;
		return value;
	}

}
