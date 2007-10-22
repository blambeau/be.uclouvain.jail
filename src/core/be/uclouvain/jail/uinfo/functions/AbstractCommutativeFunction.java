package be.uclouvain.jail.uinfo.functions;


/** 
 * Helper to create commutative functions. 
 * 
 * @author blambeau
 */
public abstract class AbstractCommutativeFunction<T> implements IAggregateFunction<T> {

	/** Computes function value on operands. */
	public T compute(Iterable<T> operands) {
		T result = null;
		for (T t : operands) {
			result = result == null ? t : compute(result,t);
		}
		return result;
	}

	/** Returns true if this function is commutative. */
	public boolean isCommutative() {
		return true;
	}

	/** Commutative compute on two operands. */
	public abstract T compute(T op1, T op2);
	
}
