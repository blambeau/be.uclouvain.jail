package be.uclouvain.jail.algo.fa.compose;

import be.uclouvain.jail.fa.IDFA;

/**
 * Provides a default implementation of IDFAComposerInput.
 * 
 * @author blambeau
 */
public class DefaultDFAComposerInput implements IDFAComposerInput {

	/** DFAs to compose. */
	private IDFA[] dfas;
	
	/** Creates an input instance. */
	public DefaultDFAComposerInput(IDFA...dfas) {
		this.dfas = dfas;
	}

	/** Returns the DFAs to compose. */
	public IDFA[] getDFAs() {
		return dfas;
	}

}
