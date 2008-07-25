package be.uclouvain.jail.algo.fa.compose;

import be.uclouvain.jail.algo.utils.AbstractAlgoInput;
import be.uclouvain.jail.fa.IFA;

/**
 * Provides a default implementation of IFAComposerInput.
 * 
 * @author blambeau
 */
public class DefaultFAComposerInput extends AbstractAlgoInput 
                                 implements IFAComposerInput {

	/** FAs to compose. */
	private IFA[] fas;
	
	/** Creates an input instance. */
	public DefaultFAComposerInput(IFA...fas) {
		this.fas = fas;
	}

	/** Returns the DFAs to compose. */
	public IFA[] getFAs() {
		return fas;
	}

}
