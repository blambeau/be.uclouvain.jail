package be.uclouvain.jail.algo.induct.utils;

import java.util.Iterator;

/** Decorates Gain works to help creating membership queries. */ 
public interface IGainD {

	/** Computes the short prefix. */
	public Object[] shortPrefix();
	
	/** Returns gained letter. */
	public Object letter();
	
	/** Computes the suffixes. */
	public Iterator<Suffix> suffixes();
	
}
