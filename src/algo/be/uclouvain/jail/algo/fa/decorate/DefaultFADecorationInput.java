package be.uclouvain.jail.algo.fa.decorate;

import be.uclouvain.jail.algo.utils.AbstractAlgoInput;
import be.uclouvain.jail.fa.IFA;

/**
 * Default implementation of {@link IFADecorationInput}.
 * 
 * @author blambeau
 */
public class DefaultFADecorationInput extends AbstractAlgoInput 
                                       implements IFADecorationInput {

	/** Source to decorate. */
	protected IFA source;
	
	/** Creates an input instance. */
	public DefaultFADecorationInput(IFA source) {
		if (source == null) { throw new IllegalArgumentException("Source DFA cannot be null"); }
		this.source = source;
	}

	/** Returns the DFA to decorate. */
	public IFA getSource() {
		return source;
	}

}
