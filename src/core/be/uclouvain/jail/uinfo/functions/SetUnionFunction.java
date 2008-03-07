package be.uclouvain.jail.uinfo.functions;

import java.util.HashSet;
import java.util.Set;

/** 
 * Computes the union of sets.
 * 
 * @author blambeau
 */
public class SetUnionFunction<T> implements IAggregateFunction<Set<T>> {

	/** Compute the function. */
	public Set<T> compute(Iterable<Set<T>> operands) {
		Set<T> set = emptySet();
		for (Set<T> o: operands) {
			if (o == null) { continue; }
			set.addAll(o);
		}
		return set;
	}

	/** Computes union of two sets. */
	public Set<T> compute(Set<T> op1, Set<T> op2) {
		Set<T> set = emptySet();
		if (op1 != null) { set.addAll(op1); }
		if (op2 != null) { set.addAll(op2); }
		return set;
	}

	/** Returns true. */
	public boolean isCommutative() {
		return true;
	}

	/** Creates a HashSet by default. This method may be overrided. */
	protected Set<T> emptySet() {
		return new HashSet<T>();
	}

}
