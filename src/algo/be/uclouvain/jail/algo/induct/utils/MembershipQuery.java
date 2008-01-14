package be.uclouvain.jail.algo.induct.utils;

import java.util.Iterator;
import java.util.NoSuchElementException;

import be.uclouvain.jail.fa.FAStateKind;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.IDFATrace;
import be.uclouvain.jail.graph.IDirectedGraphPath;

/**
 * Provides a membership query.
 * 
 * @author blambeau
 */
public class MembershipQuery<T> implements Iterable<T> {

	/** Query prefix. */
	private IDFATrace<T> prefix;

	/** Query suffix. */
	private IDFATrace<T> suffix;

	/** Creates a query instance. */
	public MembershipQuery(IDFATrace<T> prefix, IDFATrace<T> suffix) {
		this.prefix = prefix;
		this.suffix = suffix;
	}
	
	/** Returns the prefix. */
	public IDFATrace<T> prefix() {
		return prefix;
	}
	
	/** Returns the suffix. */
	public IDFATrace<T> suffix() {
		return suffix;
	}

	/** Returns query size, as number of letters. */
	public int size() {
		return prefix.getGraphPath().size()+suffix.getGraphPath().size();
	}
	
	/** Checks if the query is a positive one. */
	public boolean isPositive() {
		IDFA dfa = suffix.getDFA();
		IDirectedGraphPath path = suffix.getGraphPath();
		Object endState = path.getLastVertex();
		return FAStateKind.ACCEPTING.equals(dfa.getStateKind(endState));
	}
	
	/** Returns a string representation. */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (T letter: prefix) {
			sb.append(letter).append(" ");
		}
		sb.append(" | ");
		for (T letter: suffix) {
			sb.append(letter).append(" ");
		}
		return sb.toString();
	}
	
	/** Returns an iterator on letters. */
	public Iterator<T> iterator() {
		return new Iterator<T>() {

			/** On first iterator? */
			private boolean onFirst;
			
			/** Current iterator. */
			private Iterator<T> it = it();
			
			/** Find the iterator to use. */
			private Iterator<T> it() {
				Iterator<T> it = prefix.iterator();
				onFirst = it.hasNext();
				return onFirst ? it : suffix.iterator();
			}
			
			/** Checks if a next letter exists. */
			public boolean hasNext() {
				return (it != null) && (it.hasNext());
			}

			/** Returns next letter. */
			public T next() {
				if (!hasNext()) { throw new NoSuchElementException(); }
				
				// take next element
				T next = it.next();
				
				// if no more but on first, switch iterator
				if (!it.hasNext() && onFirst) {
					it = suffix.iterator();
					onFirst = false;
				}
				
				return next;
			}

			/** Throws Unsupported. */
			public void remove() {
				throw new UnsupportedOperationException();
			}
			
		};
	}
	
}
