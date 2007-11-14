package be.uclouvain.jail.algo.fa.tmoves;

import be.uclouvain.jail.fa.IFA;
import be.uclouvain.jail.fa.INFA;

/**
 * Provides a user-friendly API on top of the tau-remover algorithm.
 * 
 * @author blambeau
 */
public class TauRemover {

	/** Algorithm input. */ 
	private ITauRemoverInput input;
	
	/** Algorithm output. */ 
	private ITauRemoverResult result;

	/** Algorithm executed? */
	private boolean executed = false;
	
	/** Creates a determinizer instance for a NFA. */
	public TauRemover(IFA fa, ITauInformer informer) {
		input = new DefaultTauRemoverInput(fa,informer);
		result = new DefaultTauRemoverResult();
	}
	
	/** Returns the resulting NFA. */
	public INFA getResultingNFA() {
		if (!executed) { execute(); }
		return (INFA) result.adapt(INFA.class);
	}
	
	/** Executes the algorithm. */
	private void execute() {
		new TauRemoverAlgo().execute(input, result);
		executed = true;
	}
	
}
