package be.uclouvain.jail.uinfo.functions;

import java.util.Set;

/**
 * Defines a function on user infos.
 */
public interface IUserInfoFunction<T> {

	/** Computes function value on operands. */
	public T compute(Set<T> operands);

	/** Returns true if this function is commutative. */
	public boolean isCommutative();

	/** Commutative compute on two operands. */
	public T compute(T op1, T op2);
	
}
