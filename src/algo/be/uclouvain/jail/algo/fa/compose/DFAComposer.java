package be.uclouvain.jail.algo.fa.compose;

import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.impl.GraphDFA;

/**
 * Provides a user-friendly API on top of the composer algorithm.
 * 
 * @author blambeau
 */
public class DFAComposer {

	/** Algorithm input. */ 
	private IDFAComposerInput input;
	
	/** Algorithm output. */ 
	private IDFAComposerResult result;

	/** Algorithm executed? */
	private boolean executed = false;
	
	/** Creates a determinizer instance for a NFA. */
	public DFAComposer(IDFA...dfas) {
		input = new DefaultDFAComposerInput(dfas);
		result = new DefaultDFAComposerResult(new GraphDFA());
	}
	
	/** Returns the resulting DFA. */
	public IDFA getResultingDFA() {
		if (!executed) { execute(); }
		return result.getResultingDFA();
	}
	
	/** Executes the algorithm. */
	private void execute() {
		new DFAComposerAlgo().execute(input, result);
		executed = true;
	}
	
}
