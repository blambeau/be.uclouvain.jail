package be.uclouvain.jail.uinfo.functions;

/** Computes the minimum of all values. */
public class PickUpFunction<T> extends NonCommutativeFunction<T> {

	/** Computes the first element. */
	public T compute(Iterable<T> operands) {
		for (T o: operands) {
			if (o != null) { return o; }
		}
		return null;
	}

}
