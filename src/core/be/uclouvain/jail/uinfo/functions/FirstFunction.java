package be.uclouvain.jail.uinfo.functions;

/** Computes the minimum of all values. */
public class FirstFunction implements IAggregateFunction<Object> {

	/** Computes the first element. */
	public Object compute(Iterable<Object> operands) {
		return operands.iterator().next();
	}

	/** Returns op1. */
	public Object compute(Object op1, Object op2) {
		return op1;
	}

	/** Returns false. */
	public boolean isCommutative() {
		return false;
	}

}
