package be.uclouvain.jail.algo.fa.tmoves;

import be.uclouvain.jail.fa.IFA;

/**
 * Provides a default implementation of {@link ITauRemoverInput}.
 * 
 * @author blambeau
 */
public class DefaultTauRemoverInput implements ITauRemoverInput {

	/** FA with tau transitions. */
	private IFA fa;
	
	/** Tau-transition informer. */
	private ITauInformer informer;
	
	/** Creates an input instance. */
	public DefaultTauRemoverInput(IFA fa, ITauInformer informer) {
		this.fa = fa;
		this.informer = informer;
	}

	/** Returns the input fa. */
	public IFA getFA() {
		return fa;
	}

	/** Returns tau informer. */
	public ITauInformer getTauInformer() {
		return informer;
	}

}
