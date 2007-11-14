package be.uclouvain.jail.algo.fa.utils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.utils.ITotalOrder;

/** 
 * Specialization of AbstractGroup that provides useful utilities in case
 * of state groups.
 * 
 * @author blambeau
 */
public class DFAStateGroup extends AbstractGroup {

	/** Group informer. */
	private IDFAGroupInformer informer;
	
	/** Creates a state group instance. */
	public DFAStateGroup(int[] components, IDFAGroupInformer informer) {
		super();
		this.informer = informer;
		super.setComponents(components);
	}

	/** Creates a state group instance. */
	public DFAStateGroup(Object[] components, IDFAGroupInformer informer) {
		super();
		this.informer = informer;
		super.setComponents(components);
	}
	
	/** Returns the i-th directed graph. */
	@Override
	public final IDirectedGraph getGraph(int i) {
		return getDFA(i).getGraph();
	}

	/** Returns the i-th DFA. */
	public final IDFA getDFA(int i) {
		return informer.getDFA(i);
	}

	/** Returns the i-th vertices total order. */
	@Override
	public final ITotalOrder<Object> getTotalOrder(int i) {
		return getGraph(i).getVerticesTotalOrder();
	}

	/** Returns the outgoing letters of the group. */
	public Iterator<Object> getOutgoingLetters() {
		Set<Object> letters = new HashSet<Object>();

		// iterate the states in the group
		int size = size();
		for (int i=0; i<size; i++) {
			Object state = getComponent(i);
			
			// ignore uncomplete group
			if (state == null) { continue; }
			
			// iterate outgoing letters of the current state
			for (Object letter: getDFA(i).getOutgoingLetters(state)) {
				
				// add letter
				letters.add(letter);
			}
		}
		
		// returns letters
		return letters.iterator();
	}
	
	/** Creates a group of reachable edges from this state group
	 *  through a given letter. */
	public DFAEdgeGroup delta(Object letter) {
		// loop variables
		IDFA dfa = null;
		Object state = null;
		Object edge = null;
		
		// iterate the states in the group
		int size = size();
		int[] edges = new int[size];
		for (int i=0; i<size; i++) {
			
			// retrieve outgoing edge of the current state
			dfa = getDFA(i);
			state = getComponent(i);
			edge = dfa.getOutgoingEdge(state, letter);
			
			// no such edge?
			if (edge == null) {
				if (dfa.getAlphabet().contains(letter)) {
					return null;
				} else {
					edges[i] = -1;
				}
			} else {
				// edge found, set its index
				edges[i] = getEdgesTotalOrder(i).indexOf(edge);
			}
		}
		return new DFAEdgeGroup(edges,informer);
	}

}
