package be.uclouvain.jail.algo.fa.tmoves;

import be.uclouvain.jail.fa.IDFA;

/**
 * Provides a default implementation of {@link ITauRemoverInput}.
 * 
 * @author blambeau
 */
public class DefaultTauRemoverInput implements ITauRemoverInput {

	/** DFA with tau transitions. */
	private IDFA dfa;
	
	/** Tau-transition informer. */
	private ITauInformer informer;
	
	/** Creates an input instance. */
	public DefaultTauRemoverInput(IDFA dfa, ITauInformer informer) {
		this.dfa = dfa;
		this.informer = informer;
	}

	/** Returns the input dfa. */
	public IDFA getDFA() {
		return dfa;
	}

	/** Returns tau informer. */
	public ITauInformer getTauInformer() {
		return informer;
	}

}
