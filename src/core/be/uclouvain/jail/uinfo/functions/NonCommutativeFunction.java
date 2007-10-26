package be.uclouvain.jail.uinfo.functions;

/** Non commutative function. */
public abstract class NonCommutativeFunction<T> implements IAggregateFunction<T> {

	/** Throw an exception as this method should not be used. */
	public T compute(T op1, T op2) {
		throw new IllegalStateException("Cannot call commutative computation.");
	}

	/** Returns false. */
	public boolean isCommutative() {
		return false;
	}
	
}
