package be.uclouvain.jail.fa.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.chefbe.javautils.adapt.AdaptUtils;
import net.chefbe.javautils.comparisons.HashCodeUtils;
import be.uclouvain.jail.fa.FAStateKind;
import be.uclouvain.jail.fa.IAlphabet;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.IFlushableString;
import be.uclouvain.jail.fa.INFA;
import be.uclouvain.jail.fa.IString;
import be.uclouvain.jail.fa.IWord;
import be.uclouvain.jail.fa.impl.AttributeGraphFAInformer;
import be.uclouvain.jail.fa.impl.GraphDFA;
import be.uclouvain.jail.fa.impl.GraphNFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.IDirectedGraphWriter;
import be.uclouvain.jail.graph.adjacency.AdjacencyDirectedGraph;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.IUserInfoHelper;
import be.uclouvain.jail.uinfo.UserInfoHelper;

/**
 * Provides a base implementation for sample strings.
 * 
 * @author blambeau
 * @param <L> type of the string letters.
 */
public class DefaultString<L> implements IFlushableString<L> {

	/** Positive string? */
	private boolean positive;
	
	/** Underlying word. */
	private IWord<L> word;

	/** Creates a string with letters. */
	public DefaultString(IAlphabet<L> alphabet, Iterable<L> letters, boolean positive) {
		this.word = new DefaultWord<L>(alphabet, letters);
		this.positive = positive;
	}
	
	/** Creates a string with letters. */
	public DefaultString(IAlphabet<L> alphabet, L[] letters, boolean positive) {
		this.word = new DefaultWord<L>(alphabet, letters);
		this.positive = positive;
	}
	
	/** Returns alphabet which generated this word. */
	public IAlphabet<L> getAlphabet() {
		return word.getAlphabet();
	}
	
	/** Returns size of the string. */
	public int size() {
		return word.size();
	}
	
	/** Returns true if the string is negative. */
	public boolean isNegative() {
		return !positive;
	}

	/** Returns true if the string is positive. */
	public boolean isPositive() {
		return positive;
	}

	/** Returns an iterator on string letters. */
	public Iterator<L> iterator() {
		return word.iterator();
	}
	
	/** Compares with another string. */
	public int compareTo(IString<L> other) {
		int c = getAlphabet().getWordComparator().compare(this, other);

		// if not equal let return c
		if (c != 0) { return c; }
		
		// otherwise, positive strings are greater
		// than negative ones
		return new Boolean(positive).compareTo(other.isPositive());
	}

	/** Compares with another word. */
	@SuppressWarnings("unchecked")
	public int compareTo(Object who) {
		if (who == this) { return 0; }
		if (who instanceof IString == false) { return 1; }
		try {
			return compareTo((IString<L>)who);
		} catch (ClassCastException ex) {
			return 1;
		}
	}
	
	/** Returns an hash code. */
	public int hashCode() {
		int hash = HashCodeUtils.SEED;
		hash = HashCodeUtils.hash(hash, word.hashCode());
		hash = HashCodeUtils.hash(hash, positive);
		return hash;
	}
	
	/** Compares with another word. */
	public boolean equals(Object who) {
		if (who == this) { return true; }
		if (who instanceof DefaultString) {
			DefaultString other = (DefaultString) who;
			return word.equals(other.word)
			    && new Boolean(positive).equals(other.isPositive());
		}
		return compareTo(who) == 0;
	}

	

	/** Factors a state info. */
	private IUserInfo sInfo(boolean initial, boolean accepting, boolean error) {
		IUserInfoHelper helper = UserInfoHelper.instance();
		helper.addKeyValue(AttributeGraphFAInformer.STATE_INITIAL_KEY, initial);
		helper.addKeyValue(AttributeGraphFAInformer.STATE_KIND_KEY, 
				FAStateKind.fromBools(accepting,error));
		return helper.install();
	}
	
	/** Creates an edge info. */
	private IUserInfo eInfo(L letter) {
		IUserInfoHelper helper = UserInfoHelper.instance();
		return helper.keyValue(AttributeGraphFAInformer.EDGE_LETTER_KEY, letter);
	}
	
	/** Fills a NFA. */
	public Object[] fill(IDirectedGraphWriter g) {
		int size = size();

		// create initial state
		IUserInfo sInfo = sInfo(true,
                               size==0 && positive,   // accepting if empty positive string
                               size==0 && !positive); // error is empty negative string
		Object current = g.createVertex(sInfo);
		
		// prepare result
		List<Object> states = new ArrayList<Object>(size+1);
		
		// create next states
		int i=1;
		for (L letter: this) {
			// add current to result
			states.add(current);
			
			// create edge and state info
			IUserInfo eInfo = eInfo(letter);
			sInfo = sInfo(false, i==size && positive, i==size && !positive);
			
			// create next state and connect
			Object next = g.createVertex(sInfo);
			g.createEdge(current, next, eInfo);
			
			// current becomes next
			current = next;
			i++;
		}
		
		return states.toArray();
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
			INFA nfa = new GraphNFA();
			fill(nfa.getGraph());
			return nfa;
		} else if (IDFA.class.equals(c)) {
			IDFA dfa = new GraphDFA();
			fill(dfa.getGraph());
			return dfa;
		}
		 
		// allow external adapters to do their work
		return AdaptUtils.externalAdapt(this,c);
	}

}
