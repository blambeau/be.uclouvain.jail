package be.uclouvain.jail.algo.graph.copy.match.functions;

import be.uclouvain.jail.uinfo.functions.OnFirstPopulator;

/**
 * GMatch version of {@link OnFirstPopulator}.
 * 
 * @author blambeau
 */
public class ExistsFunction implements IGMatchFunction {

	/** Returns arg[0] on first, arg[1] otherwise. */
	public Object execute(Object... args) {
		if (args == null || args.length==0) {
			return false;
		}
		for (Object arg: args) {
			if (arg != null) { 
				return true;
			}
		}
		return false;
	}

}
