package be.uclouvain.jail.algo.induct.sample;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.chefbe.javautils.adapt.AdaptUtils;
import be.uclouvain.jail.fa.FAStateKind;
import be.uclouvain.jail.fa.INFA;
import be.uclouvain.jail.fa.impl.AttributeGraphFAInformer;
import be.uclouvain.jail.fa.impl.GraphNFA;
import be.uclouvain.jail.graph.IDirectedGraph;
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
public class DefaultSampleString<L> implements IFAAwareString<L> {

	/** Positive string? */
	private boolean positive;
	
	/** String letters. */
	private List<L> letters;

	/** Creates a string with letters. */
	public DefaultSampleString(boolean positive, List<L> letters) {
		this.letters = letters;
		this.positive = positive;
	}
	
	/** Creates a string with letters. */
	public DefaultSampleString(boolean positive, L...letters) {
		this.letters = new ArrayList<L>();
		for (L letter: letters) {
			this.letters.add(letter);
		}
		this.positive = positive;
	}
	
	/** Copy constructor. */
	public DefaultSampleString(ISampleString<L> copy) {
		this.letters = new ArrayList<L>();
		for (L letter: copy) {
			this.letters.add(letter);
		}
		this.positive = copy.isPositive();
	}
	
	/** Returns size of the string. */
	public int size() {
		return letters.size();
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
		return letters.iterator();
	}

	/** Factors a state info. */
	private IUserInfo sInfo(boolean initial, boolean accepting, boolean error) {
		IUserInfoHelper helper = UserInfoHelper.instance();
		helper.addKeyValue(AttributeGraphFAInformer.STATE_INITIAL_KEY, initial);
		helper.addKeyValue(AttributeGraphFAInformer.STATE_KIND_KEY, FAStateKind.fromBools(accepting,error));
		return helper.install();
	}
	
	/** Creates an edge info. */
	private IUserInfo eInfo(L letter) {
		IUserInfoHelper helper = UserInfoHelper.instance();
		return helper.keyValue(AttributeGraphFAInformer.EDGE_LETTER_KEY, letter);
	}
	
	/** Fills a NFA. */
	public void fill(IDirectedGraph g) {
		int size = size();

		// create initial state
		IUserInfo sInfo = sInfo(true,
                               size==0 && positive,   // accepting if empty positive string
                               size==0 && !positive); // error is empty negative string
		Object current = g.createVertex(sInfo);
		
		// create next states
		for (int i=1; i<=size; i++) {
			IUserInfo eInfo = eInfo(letters.get(i-1));
			sInfo = sInfo(false, i==size && positive, i==size && !positive);
			
			// create next state and connect
			Object next = g.createVertex(sInfo);
			g.createEdge(current, next, eInfo);
			
			// current becomes next
			current = next;
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
			INFA nfa = new GraphNFA();
			fill(nfa.getGraph());
			return nfa;
		}
		 
		// allow external adapters to do their work
		return AdaptUtils.externalAdapt(this,c);
	}

}
