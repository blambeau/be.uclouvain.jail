package be.uclouvain.jail.algo.induct.oracle;

import java.util.Iterator;

import be.uclouvain.jail.algo.induct.internal.PTAState;
import be.uclouvain.jail.fa.IFATrace;

/**
 * Extracts suffixes on a PTA.
 * 
 * @author blambeau
 */
public interface ISuffixExtractor {

	/** Extracts suffixes of a state. */
	public <T> Iterator<IFATrace<T>> extract(PTAState state);
	
}
