package be.uclouvain.jail.algo.fa.determinize;

import be.uclouvain.jail.fa.INFA;

/**
 * Abstracts the input of the determinization algorithm.
 * 
 * @author blambeau
 */
public interface INFADeterminizerInput {

	/** Returns the NFA to determinize. */
	public INFA getNFA();
	
}
