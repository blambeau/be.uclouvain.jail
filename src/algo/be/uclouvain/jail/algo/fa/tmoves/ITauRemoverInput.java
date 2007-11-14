package be.uclouvain.jail.algo.fa.tmoves;

import be.uclouvain.jail.fa.IFA;

/**
 * Abstract input of {@link ITauRemoverAlgo}.
 * 
 * @author LAMBEAU Bernard
 */
public interface ITauRemoverInput {

	/** Returns the FA from which tau-transitions must be removed. */
	public IFA getFA();

	/** Returns the tau-transition informer. */
	public ITauInformer getTauInformer();
	
}
