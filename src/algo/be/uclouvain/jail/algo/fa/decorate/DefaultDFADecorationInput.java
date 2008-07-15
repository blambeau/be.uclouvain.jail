package be.uclouvain.jail.algo.fa.decorate;

import be.uclouvain.jail.algo.utils.AbstractAlgoInput;
import be.uclouvain.jail.fa.IDFA;

/**
 * Default implementation of {@link IDFADecorationInput}.
 * 
 * @author blambeau
 */
public class DefaultDFADecorationInput extends AbstractAlgoInput 
                                       implements IDFADecorationInput {

	/** Source to decorate. */
	protected IDFA source;
	
	/** Creates an input instance. */
	public DefaultDFADecorationInput(IDFA source) {
		if (source == null) { throw new IllegalArgumentException("Source DFA cannot be null"); }
		this.source = source;
	}

	/** Returns the DFA to decorate. */
	public IDFA getSource() {
		return source;
	}

}
