package be.uclouvain.jail.algo.fa.tmoves;

import be.uclouvain.jail.fa.IDFA;

/**
 * Abstract input of {@link ITauRemoverAlgo}.
 * 
 * @author LAMBEAU Bernard
 */
public interface ITauRemoverInput {

	/** Returns the DFA from which tau-transitions must be removed. */
	public IDFA getDFA();

	/** Returns the tau-transition informer. */
	public ITauInformer getTauInformer();
	
}
