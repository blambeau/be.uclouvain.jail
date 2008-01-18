package be.uclouvain.jail.fa.utils;

import java.util.Comparator;
import java.util.Iterator;

import be.uclouvain.jail.fa.IAlphabet;
import be.uclouvain.jail.fa.IString;

/**
 * Provides a comparator for words.
 * 
 * @author blambeau
 * @param <L>
 */
public class StringComparator<T> implements Comparator<IString<T>> {

	/** Alphabet. */
	private IAlphabet<T> alphabet;
	
	/** Creates a comparator instance. */
	public StringComparator(IAlphabet<T> alphabet) {
		this.alphabet = alphabet;
	}
	
	/** Compares two words. */
	public int compare(IString<T> o1, IString<T> o2) {
		Iterator<T> letters = o1.iterator();
		Iterator<T> oletters = o2.iterator(); 
		
		// compare letters
		while (letters.hasNext()) {
			
			// I've got a letter, other word no
			// all previous letters were equal
			// I'm longer, then greater
			if (!oletters.hasNext()) {
				return 1;
			}
			
			// compare next letters
			T letter = letters.next();
			T oletter = oletters.next();
			int c = alphabet.compare(letter, oletter);
			if (c != 0) { return c; }
		}
		
		// Other have letters yet?
		// all previous letters were equal, then I'm smaller
		// otherwise we are equal
		if (oletters.hasNext()) {
			return -1;
		} else {
			return new Boolean(o1.isPositive()).compareTo(o2.isPositive());
		}
	}

}
