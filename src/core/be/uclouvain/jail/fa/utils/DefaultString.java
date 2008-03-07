package be.uclouvain.jail.fa.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.chefbe.javautils.adapt.AdaptUtils;
import net.chefbe.javautils.comparisons.HashCodeUtils;
import be.uclouvain.jail.fa.IAlphabet;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.IFATrace;
import be.uclouvain.jail.fa.IString;
import be.uclouvain.jail.fa.IWalkInfo;

/**
 * Provides a base implementation for sample strings.
 * 
 * @author blambeau
 * @param <L> type of the string letters.
 */
public class DefaultString<T> implements IString<T> {

	/** Alphabet. */
	private IAlphabet<T> alphabet;
	
	/** List of letters. */
	private List<T> letters;
	
	/** Is positive? */
	private Boolean isPositive;
	
	/** Creates a word instance. */
	public DefaultString(IAlphabet<T> alphabet, List<T> letters, boolean positive) {
		this.alphabet = alphabet;
		this.letters = letters;
		this.isPositive = positive;
	}
	
	/** Creates a word instance. */
	public DefaultString(IAlphabet<T> alphabet, T[] letters, boolean positive) {
		this(alphabet,toList(letters),positive);
	}
	
	/** Creates a word from iterable letters. */
	public DefaultString(IAlphabet<T> alphabet, Iterable<T> letters, boolean positive) {
		this(alphabet,toList(letters),positive);
	}
	
	/** Builds a list from an iterable. */
	private static final <X> List<X> toList(Iterable<X> it) {
		List<X> list = new ArrayList<X>();
		for (X x: it) { list.add(x); }
		return list;
	}

	/** Builds a list from an iterable. */
	private static final <X> List<X> toList(X[] it) {
		List<X> list = new ArrayList<X>();
		for (X x: it) { list.add(x); }
		return list;
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

	/** Is positive? */
	public boolean isPositive() {
		return isPositive;
	}

	/** Creates a substring. */
	public IString<T> subString(int start, int length) {
		List<T> letters = new ArrayList<T>();
		for (int i=start; i<start+length; i++) {
			letters.add(this.letters.get(i));
		}
		return new DefaultString<T>(alphabet,letters,isPositive);
	}

	/** Walks a DFA. */
	public IWalkInfo<T> walk(IDFA fa) {
		return FAWalkUtils.stringWalk(fa, this);
	}

	/** Compares with another word. */
	public int compareTo(IString<T> other) {
		return alphabet.getStringComparator().compare(this, other);
	}
	
	/** Compares with another word. */
	@SuppressWarnings("unchecked")
	public int compareTo(Object who) {
		if (who == this) { return 0; }
		if (who instanceof IString == false) { return 1; }
		try { return compareTo((IString<T>)who); } 
		catch (ClassCastException ex) { return 1; }
	}

	/** Computes the hash code. */
	public int hashCode() {
		int hash = HashCodeUtils.SEED;
		hash = HashCodeUtils.hash(hash, letters.hashCode());
		hash = HashCodeUtils.hash(hash, isPositive);
		return hash;
	}
	
	/** Compares with another word. */
	public boolean equals(Object who) {
		if (who == this) { return true; }
		if (who instanceof DefaultString) {
			DefaultString other = (DefaultString) who;
			return letters.equals(other.letters) &&
			       isPositive.equals(other.isPositive);
		}
		return compareTo(who) == 0;
	}

	/** Returns a string representation. */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(isPositive() ? "+ " : "- ");
		int i=0;
		for (T letter: this) {
			if (i++ != 0) { sb.append(" "); }
			sb.append(letter);
		}
		return sb.toString();
	}
	
	/** Adapts this trace to some type. */
	public <S> Object adapt(Class<S> c) {
		if (c.isAssignableFrom(this.getClass())) {
			return this;
		}
		if (IFATrace.class.equals(c)) {
			return String2DFA.toTrace(this);
		}
		return AdaptUtils.externalAdapt(this,c);
	}

}
