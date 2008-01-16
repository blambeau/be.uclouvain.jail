package be.uclouvain.jail.fa.utils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.chefbe.javautils.adapt.AdaptUtils;
import be.uclouvain.jail.algo.fa.determinize.NFADeterminizer;
import be.uclouvain.jail.fa.IAlphabet;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.IExtensibleSample;
import be.uclouvain.jail.fa.IFlushableString;
import be.uclouvain.jail.fa.INFA;
import be.uclouvain.jail.fa.IString;
import be.uclouvain.jail.fa.impl.GraphNFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.adjacency.AdjacencyDirectedGraph;

/**
 * Provides a base implementation for samples.
 * 
 * @author blambeau
 * @param <L> type of the letters.
 */
public class DefaultSample<L> implements IExtensibleSample<L> {

	/** Alphabet to use. */
	private IAlphabet<L> alphabet;
	
	/** Sample strings. */
	private Set<IString<L>> strings;
	
	/** Creates an empty sample with an auto alphabet. */
	public DefaultSample() {
		this.alphabet = new AutoAlphabet<L>();
		this.strings = new HashSet<IString<L>>();
	}

	/** Returns the alphabet. */
	public IAlphabet<L> getAlphabet() {
		return alphabet;
	}

	/** Returns true if the sample contains a given string. */
	public boolean contains(IString<L> s) {
		return strings.contains(s);
	}
	
	/** Returns size of the sample. */
	public int size() {
		return strings.size();
	}
	
	/** Adds a sample string. */
	public void addString(IString<L> string) {
		if (alphabet instanceof AutoAlphabet) {
			for (L letter: string) {
				((AutoAlphabet<L>)alphabet).addLetter(letter);
			}
		}
		strings.add(string);
	}
	
	/** Returns an iterator on sample strings. */
	public Iterator<IString<L>> iterator() {
		return strings.iterator();
	}

	/** Fills a graph with strings. */
	private void fill(IDirectedGraph g) {
		for (IString<L> s: strings) {
			if (s instanceof IFlushableString) {
				((IFlushableString<L>)s).fill(g);
			} else {
				DefaultString<L> s2 = new DefaultString<L>(alphabet,s,s.isPositive());
				s2.fill(g);
			}
		}
	}
	
	/** Provides adaptations. */
	public <T> Object adapt(Class<T> c) {
		if (c.isAssignableFrom(getClass())) {
			return this;
		}
		 
		if (IDirectedGraph.class.equals(c)) {
			IDirectedGraph g = new AdjacencyDirectedGraph();
			fill(g);
			return g;
		} else if (INFA.class.equals(c)) {
			INFA nfa = new GraphNFA(getAlphabet());
			fill(nfa.getGraph());
			return nfa;
		} else if (IDFA.class.equals(c)) {
			INFA nfa = new GraphNFA(getAlphabet());
			fill(nfa.getGraph());
			return new NFADeterminizer(nfa).getResultingDFA();
		}
		 
		// allow external adapters to do their work
		return AdaptUtils.externalAdapt(this,c);
	}

}
