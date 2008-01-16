package be.uclouvain.jail.fa.utils;

import java.util.Iterator;
import java.util.NoSuchElementException;

import net.chefbe.javautils.adapt.AdaptUtils;
import be.uclouvain.jail.fa.FAStateKind;
import be.uclouvain.jail.fa.IAlphabet;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.IExtensibleSample;
import be.uclouvain.jail.fa.IFATrace;
import be.uclouvain.jail.fa.IString;
import be.uclouvain.jail.fa.constraints.PTAGraphConstraint;
import be.uclouvain.jail.graph.IDirectedGraph;

/**
 * Decorates a PTA as a sample.
 * 
 * @author blambeau
 * @param <L>
 */
public class PTASample<L> implements IExtensibleSample<L> {

	/** Decorated dfa. */
	private IDFA pta;
	
	/** Size of the PTA. */
	private int size = -1;
	
	/** Creates a sample instance. */
	public PTASample(IDFA nfa) {
		assert (new PTAGraphConstraint().isRespectedBy(nfa.getGraph())) : "Valid PTA.";
		this.pta = nfa;
	}
	
	/** Returns the sample alphabet. */
	public IAlphabet<L> getAlphabet() {
		return pta.getAlphabet();
	}

	/** Returns true if the sample contains a given string. */
	public boolean contains(IString<L> s) {
		return FAWalkUtils.walk(pta,s) != null;
	}
	
	/** Returns a depth first instance. */
	private PTADepthFirst depthFirst() {
		return new PTADepthFirst(pta);
	}
	
	/** Checks if a state is the end of a string. */
	private boolean isEndOfString(Object state) {
		FAStateKind kind = pta.getStateKind(state);
		return (FAStateKind.ACCEPTING.equals(kind) || 
			    FAStateKind.ERROR.equals(kind));
	}
	
	/** Returns number of strings. */
	public int size() {
		if (size == -1) {
			size = 0;
			PTADepthFirst df = depthFirst();
			while (df.hasNext()) {
				if (isEndOfString(df.next())) {
					size++;
				}
			}
		}
		return size;
	}

	/** Returns an iterator on strings. */
	public Iterator<IString<L>> iterator() {
		return new Iterator<IString<L>>() {
			
			/** Depth first visit. */
			private PTADepthFirst depthFirst = depthFirst();
			
			/** Next string to return. */
			private IString<L> next = findNext();
			
			/** Finds the next string. */
			private IString<L> findNext() {
				while (depthFirst.hasNext()) {
					Object next = depthFirst.next();
					if (isEndOfString(next)) {
						IFATrace<L> trace = depthFirst.flushTrace();
						return new FATraceString<L>(trace);
					}
				}
				return null;
			}
			
			/** Has a next string? */
			public boolean hasNext() {
				return next != null;
			}

			/** Returns next string. */
			public IString<L> next() {
				if (!hasNext()) { throw new NoSuchElementException(); }
				IString<L> toReturn  = next;
				next = findNext();
				return toReturn;
			}
			
			/** Throws an exception. */
			public void remove() {
				throw new UnsupportedOperationException();
			}

		};
	}

	/** Adds a sample string, by writing in the nfa. */
	public void addString(IString<L> s) {
	}

	/** Provides adaptations. */
	public <T> Object adapt(Class<T> c) {
		if (c.isAssignableFrom(getClass())) {
			return this;
		}
		
		// provide classical adaptations
		if (IDirectedGraph.class.equals(c)) {
			return pta.getGraph();
		} else if (IDFA.class.equals(c)) {
			return pta;
		}
		 
		// allow external adapters to do their work
		return AdaptUtils.externalAdapt(this,c);
	}

}
