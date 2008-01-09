package be.uclouvain.jail.algo.graph.copy.match.functions;

import java.util.Random;

import be.uclouvain.jail.Jail;

/**
 * Function which chooses randomly from a set of values. 
 * 
 * @author blambeau
 */
public class ChooseFunction implements IGMatchFunction {

	/** Randomizer to use. */
	private Random r;

	/** Creates a choose function. */
	public ChooseFunction() {
		r = Jail.randomizer();
	}
	
	/** Executes the function. */
	public Object execute(Object...args) {
		// randomly generates an integer
		if (args == null || args.length==0) {
			return new Integer(r.nextInt());
		}
		
		// randomly chooses a value from args
		int i = r.nextInt(args.length);
		return args[i];
	}

}
