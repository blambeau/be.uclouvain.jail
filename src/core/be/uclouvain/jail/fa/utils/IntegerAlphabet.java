package be.uclouvain.jail.fa.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import be.uclouvain.jail.fa.IAlphabet;
import be.uclouvain.jail.fa.IString;
import be.uclouvain.jail.graph.utils.ITotalOrder;
import be.uclouvain.jail.graph.utils.ListTotalOrder;

/**
 * Provides a finite alphabet over integers.
 * 
 * <p>This alphabet contains Integer letters. Basically it contains letters
 * from 0 to size-1, but can also be used in a more general scheme by providing
 * start, offset et size fields at construction.</p>
 * 
 * TODO: IntegerAlphabet.contains() is really inefficient, but so simple like this ;-)
 * @author blambeau
 */
public class IntegerAlphabet implements IAlphabet<Integer> {

	/** Word comparator. */
	private Comparator<IString<Integer>> stringComparator;

	/** Start number, offset between numbers, number of letters. */
	private int start, offset, size;
	
	/** List of letters. */
	private List<Integer> letters;

	/** Creates an integer alphabet. */
	public IntegerAlphabet(int start, int offset, int size) {
		this.start = start;
		this.offset = offset;
		this.size = size;
	}
	
	/** Creates a default alphabet of size letters, starting at 0
	 * and with offset equal to 1. */
	public IntegerAlphabet(int size) {
		this(0,1,size);
	}

	/** Builds the letters. */
	private void buildLetters() {
		letters = new ArrayList<Integer>();
		Integer current = start;
		Integer end = start+(offset*size);
		while (current<end) {
			letters.add(current);
			current += offset;
		}
	}
	
	/** Converts letters to a word. */
	public IString<Integer> string(Iterable<Integer> letters) {
		return new DefaultString<Integer>(this,letters,true);
	}

	/** Returns true if the letter is known. */
	public boolean contains(Integer letter) {
		buildLetters();
		return letters.contains(letter);
	}

	/** Returns alphabet letters as a total order. */
	public ITotalOrder<Integer> getLetters() {
		if (letters == null) {
			buildLetters();
		}
		return new ListTotalOrder<Integer>(letters);
	}

	/** Returns a word comparator. */
	public Comparator<IString<Integer>> getStringComparator() {
		return stringComparator;
	}

	/** Compares two letters. */
	public int compare(Integer e, Integer f) {
		return e-f;
	}

	/** Returns an iterator over letters. */
	public Iterator<Integer> iterator() {
		return new Iterator<Integer>() {

			/** Next letter to return. */
			private Integer next = start;
			
			/** Has next letter? */
			public boolean hasNext() {
				return next != null;
			}

			/** Returns next letter. */
			public Integer next() {
				Integer toReturn = next;
				next += offset;
				if (next >= (start+offset*size)) {
					next = null;
				}
				return toReturn;
			}

			/** Throws a UnsupportedOperationException. */
			public void remove() {
				throw new UnsupportedOperationException("Cannot remove letters of alphabets.");
			}
			
		};
	}

}
