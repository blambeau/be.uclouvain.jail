package be.uclouvain.jail.algo.graph.copy.match.functions;

/** 
 * Concatenates toString of the arguments.
 * 
 * @author blambeau
 */
public class ConcatFunction implements IGMatchFunction<String> {

	/** Concatenates arguments. */
	public String execute(Object...args) {
		StringBuffer sb = new StringBuffer();
		if (args != null) {
			for (Object arg: args) {
				sb.append(arg == null ? "" : arg.toString());
			}
		}
		return sb.toString();
	}

}
