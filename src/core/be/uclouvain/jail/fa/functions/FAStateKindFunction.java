package be.uclouvain.jail.fa.functions;

import be.uclouvain.jail.uinfo.functions.AbstractCommutativeFunction;

/** Function to handle automaton state kind. */
public class FAStateKindFunction extends AbstractCommutativeFunction<FAStateKind> {

	/** Computes the suppremum. */
	@Override
	public FAStateKind compute(FAStateKind op1, FAStateKind op2) {
		return FAStateKind.supremum(op1, op2);
	}

}
