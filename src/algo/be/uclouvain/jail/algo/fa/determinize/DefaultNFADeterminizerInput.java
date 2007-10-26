package be.uclouvain.jail.algo.fa.determinize;

import be.uclouvain.jail.fa.INFA;

/**
 * Provides a default implementation of {@link INFADeterminizerInput}.
 * 
 * @author blambeau
 */
public class DefaultNFADeterminizerInput implements INFADeterminizerInput {

	/** NFA to determinize. */
	private INFA nfa;
	
	/** Creates an input instance. */
	public DefaultNFADeterminizerInput(INFA nfa) {
		this.nfa = nfa;
	}

	/** Returns the NFA to determinize. */
	public INFA getNFA() {
		return nfa;
	}

}
