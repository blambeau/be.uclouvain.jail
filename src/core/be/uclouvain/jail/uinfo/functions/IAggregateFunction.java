package be.uclouvain.jail.uinfo.functions;

/**
 * Defines an aggregate function to be used to construct user infos.
 */
public interface IAggregateFunction<T> {

	/** Computes function value on operands. */
	public T compute(Iterable<T> operands);

	/** Returns true if this function is commutative. */
	public boolean isCommutative();

	/** Commutative compute on two operands. */
	public T compute(T op1, T op2);
	
}
