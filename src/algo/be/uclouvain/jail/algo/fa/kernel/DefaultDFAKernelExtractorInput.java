package be.uclouvain.jail.algo.fa.kernel;

import be.uclouvain.jail.fa.IDFA;

/**
 * Default implementation of {@link IDFAKernelExtractorInput}.
 * 
 * @author blambeau
 */
public class DefaultDFAKernelExtractorInput implements IDFAKernelExtractorInput {

	/** Input dfa. */
	private IDFA dfa;
	
	/** Creates an input instance. */
	public DefaultDFAKernelExtractorInput(IDFA dfa) {
		this.dfa = dfa;
	}

	/** Returns the DFA. */
	public IDFA getDFA() {
		return dfa;
	}

}
