package be.uclouvain.jail.fa.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.chefbe.javautils.adapt.AdaptUtils;
import be.uclouvain.jail.fa.IAlphabet;
import be.uclouvain.jail.fa.IWord;

/** Word class. */
public final class DefaultWord<T> implements IWord<T> {
	
	/** Alphabet. */
	private IAlphabet<T> alphabet;
	
	/** List of letters. */
	private List<T> letters;
	
	/** Computed has code. */
	private int hash;
	
	/** Creates a word instance. */
	public DefaultWord(IAlphabet<T> alphabet, List<T> letters) {
		this.alphabet = alphabet;
		this.letters = letters;
		this.hash = letters.hashCode();
	}
	
	/** Creates a word instance. */
	public DefaultWord(IAlphabet<T> alphabet, T[] letters) {
		this.alphabet = alphabet;
		this.letters = new ArrayList<T>();
		for (T letter: letters) {
			this.letters.add(letter);
		}
		this.hash = letters.hashCode();
	}
	
	/** Creates a word from iterable letters. */
	public DefaultWord(IAlphabet<T> alphabet, Iterable<T> letters) {
		this.alphabet = alphabet;
		this.letters = null;
		if (letters instanceof List) {
			this.letters = (List<T>) letters;
			this.hash = this.letters.hashCode();
		} else {
			this.letters = new ArrayList<T>();
			for (T letter: letters) {
				this.letters.add(letter);
			}
			this.hash = this.letters.hashCode();
		}
	}
	
	/** Returns alphabet which generated this word. */
	public IAlphabet<T> getAlphabet() {
		return alphabet;
	}
	
	/** Returns word size. */
	public int size() {
		return letters.size();
	}
	
	/** Returns an iterator on letters. */
	public Iterator<T> iterator() {
		return letters.iterator();
	}

	/** Compares with another word. */
	public int compareTo(IWord<T> other) {
		return alphabet.getWordComparator().compare(this, other);
	}
	
	/** Compares with another word. */
	@SuppressWarnings("unchecked")
	public int compareTo(Object who) {
		if (who == this) { return 0; }
		if (who instanceof IWord == false) { return 1; }
		try {
			return compareTo((IWord<T>)who);
		} catch (ClassCastException ex) {
			return 1;
		}
	}
	
	/** Returns an hash code. */
	public int hashCode() {
		return hash;
	}
	
	/** Compares with another word. */
	public boolean equals(Object who) {
		if (who == this) { return true; }
		if (who instanceof DefaultWord) {
			return letters.equals(((DefaultWord)who).letters);
		}
		return compareTo(who) == 0;
	}

	/** Adapts this trace to some type. */
	public <S> Object adapt(Class<S> c) {
		if (c.isAssignableFrom(this.getClass())) {
			return this;
		}
		return AdaptUtils.externalAdapt(this,c);
	}
	
}