package be.uclouvain.jail.algo.fa.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.chefbe.javautils.collections.arrays.ArrayUtils;
import net.chefbe.javautils.collections.arrays.ArrayUtils.IArrayExploder;
import be.uclouvain.jail.algo.commons.Avoid;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.IFA;
import be.uclouvain.jail.fa.INFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.utils.ITotalOrder;

/** 
 * Specialization of AbstractGroup that provides useful utilities in case
 * of state groups.
 * 
 * @author blambeau
 */
public class MultiFAStateGroup extends AbstractMultiGroup {

	/** Group informer. */
	private IMultiFAGroupInformer informer;
	
	/** Creates a state group instance. */
	public MultiFAStateGroup(int[] components, IMultiFAGroupInformer informer) {
		super();
		this.informer = informer;
		super.setComponents(components);
	}

	/** Creates a state group instance. */
	public MultiFAStateGroup(Object[] components, IMultiFAGroupInformer informer) {
		super();
		this.informer = informer;
		super.setComponents(components);
	}
	
	/** Returns the i-th directed graph. */
	@Override
	public final IDirectedGraph getGraph(int i) {
		return getFA(i).getGraph();
	}

	/** Returns the i-th FA. */
	public final IFA getFA(int i) {
		return informer.getFA(i);
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
			for (Object letter: getFA(i).getOutgoingLetters(state)) {
				// add letter
				letters.add(letter);
			}
		}
		
		// returns letters
		return letters.iterator();
	}
	
	/** Computes a delta function. */
	public List<MultiFAEdgeGroup> delta(Object letter) {
		final List<MultiFAEdgeGroup> edges = new ArrayList<MultiFAEdgeGroup>();

		// compute delta
		int size = size();
		Object[][] outs = new Object[size][];
		for (int i=0; i<size; i++) {
			outs[i] = delta(i, letter);
		}
		
		// explode it
		ArrayUtils.explode(outs, new IArrayExploder() {
			public void group(Object[] group) {
				edges.add(new MultiFAEdgeGroup(group, informer));
			}
		});
		
		// return edge groups
		return edges;
	}

	/** Delta function on a single FA. */
	private Object[] delta(int i, Object letter) {
		Object state = getComponent(i);
		IFA fa = getFA(i);
		boolean knows = fa.getAlphabet().contains(letter);
		if (fa instanceof IDFA) {
			IDFA dfa = (IDFA) fa;
			Object edge = dfa.getOutgoingEdge(state, letter);

			// edge not found but in alphabet ? avoid it !
			if (edge == null && knows) throw new Avoid();

			return new Object[]{edge};
		} else if (fa instanceof INFA) {
			INFA nfa = (INFA) fa;
			Object[] edges = nfa.getOutgoingEdges(state, letter).toArray();
			
			// edges not found but in alphabet ? avoid it
			if (edges.length == 0 && knows) throw new Avoid();
			return edges.length == 0 ? new Object[]{null} : edges;
		} else {
			throw new IllegalStateException("Not a DFA nor a NFA");
		}
	}
	
}
