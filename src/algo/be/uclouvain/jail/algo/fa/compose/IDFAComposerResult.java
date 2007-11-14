package be.uclouvain.jail.algo.fa.compose;

import be.uclouvain.jail.algo.fa.utils.DFAEdgeGroup;
import be.uclouvain.jail.algo.fa.utils.DFAStateGroup;
import be.uclouvain.jail.fa.IDFA;

/**
 * Abstracts result of the DFAComposer.
 * 
 * @author blambeau
 */
public interface IDFAComposerResult {

	/** Creates a state. */
	public Object createState(DFAStateGroup sources);
	
	/** Creates an edge. */
	public Object createEdge(Object source, Object target, DFAEdgeGroup edges);
	
	/** Returns the resulting DFA. */
	public IDFA getResultingDFA();
	
}
