package be.uclouvain.jail.algo.fa.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.IFA;
import be.uclouvain.jail.fa.INFA;

/** 
 * Provides an implementation of states group. 
 * 
 * @author blambeau
 */
public class FAStateGroup extends FAAbstractGroup {

	/** Creates an empty group instance. */
	public FAStateGroup(IFA fa) {
		super(fa,fa.getGraph().getVerticesTotalOrder());
	}
	
	/** Adds a state in the group. */
	public void addState(Object state) {
		super.addComponent(state);
	}
	
	/** Adds some states in the group. */
	public void addStates(Object...states) {
		super.addComponents(states);
	}
	
	/** Adds some states in the group. */
	public void addStates(Collection<Object> states) {
		super.addComponents(states);
	}
	
	/** Returns an iterator on outgoing letters of the group. */
	public Iterator<Object> getOutgoingLetters() {
		Set<Object> letters = new TreeSet<Object>(fa.getAlphabet());

		// iterate the states in the group
		for (Object state: this) {
			// ignore uncomplete group
			if (state == null) { continue; }
			
			// iterate outgoing letters of the current state
			for (Object letter: fa.getOutgoingLetters(state)) {
				// add letter
				letters.add(letter);
			}
		}
		
		// returns letters
		return letters.iterator();
	}
	
	/** Returns outgoing edges labeled by letter of some state. */
	private void addOutgoingEdges(FAEdgeGroup group, Object state, Object letter) {
		if (fa instanceof IDFA) {
			Object edge = ((IDFA)fa).getOutgoingEdge(state, letter);
			if (edge != null) {
				group.addEdge(edge);
			}
		} else if (fa instanceof INFA) {
			Collection<Object> edges = ((INFA)fa).getOutgoingEdges(state, letter);
			if (edges != null && !edges.isEmpty()) {
				group.addEdges(edges);
			}
		} else {
			for (Object edge: fa.getGraph().getOutgoingEdges(state)) {
				if (letter.equals(fa.getEdgeLetter(edge))) {
					group.addEdge(edge);
				}
			}
		}
	}
	
	/** Creates an edge group for some letters. */
	public FAEdgeGroup delta(Object letter) {
		FAEdgeGroup edges = new FAEdgeGroup(fa);
		for (Object state: this) {
			addOutgoingEdges(edges,state,letter);
		}
		return edges;
	}
	
}
