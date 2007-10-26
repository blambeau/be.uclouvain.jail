package be.uclouvain.jail.algo.fa.minimize;

import be.uclouvain.jail.fa.IDFA;

/**
 * Provides a default implementation of {@link IDFAMinimizerInput}.
 * 
 * @author blambeau
 */
public class DefaultDFAMinimizerInput implements IDFAMinimizerInput {

	/** DFA to minimize. */
	private IDFA dfa;

	/** Creates an input instance. */
	public DefaultDFAMinimizerInput(IDFA dfa) {
		this.dfa = dfa;
	}

	/** Returns the DFA to minimize. */
	public IDFA getDFA() {
		return dfa;
	}

}
