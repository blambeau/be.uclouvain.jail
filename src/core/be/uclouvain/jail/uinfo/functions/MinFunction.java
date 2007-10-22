package be.uclouvain.jail.uinfo.functions;

/** Computes the minimum of all values. */
public class MinFunction extends AbstractAggregateFunction<Comparable> {

	/** Computes minimum of op1 and op2. */
	@SuppressWarnings("unchecked")
	@Override
	public Comparable compute(Comparable op1, Comparable op2) {
		return op1.compareTo(op2) < 0 ? op1 : op2;
	}

	/** Returns null. */
	@Override
	protected Comparable onEmpty() {
		return null;
	}

}
