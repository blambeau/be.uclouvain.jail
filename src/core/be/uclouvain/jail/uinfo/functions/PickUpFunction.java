package be.uclouvain.jail.uinfo.functions;

/** Takes the first non null value in the iterated operands. */
public class PickUpFunction<T> extends NonCommutativeFunction<T> {

	/** Computes the first element. */
	public T compute(Iterable<T> operands) {
		for (T o: operands) {
			if (o != null) { return o; }
		}
		return null;
	}

}
