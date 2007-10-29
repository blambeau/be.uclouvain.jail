package be.uclouvain.jail.algo.fa.compose;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import be.uclouvain.jail.fa.IDFA;

/**
 * Standard algorithm for DFA composition.
 * 
 * @author blambeau
 */
public class DFAComposerAlgo implements IDFAGroupInformer {

	/** Created states. */
	private Map<DFAStateGroup,Object> explored;
	
	/** Input DFAs. */
	private IDFA[] dfas;
	
	/** Algorithm result. */
	private IDFAComposerResult result;
	
	/** Returns the i-th DFA. */
	public IDFA getDFA(int i) {
		return dfas[i];
	}

	/** Checks if a state has been explored. */
	private boolean isExplored(DFAStateGroup state) {
		return explored.containsKey(state);
	}
	
	/** Marks a state definition as explored. */ 
	private void explored(DFAStateGroup state, Object target) {
		explored.put(state,target);
	}
	
	/** Explore some states. */
	private void explore(DFAStateGroup state) {
		if (explored.containsKey(state)) {
			throw new AssertionError("State not already explored.");
		}
		
		// create the target state
		Object target = result.createState(state);
		//System.out.println("Exploring " + state);
		
		// mark as explored
		explored(state,target);
		
		// explore it
		Iterator letters = state.getOutgoingLetters();
		while (letters.hasNext()) {
			// find outgoing edges for this letter
			Object letter = letters.next();
			DFAEdgeGroup delta = state.delta(letter);
			
			// synchronization blocked by one DFA ?
			if (delta == null) { 
				continue; 
			}
			
			// let's go to the new target state
			DFAStateGroup goesto = delta.getTargetStateGroup(state);
			
			//System.out.print("With letter " + letter + " goes to " + goesto);
			
			// recurse when new state found.
			if (!isExplored(goesto)) {
				//System.out.println(" ... not yet explored");
				explore(goesto);
			} else {
				//System.out.println(" ... already done!");
			}
			
			// create target edge
			Object created = explored.get(goesto);
			result.createEdge(target, created, delta);
		}
	}
	
	/** Executes the algorithm. */
	public void execute(IDFAComposerInput input, IDFAComposerResult result) {
		this.result = result;
		this.dfas = input.getDFAs();
		this.explored = new HashMap<DFAStateGroup,Object>();
		
		// create init state
		int size = this.dfas.length;
		Object init[] = new Object[size];
		for (int i=0; i<size; i++) {
			init[i] = dfas[i].getInitialState();
		}
		DFAStateGroup initState = new DFAStateGroup(init,this);
		
		// explore it
		explore(initState);
	}
	
}
