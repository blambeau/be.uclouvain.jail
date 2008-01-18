package be.uclouvain.jail.fa;

import java.util.Comparator;

import be.uclouvain.jail.graph.utils.ITotalOrder;

/**
 * Provides a finite automaton alphabet abstraction.
 * 
 * <p>An alphabet is basically a finite set of letters. However, Jail
 * makes the assumption that alphabet letters are totally ordered. This is
 * the reason why an alphabet extends the Comparator interface and provides
 * a way to get a TotalOrder.</p>   
 * 
 * <p>This interface may be implemented by users. However, in many cases, 
 * alphabets already provided by Jail makes the job. Take a look on AutoAlphabet
 * specifically before implementing it.</p> 
 * 
 * @author blambeau
 */
public interface IAlphabet<L> extends Comparator<L>, Iterable<L> {

	/** Converts an iterable of letters to a string. */
	public IString<L> string(Iterable<L> letters);
	
	/** Returns a comparator for words. */
	public Comparator<IString<L>> getStringComparator();
	
	/** Returns true if the letter is known by its alphabet. */
	public boolean contains(L letter);
	
	/** Returns the letters as a total order. */
	public ITotalOrder<L> getLetters();
	
}
