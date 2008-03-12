package be.uclouvain.jail.fa.utils;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

import be.uclouvain.jail.fa.IAlphabet;
import be.uclouvain.jail.fa.IString;
import be.uclouvain.jail.graph.utils.CopyTotalOrder;
import be.uclouvain.jail.graph.utils.ITotalOrder;

/** 
 * An alphabet for which letters are provided at construction.
 */ 
public class StaticAlphabet<L extends Comparable<L>> implements IAlphabet<L> {

	/** Letters. */
	private TreeSet<L> letters;
	
	/** Creates an alphabet instance. */
	public StaticAlphabet(L...ls) {
		this.letters = new TreeSet<L>();
		for (L l: ls) {
			letters.add(l);
		}
	}
	
	/** Contains a given letter? */
	public boolean contains(L letter) {
		return letters.contains(letter);
	}

	/** Returns letters. */
	public ITotalOrder<L> getLetters() {
		return new CopyTotalOrder<L>(letters);
	}

	/** Returns a string comparator. */
	public Comparator<IString<L>> getStringComparator() {
		return new StringComparator<L>(this);
	}

	/** Converts an iterable of letters to a string. */
	public IString<L> string(Iterable<L> letters) {
		return new DefaultString<L>(this,letters,true);
	}

	/** Compares two letters. */
	public int compare(L arg0, L arg1) {
		return arg0.compareTo(arg1);
	}

	/** Returns an iterator on letters. */
	public Iterator<L> iterator() {
		return letters.iterator();
	}

}
