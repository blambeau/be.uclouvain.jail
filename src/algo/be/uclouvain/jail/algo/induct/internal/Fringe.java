package be.uclouvain.jail.algo.induct.internal;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.graph.utils.ITotalOrder;

/** 
 * Provides a utility to use the fringe, i.e. blue states candidate
 * for merging. 
 *
 * <p>The fringe is basically a collection of edges. These edges are in
 * fact (kState,letter) pairs mapped to PTAEdge.</p> 
 */
public class Fringe implements Iterable<PTAEdge> {

	/** Induction algorithm. */
	private InductionAlgo algo;

	/** DFA under construction. */
	private IDFA dfa;

	/** Works. */
	private Map<SLPair,PTAEdge> works;

	/** Creates a fringe instance. */
	public Fringe(InductionAlgo algo) {
		this.algo = algo;
		this.dfa = algo.getDFA();

		// install the fringe collection
		final ITotalOrder<Object> states = dfa.getGraph().getVerticesTotalOrder();
		works = new TreeMap<SLPair,PTAEdge>(new Comparator<SLPair>() {
			@SuppressWarnings("unchecked")
			public int compare(SLPair k, SLPair l) {
				int kIndex = states.indexOf(k.kState());
				int lIndex = states.indexOf(l.kState());
				if (kIndex != lIndex) {
					return kIndex - lIndex;
				} else {
					return dfa.getAlphabet().compare(k.letter(), l.letter());
				}
			}

		});
	}

	/** Returns the edge on the fringe that can be reached using 
	 * a letter outside a kernel state. */
	public PTAEdge fringeEdge(Object kState, Object letter) {
		return works.get(new SLPair(kState, letter));
	}

	/** Adds an edge on the fringe. */
	public void add(Object kState, PTAEdge edge) {
		if (kState == null || edge == null) {
			throw new IllegalArgumentException("All params mandatory.");
		}
		
		// create pair
		Object letter = edge.letter();
		SLPair pair = new SLPair(kState, letter);
		
		// some checks
		assert (algo.getDFA().getOutgoingEdge(kState, letter) == null) : "kState does not have that letter yet.";
		assert (!works.containsKey(pair)) : "Fringe does not contains the pair.";
		
		// create new fringe info
		works.put(pair, edge);
		edge.setSourceKernelState(kState);
	}

	/** Removes a fringe component. */
	public void remove(Object kState, Object letter) {
		SLPair pair = new SLPair(kState, letter);
		assert (works.containsKey(pair)) : "Removes an existing edge.";
		works.remove(pair);
	}

	/** Checks if the fringe is empty. */
	public boolean isEmpty() {
		return works.isEmpty();
	}

	/** Iterates the finrge. */
	public Iterator<PTAEdge> iterator() {
		return works.values().iterator();
	}

}
