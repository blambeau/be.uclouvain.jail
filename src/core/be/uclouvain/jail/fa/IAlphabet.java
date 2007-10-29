package be.uclouvain.jail.fa;

import java.util.Comparator;

import be.uclouvain.jail.graph.utils.ITotalOrder;

/**
 * Provides an finite automaton alphabet abstraction.
 * 
 * @author blambeau
 */
public interface IAlphabet<L> extends Comparator<L>, Iterable<L> {

	/** Returns true if the letter is known by its alphabet. */
	public boolean contains(L letter);
	
	/** Returns the letters as a total order. */
	public ITotalOrder<L> getLetters();
	
}
