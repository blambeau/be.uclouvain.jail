package be.uclouvain.jail.algo.induct.listeners;

import be.uclouvain.jail.algo.induct.internal.IWork;
import be.uclouvain.jail.algo.induct.internal.InductionAlgo;
import be.uclouvain.jail.algo.induct.internal.PTAEdge;
import be.uclouvain.jail.algo.induct.internal.PTAState;
import be.uclouvain.jail.algo.induct.internal.Simulation;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.impl.GraphDFA;
import be.uclouvain.jail.graph.IDirectedGraph;

/**
 * Creates a DFA during the algorithm.
 * 
 * @author blambeau
 */
public class DFACreationListener extends InductionAlgoListenerAdapter {

	/** DFA under creation. */
	private IDFA dfa;
	
	/** Graph writer. */
	private IDirectedGraph dfag;
	
	/** Initializes the listener. */
	public void initialize(InductionAlgo algo) {
		this.dfa = new GraphDFA();
		this.dfag = dfa.getGraph();
	}
	
	/** Consolidates an edge. */
	public void consolidate(PTAEdge edge) {
		// find corresponding source and target
		PTAState source = edge.source();
		PTAState target = edge.target();
		Object kSource = source.retrieve(this);
		Object kTarget = target.retrieve(this);

		// create edge
		Object kEdge = dfag.createEdge(kSource, kTarget, edge.getUserInfo());
		
		// keep mapping with the created edge
		edge.keep(this,kEdge);
	}

	/** Consolidates an edge. */
	public void consolidate(PTAState state) {
		// create state
		Object kState = dfag.createVertex(state.getUserInfo());
		
		// keep mapping with that state
		state.keep(this, kState);
	}

	/** Starts a try. */
	public void startTry(PTAEdge edge, Object kState) {
		// find corresponding source and target
		PTAState source = edge.source();
		Object kSource = source.retrieve(this);
		Object kTarget = kState;

		// create edge
		Object kEdge = dfag.createEdge(kSource, kTarget, edge.getUserInfo());
		
		// keep mapping with that state
		edge.keep(this,kEdge);
	}
	
	/** Commits the try. */
	public void commit(Simulation simu) {
		if (simu.isRealTry()) {
			// retrieve sour and target
			IWork work = simu.getStartTry();
			PTAEdge fEdge = (PTAEdge) work.victim();
			PTAState source = fEdge.source();
			Object kSource = source.retrieve(this);
			Object kTarget = work.target();

			// check if the edge has been removed
			Object kEdge = fEdge.retrieve(this);
			if (kEdge == null) {
				// replay!
				kEdge = dfag.createEdge(kSource, kTarget, fEdge.getUserInfo());
			}
		}
	}

	/** Rollbacks the try. */
	public void rollback(Simulation simu) {
		if (simu.isRealTry()) {
			// retrieve created kernel edge
			IWork work = simu.getStartTry();
			PTAEdge fEdge = (PTAEdge) work.victim();
			Object kEdge = fEdge.retrieve(this);

			// remove it from the DFA
			dfag.removeEdge(kEdge);
			
			// remove it from mapping
			fEdge.forget(this);
		}
	}

}
