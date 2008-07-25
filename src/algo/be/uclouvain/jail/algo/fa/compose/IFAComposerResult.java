package be.uclouvain.jail.algo.fa.compose;

import be.uclouvain.jail.algo.commons.IAlgoResult;
import be.uclouvain.jail.algo.fa.utils.MultiFAEdgeGroup;
import be.uclouvain.jail.algo.fa.utils.MultiFAStateGroup;

/**
 * Abstracts result of the DFAComposer.
 * 
 * @author blambeau
 */
public interface IFAComposerResult extends IAlgoResult {

	/** Fired when algorithm starts. */
	public void started(IFAComposerInput input);
	
	/** Fired when algorithm ends. */
	public void ended();
	
	/** Creates a state. */
	public void stateFound(MultiFAStateGroup state);
	
	/** Creates an edge. */
	public void stateReached(MultiFAStateGroup source, MultiFAEdgeGroup edge, MultiFAStateGroup target);
	
}
