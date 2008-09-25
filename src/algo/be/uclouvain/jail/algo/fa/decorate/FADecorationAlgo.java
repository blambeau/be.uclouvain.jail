package be.uclouvain.jail.algo.fa.decorate;

import java.util.HashSet;

import be.uclouvain.jail.fa.IFA;
import be.uclouvain.jail.graph.IDirectedGraph;

/**
 * Decorates a DFA.
 * 
 * @author blambeau
 */
public class FADecorationAlgo {

	/** States to be explored. */
	private HashSet<Object> toExplore;
	
	/** Result companion. */
	private IFADecorationResult result;
	
	/** DFA to decorate. */
	private IFA dfa;
	
	/** Underlying graph. */
	private IDirectedGraph g;
	
	/** Mark a state as "to be explored". */
	private void markAsToExplore(Object target) {
		if (!toExplore.contains(target)) {
			toExplore.add(target);
		}
	}
	
	/** Propagates decoration of a state. */
	private void propagate(Object source) {
		for (Object edge: g.getOutgoingEdges(source)) {
			Object target = g.getEdgeTarget(edge);
			if (result.propagate(source,edge,target)) {
				markAsToExplore(target);
			}
		}
	}

	/** Initialize algorithm, decorating states with 
	 * default values. */
	private void initialize() {
		for (Object state: g.getVertices()) {
			boolean toBeExplored = result.initDeco(state, dfa.isInitial(state));
			if (toBeExplored) {
				markAsToExplore(state);
			}
		}
	}
	
	/** Main loop, try to empty toExplore. */
	private void mainLoop() {
		while (!toExplore.isEmpty()) {
			Object source = toExplore.iterator().next();
			toExplore.remove(source);
			propagate(source);
		}
	}
	
	/** Executes the algorithm. */
	public void execute(IFADecorationInput input, IFADecorationResult result) {
		// initialize instance variables
		this.result = result;
		this.toExplore = new HashSet<Object>();
		this.dfa = input.getSource();
		this.g = this.dfa.getGraph();
		
		// start algo
		result.started(input);
		initialize();
		mainLoop();
		result.ended();
	}
	
}
