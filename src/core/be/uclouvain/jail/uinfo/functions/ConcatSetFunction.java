package be.uclouvain.jail.uinfo.functions;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/** Creates a set. */
public class ConcatSetFunction<T> extends AbstractCommutativeFunction<Object> {

	/** Returns an empty set. */
	@Override
	protected Object neutral() {
		return Collections.EMPTY_SET;
	}

	/** Converts a value to a set. */
	@SuppressWarnings("unchecked")
	private Set<T> toSet(Object op) {
		if (op instanceof Set) {
			return (Set<T>) op;
		} else {
			Set<T> set = new HashSet<T>();
			set.add((T)op);
			return set;
		}
	}
	
	/** Constructs a set. */
	@Override
	public Object compute(Object op1, Object op2) {
		Set<T> set1 = toSet(op1);
		Set<T> set2 = toSet(op2);
		Set<T> result = new HashSet<T>();
		result.addAll(set1);
		result.addAll(set2);
		return result;
	}

}
