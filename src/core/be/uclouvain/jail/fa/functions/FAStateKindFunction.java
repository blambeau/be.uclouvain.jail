package be.uclouvain.jail.fa.functions;

import be.uclouvain.jail.algo.commons.Avoid;
import be.uclouvain.jail.fa.FAStateKind;
import be.uclouvain.jail.uinfo.functions.AbstractAggregateFunction;

/** 
 * Function to handle automaton state kind. 
 * 
 * <p>This function can be used when merging automaton states to compute
 * the resulting state kind.</p>
 */
public class FAStateKindFunction extends AbstractAggregateFunction<FAStateKind> {

	/** AND / OR flags. */
	public static final int OR = 1;
	public static final int AND = 2;
	
	/** Accepting operator. */
	private int acceptingOp;
	
	/** Error operator. */
	private int errorOp;
	
	/** Throw Avoid exception when reaching avoid? */
	private boolean throwOnAvoid = false;
	
	/** Suppremum array. */
	/*
	private FAStateKind[][] suppremum = new FAStateKind[][]{
		{ FAStateKind.ACCEPTING, FAStateKind.ACCEPTING, FAStateKind.AVOID, FAStateKind.AVOID },
		{ FAStateKind.ACCEPTING, FAStateKind.PASSAGE, FAStateKind.ERROR, FAStateKind.AVOID},
		{ FAStateKind.AVOID, FAStateKind.ERROR, FAStateKind.ERROR, FAStateKind.AVOID },
		{ FAStateKind.AVOID, FAStateKind.AVOID, FAStateKind.AVOID, FAStateKind.AVOID }
	};
	*/
	
	/** Creates a function with a specific suppremum lattice and
	 *  falg indicating if an avoid exception must be thrown. */
	public FAStateKindFunction(int acceptingOp, int errorOp, boolean throwOnAvoid) {
		this.acceptingOp = acceptingOp;
		this.errorOp = errorOp;
		this.throwOnAvoid = throwOnAvoid;
	}

	/** Computes the suppremum between two values. */
	public FAStateKind supremum(FAStateKind k1, FAStateKind k2) {
		boolean isAccepting = (acceptingOp == OR) ?
				              k1.isFlagAccepting() || k2.isFlagAccepting() :
				              k1.isFlagAccepting() && k2.isFlagAccepting();
  		boolean isError = (errorOp == OR) ?
			              k1.isFlagError() || k2.isFlagError() :
			              k1.isFlagError() && k2.isFlagError();
	    return FAStateKind.fromBools(isAccepting, isError);
	}

	/** Computes the suppremum. */
	@Override
	public FAStateKind compute(FAStateKind op1, FAStateKind op2) {
		FAStateKind kind = supremum(op1, op2);
		if (FAStateKind.AVOID.equals(kind) && throwOnAvoid) {
			throw new Avoid();
		} else {
			return kind;
		}
	}

	/** Returns FAStateKind.PASSAGE. */
	protected FAStateKind onEmpty() {
		return FAStateKind.PASSAGE;
	}
	
}
