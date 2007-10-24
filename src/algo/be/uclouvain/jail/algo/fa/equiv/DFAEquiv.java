package be.uclouvain.jail.algo.fa.equiv;

import be.uclouvain.jail.algo.fa.equiv.IDFAEquivOutput.CounterExampleKind;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.IDFATrace;

/**
 * Provides a user-friendly API to check equivalence of DFAs.
 * 
 * @author blambeau
 */
public class DFAEquiv {

	/** Algorithm input. */
	private DefaultDFAEquivInput input;

	/** Algorithm output. */
	private DefaultDFAEquivOutput output;
	
	/** Algorithm executed? */
	private boolean executed = false;
	
	/** Creates a DFAEquiv algorithm instance. */
	public DFAEquiv(IDFA tested, IDFA reference) {
		this.input = new DefaultDFAEquivInput(tested,reference);
		this.output = new DefaultDFAEquivOutput();
	}
	
	/** Sets counter example enabled flag. */
	public void setCounterExampleEnabled(boolean enabled) {
		this.input.setCounterExampleEnabled(enabled);
	}
	
	/** Returns true if the two DFAs are equivalent. */
	public boolean areEquivalent() {
		if (!executed) { execute(); }
		return output.areEquivalent();
	}
	
	/** Checks DFA equivalence of tested with reference. */ 
	public static boolean isEquivalentTo(IDFA tested, IDFA reference) {
		return new DFAEquiv(tested,reference).areEquivalent();
	}
	
	/** Executes equiv algorithm. */
	private void execute() {
		new DFAEquivAlgo().execute(input,output);
		executed = true;
	}

	/** Returns kind of counter example. */
	public CounterExampleKind getCounterExampleKind() {
		if (!executed) { execute(); }
		return output.getCounterExampleKind();
	}
	
	/** Returns kind of counter example. */
	public IDFATrace getCounterExample() {
		if (!executed) { execute(); }
		return output.getCounterExample();
	}
	
}
