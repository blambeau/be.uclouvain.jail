package be.uclouvain.jail.algo.fa.compose;

import be.uclouvain.jail.fa.IDFA;

/** Informer for groups. */
public interface IDFAGroupInformer {

	/** Returns the i-th DFA. */
	public IDFA getDFA(int i);
	
}