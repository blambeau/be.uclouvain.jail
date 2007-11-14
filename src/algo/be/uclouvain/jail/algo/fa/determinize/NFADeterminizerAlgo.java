package be.uclouvain.jail.algo.fa.determinize;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import be.uclouvain.jail.algo.fa.utils.FAEdgeGroup;
import be.uclouvain.jail.algo.fa.utils.FAStateGroup;
import be.uclouvain.jail.fa.INFA;

/**
 * Standard NDA determinization algorithm.
 * 
 * <p>This algorithm computes a DFA which is equivalent to a source NFA.</p>
 * 
 * @author LAMBEAU Bernard
 */
public class NFADeterminizerAlgo {

	/* ---------------------------------------------------------------------------------------------- Fields */
	/** Target states to explore. */
	private Set<FAStateGroup> toExplore;

	/** Explored states. */
	private Set<FAStateGroup> explored;

	/** Non deterministic automaton to determinize. */
	private INFA nfa;
	
	/** Creates an algorithm instance. */
	public NFADeterminizerAlgo() {
	}

	/** Returns true when there is target state defnitions to explore. */
	protected boolean hasNext() {
		return !toExplore.isEmpty();
	}

	/**
	 * Returns a target state definition to explore.
	 * 
	 * <p>This method removes a target state definition of the toExplore list and mark it 
	 * as explored.</p>
	 */
	private FAStateGroup getOne() {
		/* get one target definition */
		FAStateGroup sources = toExplore.iterator().next();
		toExplore.remove(sources);

		/* mark as explored */
		explored.add(sources);

		return sources;
	}

	/** Checks if a target state definition has been explored. */
	private boolean isAlreadyFound(FAStateGroup def) {
		return explored.contains(def) || toExplore.contains(def);
	}

	/** 
	 * Creates a new target state definition.
	 * 
	 * @param def the definition of the target states (a set of source states).
	 */
	private void createOne(FAStateGroup def) {
		toExplore.add(def);
	}
	
	/** Main method of the algo */
	@SuppressWarnings("unchecked")
	protected void main(INFADeterminizerInput input, INFADeterminizerResult result) {
		// initialize algorithm
		this.nfa = input.getNFA();
		toExplore = new HashSet<FAStateGroup>();
		explored = new HashSet<FAStateGroup>();
		
		/* algo started event */
		result.started(input);

		/* create one for initial states of the NFA */
		FAStateGroup initStates = new FAStateGroup(nfa);
		for (Object init: nfa.getInitialStates()) {
			initStates.addState(init);
		}
		createOne(initStates);

		/* main loop ... while target state created and not explored */
		while (hasNext()) {

			/* get the definition of the target state (a set of source states) */
			FAStateGroup sources = getOne();

			/* iterate interresting alphabet letters */
			Iterator letters = sources.getOutgoingLetters();
			while (letters.hasNext()) {
				Object letter = letters.next();
				
				// find reachable edges and states
				FAEdgeGroup outEdges = sources.delta(letter);
				FAStateGroup targets = outEdges.getTargetStateGroup();

				// create state
				if (!isAlreadyFound(targets)) {
					createOne(targets);
				}

				// create transitions
				result.createTargetTransitions(sources, targets, outEdges);
			}
		}

		result.ended();
	}

	/** Executes the determinization. */
	public void execute(INFADeterminizerInput input, INFADeterminizerResult result) {
		main(input,result);
	}
	
}
