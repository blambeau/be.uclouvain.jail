package be.uclouvain.jail.algo.fa.equiv;

import be.uclouvain.jail.fa.IDFATrace;

/**
 * Provides a default implementation of {@link IDFAEquivOutput}.
 * 
 * @author blambeau
 */
public class DefaultDFAEquivOutput implements IDFAEquivOutput {

	/** Equivalence proved? */
	private boolean equivalence;
	
	/** Counter-example trace. */
	private IDFATrace trace;

	/** Counter example kind. */
	private CounterExampleKind kind;
	
	/** Mark equivalence as proved. */
	public void equivProved() {
		this.equivalence = true;
	}
	
	/** Returns true if equivalence has been proved. */
	public boolean areEquivalent() {
		return this.equivalence;
	}

	/** Mark equivalence as unproved and register the provided
	 * counter-example (that may be null, according to counterExampleEnabled flag
	 * of the algorithm input). */ 
	public void counterExampleFound(CounterExampleKind kind, IDFATrace trace) {
		this.equivalence = false;
		this.trace = trace;
		this.kind = kind;
	}

	/** Returns the kind of counter example. */
	public CounterExampleKind getCounterExampleKind() {
		return kind;
	}

	/** Returns the counter-example. */
	public IDFATrace getCounterExample() {
		return trace;
	}
	
}
