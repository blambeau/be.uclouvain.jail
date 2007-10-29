package be.uclouvain.jail.algo.fa.compose;

import be.uclouvain.jail.fa.IDFA;

/** 
 * Abstracts input of the Composer.
 * 
 * @author blambeau
 */
public interface IDFAComposerInput {

	/** Returns automata to compose. */
	public IDFA[] getDFAs();
	
}
