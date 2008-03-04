package be.uclouvain.jail.algo.induct.listeners;

import be.uclouvain.jail.algo.induct.internal.Fringe;
import be.uclouvain.jail.algo.induct.internal.PTAEdge;
import be.uclouvain.jail.algo.induct.internal.PTAState;

/**
 * Maintains the fringe.
 * 
 * @author blambeau
 */
public class FringeMaintainer extends InductionAlgoListenerAdapter {

	/** The fringe to maintain. */
	private Fringe fringe;

	/** Removes the edge from the fringe. */
	public void consolidate(PTAEdge edge) {
		// TODO: find source kernel state
		Object letter = edge.letter();
		Object skState = null;
		fringe.remove(skState, letter);
	}

	/** Add children to the fringe. */
	public void consolidate(PTAState state) {
		// TODO: find source kernel state
		// update fringe
		Object skState = null;
		for (PTAEdge edge : state.outEdges()) {
			fringe.add(skState, edge);
		}
	}

}
