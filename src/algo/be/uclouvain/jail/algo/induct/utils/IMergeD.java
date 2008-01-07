package be.uclouvain.jail.algo.induct.utils;

import java.util.Iterator;

/** Decorates Merge works to help creating membership queries. */ 
public interface IMergeD {

	/** Returns the short prefix of target state. */
	public Object[] shortPrefix();

	/** Returns suffixes of the victim state. */
	public Iterator suffixes();

}
