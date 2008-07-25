package be.uclouvain.jail.algo.fa.compose;

import be.uclouvain.jail.algo.commons.IAlgoInput;
import be.uclouvain.jail.fa.IFA;

/** 
 * Abstracts input of the Composer.
 * 
 * @author blambeau
 */
public interface IFAComposerInput extends IAlgoInput {

	/** Returns automata to compose. */
	public IFA[] getFAs();
	
}
