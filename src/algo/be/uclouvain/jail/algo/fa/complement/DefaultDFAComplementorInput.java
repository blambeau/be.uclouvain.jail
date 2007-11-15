package be.uclouvain.jail.algo.fa.complement;

import be.uclouvain.jail.fa.IDFA;

/** Default implementation of IDFAComplementorInput. */
public class DefaultDFAComplementorInput implements IDFAComplementorInput {

	/** DFA to complement. */
	private IDFA dfa;
	
	/** Completion heuristic. */
	private DFAComplementorHeuristic heuristic;

	/** Creates an input instance. */
	public DefaultDFAComplementorInput(IDFA dfa) {
		this.dfa = dfa;
		this.heuristic = DFAComplementorHeuristic.ERROR_STATE;
	}

	/** Returns the DFA. */
	public IDFA getDFA() {
		return dfa;
	}

	/** Returns the heuristic. */
	public DFAComplementorHeuristic getHeuristic() {
		return heuristic;
	}

	/** Sets the heuristic to use. */
	public void setHeuristic(DFAComplementorHeuristic h) {
		this.heuristic = h;
	}
	
}
