package be.uclouvain.jail.algo.fa.equiv;

import be.uclouvain.jail.fa.IDFA;

/**
 * Provides a default implementation of {@link IDFAEquivInput}.
 * 
 * @author blambeau
 */
public class DefaultDFAEquivInput implements IDFAEquivInput {

	/** DFA to test. */
	private IDFA tested;
	
	/** Reference DFA. */
	private IDFA reference;

	/** Informer to use. */
	private IDFAEquivInformer informer;
	
	/** Counter example enabled? */
	private boolean counterExampleEnabled;
	
	/** Creates an input instance. */
	public DefaultDFAEquivInput(IDFA tested, IDFA reference) {
		this.tested = tested;
		this.reference = reference;
	}
	
	/** Returns tested DFA. */
	public IDFA getTested() {
		return tested;
	}

	/** Returns reference DFA. */
	public IDFA getReference() {
		return reference;
	}

	/** Returns true if non-equivalence trace must be 
	 * provided, false otherwise. */
	public boolean isCounterExampleEnabled() {
		return this.counterExampleEnabled;
	}

	/** Sets the non-equivalence trace enabled flag. */
	public void setCounterExampleEnabled(boolean enabled) {
		this.counterExampleEnabled = enabled;
	}
	
	/** Returns user-defines informer to use. */
	public IDFAEquivInformer getInformer() {
		return informer;
	}

}
