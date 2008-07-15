package be.uclouvain.jail.algo.fa.decorate;

import be.uclouvain.jail.fa.IDFA;

/**
 * Input abstraction of {@link DFADecorationAlgo}.
 * 
 * @author blambeau
 */
public interface IDFADecorationInput {

	/** Returns the DFA to decorate. */
	public IDFA getSource();

}
