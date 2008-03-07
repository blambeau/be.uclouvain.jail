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
	public static final int TRUE = 4;
	public static final int FALSE = 8;
	
	/** Accepting operator. */
	private int acceptingOp;
	
	/** Error operator. */
	private int errorOp;
	
	/** Throw Avoid exception when reaching avoid? */
	private boolean throwOnAvoid = false;
	
	/** Creates a function with a specific suppremum lattice and
	 *  falg indicating if an avoid exception must be thrown. */
	public FAStateKindFunction(int acceptingOp, int errorOp, boolean throwOnAvoid) {
		this.acceptingOp = acceptingOp;
		this.errorOp = errorOp;
		this.throwOnAvoid = throwOnAvoid;
	}

	/** Computes the suppremum between two values. */
	public FAStateKind supremum(FAStateKind k1, FAStateKind k2) {
		boolean isAccepting = false;
		boolean isError = false;
		switch (acceptingOp) {
			case TRUE: isAccepting = true; break;
			case FALSE: isAccepting = false; break;
			case OR: isAccepting = k1.isFlagAccepting() || k2.isFlagAccepting(); break; 
			case AND: isAccepting = k1.isFlagAccepting() && k2.isFlagAccepting(); break;
		}
		switch (errorOp) {
			case TRUE: isError = true; break;
			case FALSE: isError = false; break;
			case OR: isError = k1.isFlagError() || k2.isFlagError(); break; 
			case AND: isError = k1.isFlagError() && k2.isFlagError(); break;
		}
	    return FAStateKind.fromBools(isAccepting, isError);
	}

	/** Computes the suppremum. */
	@Override
	public FAStateKind compute(FAStateKind op1, FAStateKind op2) {
		FAStateKind kind = supremum(op1, op2);
		if (throwOnAvoid && FAStateKind.AVOID.equals(kind)
			&& (!FAStateKind.AVOID.equals(op1) || !FAStateKind.AVOID.equals(op2))) {
			throw new Avoid();	
		}
		return kind;
	}

	/** Returns FAStateKind.PASSAGE. */
	protected FAStateKind onEmpty() {
		boolean isAccepting = (acceptingOp == TRUE);
		boolean isError = (errorOp == TRUE);
		return FAStateKind.fromBools(isAccepting, isError);
	}
	
}
