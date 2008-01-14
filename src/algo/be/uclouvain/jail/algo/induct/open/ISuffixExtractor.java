package be.uclouvain.jail.algo.induct.open;

import be.uclouvain.jail.algo.induct.internal.PTAState;
import be.uclouvain.jail.fa.IDFATrace;

/**
 * Extracts suffixes on a PTA.
 * 
 * @author blambeau
 */
public interface ISuffixExtractor {

	/** Extracts suffixes of a state. */
	public IDFATrace[] extract(PTAState state);
	
}
