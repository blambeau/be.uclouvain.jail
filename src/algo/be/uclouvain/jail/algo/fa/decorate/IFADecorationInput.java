package be.uclouvain.jail.algo.fa.decorate;

import be.uclouvain.jail.fa.IFA;

/**
 * Input abstraction of {@link FADecorationAlgo}.
 * 
 * @author blambeau
 */
public interface IFADecorationInput {

	/** Returns the DFA to decorate. */
	public IFA getSource();

}
