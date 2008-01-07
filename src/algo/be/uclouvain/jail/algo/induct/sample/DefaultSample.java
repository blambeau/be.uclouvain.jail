package be.uclouvain.jail.algo.induct.sample;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.chefbe.javautils.adapt.AdaptUtils;
import be.uclouvain.jail.algo.fa.determinize.NFADeterminizer;
import be.uclouvain.jail.fa.IAlphabet;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.INFA;
import be.uclouvain.jail.fa.impl.GraphNFA;
import be.uclouvain.jail.fa.utils.AutoAlphabet;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.adjacency.AdjacencyDirectedGraph;

/**
 * Provides a base implementation for samples.
 * 
 * @author blambeau
 * @param <L> type of the letters.
 */
public class DefaultSample<L> implements ISample<L> {

	/** Alphabet to use. */
	private IAlphabet<L> alphabet;
	
	/** Sample strings. */
	private List<ISampleString<L>> strings;
	
	/** Creates an empty sample with an auto alphabet. */
	public DefaultSample() {
		this.alphabet = new AutoAlphabet<L>();
		this.strings = new ArrayList<ISampleString<L>>();
	}

	/** Returns the alphabet. */
	public IAlphabet<L> getAlphabet() {
		return alphabet;
	}

	/** Returns size of the sample. */
	public int size() {
		return strings.size();
	}
	
	/** Adds a sample string. */
	public void addSampleString(ISampleString<L> string) {
		if (alphabet instanceof AutoAlphabet) {
			for (L letter: string) {
				((AutoAlphabet<L>)alphabet).addLetter(letter);
			}
		}
		strings.add(string);
	}
	
	/** Returns an iterator on sample strings. */
	public Iterator<ISampleString<L>> iterator() {
		return strings.iterator();
	}

	/** Fills a graph with strings. */
	private void fill(IDirectedGraph g) {
		for (ISampleString<L> s: strings) {
			if (s instanceof IFAAwareString) {
				((IFAAwareString<L>)s).fill(g);
			} else {
				DefaultSampleString<L> s2 = new DefaultSampleString<L>(s);
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
