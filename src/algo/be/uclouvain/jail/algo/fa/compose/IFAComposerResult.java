package be.uclouvain.jail.algo.fa.compose;

import be.uclouvain.jail.algo.commons.Avoid;
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

	/** Returns true if this state has already been explored by 
	 * the algorithm, false otherwise. */
	public boolean isExplored(MultiFAStateGroup source);

	/** Fired when a state is explored for the first time. */
	public void exploring(MultiFAStateGroup source);

	/** 
	 * Fired when the target state is reached from source through an edge. 
	 * 
	 * <p>Source state has always been explored (exploring method has been previously called).
	 * Target state may be already explored but not necesserally.</p>  
	 * 
	 * <p>This method may throw an Avoid exception to let the algorithm know that the
	 * target state is in fact unreachable due to edge specific informations.</p>
	 * 
	 * <p>This method is expected to return !isExplored(target), that is, a boolean indicating
	 * if the target state must be further explored or not.</p> 
	 */
	public boolean reached(MultiFAStateGroup source, MultiFAEdgeGroup edge, MultiFAStateGroup target) throws Avoid;

	/** 
	 * Fired when a state has been entirely explored.
	 */
	public void endexplore(MultiFAStateGroup source);
	
}
