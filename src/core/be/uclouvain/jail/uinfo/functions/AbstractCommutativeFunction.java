package be.uclouvain.jail.uinfo.functions;

import java.util.Set;

/** 
 * Helper to create commutative functions. 
 * 
 * @author blambeau
 */
public abstract class AbstractCommutativeFunction<T> implements IUserInfoFunction<T> {

	/** Computes function value on operands. */
	public T compute(Set<T> operands) {
		T result = neutral();
		for (T t : operands) {
			result = compute(result,t);
		}
		return result;
	}

	/** Returns true if this function is commutative. */
	public boolean isCommutative() {
		return true;
	}

	/** Returns the absorber value. */
	protected abstract T neutral();
	
	/** Commutative compute on two operands. */
	public abstract T compute(T op1, T op2);
	
}
