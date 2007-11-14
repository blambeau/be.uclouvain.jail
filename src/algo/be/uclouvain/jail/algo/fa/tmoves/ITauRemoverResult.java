package be.uclouvain.jail.algo.fa.tmoves;

import be.uclouvain.jail.adapt.IAdaptable;
import be.uclouvain.jail.algo.fa.utils.FAEdgeGroup;
import be.uclouvain.jail.algo.fa.utils.FAStateGroup;

/**
 * Abstract output of the tau-remover algorithm.
 * 
 * <p>Pseudo code of the algorithm as seen by this abstraction is described in 
 * {@link TauRemoverAlgo} javadoc documentation.</p>
 * 
 * @author LAMBEAU Bernard
 */
public interface ITauRemoverResult extends IAdaptable {

	/** "Algorithm started" event. */
	public void started(ITauRemoverInput input);

	/** "Algorithm ended" event. */
	public void ended();

	/** 
	 * Creates transitions in the resulting NFA. 
	 *
	 * @param source source state of the input NFA.
	 * @param targets target states of the input NFA.
	 * @param edges edges that merge.
	 */
	public void createTargetTransitions(Object source, FAStateGroup targets, FAEdgeGroup edges);

}
