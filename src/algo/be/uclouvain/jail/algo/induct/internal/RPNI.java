package be.uclouvain.jail.algo.induct.internal;

import be.uclouvain.jail.algo.commons.Avoid;
import be.uclouvain.jail.algo.commons.Restart;
import be.uclouvain.jail.graph.IDirectedGraph;

/** RPNI induction algorithm. */
public class RPNI extends InductionAlgo {

	/** Creates an algorithm instance. */
	public RPNI() {
	}

	/** Main RPNI loop. */
	protected void mainLoop() throws Restart {
		Simulation simu = null;
		
		// while fringe is not empty
		IDirectedGraph dfag = dfa.getGraph();
		while (!fringe.isEmpty()) {
			// select an edge to merge
			PTAEdge fEdge = selectFringeEdge();

			// find target kernel state (try-error)
			boolean found = false;
			for (Object kState: dfag.getVertices()) {
				// rapid check, avoid costly operations if known as incompatible
				if (!isCompatible(kState, fEdge)) {
					continue;
				}
				
				// try merge
				try {
					simu = new Simulation(this, fEdge, kState);
					fEdge.simulate(this, simu);
					checkWithOracle(simu);
					simu.commit();
					
					// alright, take it!
					found = true;
					break;
				} catch (Avoid avoid) {
					simu.rollback();
				}
			}
			
			// consolidate fringe state when all incompatible
			if (!found) {
				fEdge.consolidate(this);
			}
		}
	}

	/** Selects a fringe edge to use for merging. */
	protected PTAEdge selectFringeEdge() {
		return (PTAEdge) fringe.iterator().next();
	}
	
}
