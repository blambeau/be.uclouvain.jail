package be.uclouvain.jail.algo.fa.tmoves;

import java.util.Iterator;

import be.uclouvain.jail.algo.fa.utils.FAEdgeGroup;
import be.uclouvain.jail.algo.fa.utils.FAStateGroup;
import be.uclouvain.jail.fa.IFA;
import be.uclouvain.jail.graph.IDirectedGraph;

/**
 * <p>Standard e-moves remover algorithm. This algorithm computes the equivalent NFA of 
 * a source FA without any epsilon transition.</p>
 * 
 * @author LAMBEAU Bernard
 */
public final class TauRemoverAlgo {

	/* ---------------------------------------------------------------------------------------------- Fields */
	/** Source FA from which the tau-transitions must be removed. */
	private IFA fa;

	/** Underlying graph. */
	private IDirectedGraph graph;
	
	/** Epsilon informer. */
	private ITauInformer<Object> epsilon;

	/* ---------------------------------------------------------------------------------------------- Construction */
	/** Creates an algorithm instance. */
	public TauRemoverAlgo() {
	}

	/** Computes the closure of a single state. */
	private void eclosure(Object state, FAStateGroup closure) {
		closure.addState(state);
		
		// look at outgoing edges
		for (Object outEdge: graph.getOutgoingEdges(state)) {
			// bypass non epsilon letters
			Object letter = fa.getEdgeLetter(outEdge);
			if (!epsilon.isEpsilon(letter)) {
				continue;
			}
			
			// get edge target
			Object target = graph.getEdgeTarget(outEdge);
			
			// add to the set of reachable states and recurse if 
			// required
			if (!closure.contains(target)) {
				// recurse on that state
				eclosure(target,closure);
			}
		}
	}
	
	/** Computes the closure of a group of states. */
	private FAStateGroup eclosure(FAStateGroup group) {
		FAStateGroup closure = new FAStateGroup(fa); 
		for (Object state: group) {
			if (!closure.contains(state)) {
				eclosure(state,closure);
			}
		}
		return closure;
	}
	
	/** Computes the closure of a single state. */
	private FAStateGroup eclosure(Object state) {
		FAStateGroup closure = new FAStateGroup(fa);
		eclosure(state,closure);
		return closure;
	}
	
	/* ---------------------------------------------------------------------------------------------- Main */
	/**
	 * Executes the e-moves algorithm on a given source and result.
	 * 
	 * @param source a NFA with epsilon moves transitions.
	 * @param algorithmResult a result to construct.
	 */
	@SuppressWarnings("unchecked")
	private void main(ITauRemoverInput input, ITauRemoverResult result) {
		this.fa = input.getFA();
		this.graph = fa.getGraph();
		this.epsilon = input.getTauInformer();

		/* started event to the result */
		result.started(input);

		/* for each state */
		for (Object sourceState : graph.getVertices()) {

			// compute closure of the state
			FAStateGroup sources = eclosure(sourceState);

			/* iterate interresting alphabet letters */
			Iterator<Object> letters = sources.getOutgoingLetters();
			while (letters.hasNext()) {
				Object letter = letters.next();
				
				/* bypass epsilon */
				if (epsilon.isEpsilon(letter)) {
					continue;
				}

				// find reachable edges and states
				FAEdgeGroup outEdges = sources.delta(letter);
				FAStateGroup targets = eclosure(outEdges.getTargetStateGroup());
				
				/* create transitions */
				result.createTargetTransitions(sourceState, targets, outEdges);
			}
		}

		result.ended();
	}

	/** Executes the algorithm. */
	public void execute(ITauRemoverInput input, ITauRemoverResult result) {
		main(input,result);
	}
	
}
