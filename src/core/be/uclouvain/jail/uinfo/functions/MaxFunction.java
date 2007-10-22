package be.uclouvain.jail.uinfo.functions;

/** Computes the maximum of all values. */
public class MaxFunction extends AbstractAggregateFunction<Comparable> {

	/** Computes minimum of op1 and op2. */
	@SuppressWarnings("unchecked")
	@Override
	public Comparable compute(Comparable op1, Comparable op2) {
		return op1.compareTo(op2) < 0 ? op2 : op1;
	}

	/** Returns null. */
	@Override
	protected Comparable onEmpty() {
		return null;
	}

}
