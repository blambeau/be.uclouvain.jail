package be.uclouvain.jail.fa.utils;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

import be.uclouvain.jail.fa.IAlphabet;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.INFA;
import be.uclouvain.jail.fa.impl.GraphFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.utils.CopyTotalOrder;
import be.uclouvain.jail.graph.utils.ITotalOrder;

/**
 * Provides a useful automatic alphabet.
 * 
 * @author blambeau
 *
 * @param <T> letter type.
 */
public class AutoAlphabet<T> implements IAlphabet<T> {

	/** The letters. */
	private TreeSet<T> letters;
	
	/** Comparator for letters. */
	private Comparator<T> comparator;
	
	/** Creates an alphabet with an explicit letter comparator. */
	public AutoAlphabet(Comparator<T> comparator) {
		this.comparator = comparator == null ? this : comparator;
		this.letters = new TreeSet<T>(comparator);
	}

	/** 
	 * Creates an alphabet.
	 * 
	 * <p>By using this constructor you must ensure that the letters
	 * of the alphabet are comparable, or that you use a subclass of
	 * this one, overriding the compare method.</p>
	 */
	public AutoAlphabet() {
		this.letters = new TreeSet<T>(this);
		this.comparator = this;
	}
	
	/** Infers the alphabet from a DFA. */
	@SuppressWarnings("unchecked")
	public static <S> AutoAlphabet<S> inferAlphabet(GraphFA fa, Comparator<S> comparator) {
		AutoAlphabet<S> alphabet = new AutoAlphabet<S>(comparator);
		IDirectedGraph graph = fa.getGraph();
		for (Object edge: graph.getEdges()) {
			S letter = (S) fa.getEdgeLetter(edge);
			alphabet.addLetter(letter);
		}
		return alphabet;
	}
	
	/** Infers the alphabet from a DFA. */
	@SuppressWarnings("unchecked")
	public static <S> AutoAlphabet<S> inferAlphabet(IDFA dfa, Comparator<S> comparator) {
		AutoAlphabet<S> alphabet = new AutoAlphabet<S>(comparator);
		IDirectedGraph graph = dfa.getGraph();
		for (Object edge: graph.getEdges()) {
			S letter = (S) dfa.getEdgeLetter(edge);
			alphabet.addLetter(letter);
		}
		return alphabet;
	}
	
	/** Infers the alphabet from a NFA. */
	@SuppressWarnings("unchecked")
	public static <S> AutoAlphabet<S> inferAlphabet(INFA dfa, Comparator<S> comparator) {
		AutoAlphabet<S> alphabet = new AutoAlphabet<S>(comparator);
		IDirectedGraph graph = dfa.getGraph();
		for (Object edge: graph.getEdges()) {
			S letter = (S) dfa.getEdgeLetter(edge);
			alphabet.addLetter(letter);
		}
		return alphabet;
	}
	
	/** Adds a letter inside this alphabet. */
	public void addLetter(T letter) {
		this.letters.add(letter);
	}
	
	/** Returns the letters as a total order. */
	public ITotalOrder<T> getLetters() {
		return new CopyTotalOrder<T>(letters);
	}

	/** Returns an iterator on letters. */
	public Iterator<T> iterator() {
		return letters.iterator();
	}

	/** Compares two letters. */
	@SuppressWarnings("unchecked")
	public int compare(T o1, T o2) {
		if (comparator != null && comparator != this) {
			return comparator.compare(o1, o2);
		} else if (o1 instanceof Comparable) {
			return ((Comparable)o1).compareTo(o2);
		} else if (o2 instanceof Comparable) {
			return ((Comparable)o2).compareTo(o1);
		} else {
			throw new IllegalStateException("Letters must be comparable when no comparator is provided.");
		}
	}
	
}
