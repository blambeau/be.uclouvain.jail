package be.uclouvain.jail.uinfo.functions;

import be.uclouvain.jail.algo.commons.Avoid;

/**
 * Provides a function that verify that all operands are the same and
 * returns that unique value.
 * 
 * <p>This function throws an Avoid exception when the all-same constraint
 * is not respected. It should then be used in algorithm which are robust
 * against that exception.</p>
 * 
 * @author blambeau
 */
public class AllSameFunction<T> extends AbstractAggregateFunction<T> {

	public AllSameFunction() {
	}
	
	/** Checks the op1.equals(op2) and returns op1. */
	@Override
	public T compute(T op1, T op2) {
		if (op1 == null && op2 == null) { return null; }
		if (op1 != null && op1.equals(op2)) {
			return op1;
		} else {
			throw new Avoid();
		}
	}

	/** Throws an Avoid exception. */
	@Override
	protected T onEmpty() {
		throw new Avoid();
	}

}
