package be.uclouvain.jail.algo.fa.determinize;

import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.INFA;

/**
 * Provides a user-friendly API on top of the determinizer algorithm.
 * 
 * @author blambeau
 */
public class NFADeterminizer {

	/** Algorithm input. */ 
	private INFADeterminizerInput input;
	
	/** Algorithm output. */ 
	private INFADeterminizerResult result;

	/** Algorithm executed? */
	private boolean executed = false;
	
	/** Creates a determinizer instance for a NFA. */
	public NFADeterminizer(INFA nfa) {
		input = new DefaultNFADeterminizerInput(nfa);
		result = new DefaultNFADeterminizerResult();
	}
	
	/** Returns the resulting DFA. */
	public IDFA getResultingDFA() {
		if (!executed) { execute(); }
		return result.getResultingDFA();
	}
	
	/** Executes the algorithm. */
	private void execute() {
		new NFADeterminizerAlgo().execute(input, result);
		executed = true;
	}
	
}
