package be.uclouvain.jail.fa.functions;

import be.uclouvain.jail.uinfo.functions.AbstractAggregateFunction;

/** 
 * Function to handle automaton state kind. 
 * 
 * <p>This function can be used when merging automaton states to compute
 * the resulting state kind.</p>
 */
public class FAStateKindFunction extends AbstractAggregateFunction<FAStateKind> {

	/** Computes the suppremum. */
	@Override
	public FAStateKind compute(FAStateKind op1, FAStateKind op2) {
		return FAStateKind.supremum(op1, op2);
	}

	/** Returns FAStateKind.PASSAGE. */
	protected FAStateKind onEmpty() {
		return FAStateKind.PASSAGE;
	}
	
}
