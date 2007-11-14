package be.uclouvain.jail.algo.fa.compose;

import be.uclouvain.jail.algo.fa.utils.MultiDFAEdgeGroup;
import be.uclouvain.jail.algo.fa.utils.MultiDFAStateGroup;
import be.uclouvain.jail.fa.IDFA;

/**
 * Abstracts result of the DFAComposer.
 * 
 * @author blambeau
 */
public interface IDFAComposerResult {

	/** Creates a state. */
	public Object createState(MultiDFAStateGroup sources);
	
	/** Creates an edge. */
	public Object createEdge(Object source, Object target, MultiDFAEdgeGroup edges);
	
	/** Returns the resulting DFA. */
	public IDFA getResultingDFA();
	
}
