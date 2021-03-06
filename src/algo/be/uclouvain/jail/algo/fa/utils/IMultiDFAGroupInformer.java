package be.uclouvain.jail.algo.fa.utils;

import be.uclouvain.jail.fa.IDFA;

/** Informer for groups. */
public interface IMultiDFAGroupInformer {

	/** Returns the i-th DFA. */
	public IDFA getDFA(int i);
	
}
