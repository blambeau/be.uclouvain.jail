package be.uclouvain.jail.uinfo.functions;

import java.util.HashSet;
import java.util.Set;

/** Creates a set group with values. */
public class GroupFunction extends NonCommutativeFunction<Object> {

	/** Creates a set with the values. */
	public Object compute(Iterable<Object> operands) {
		Set<Object> set = new HashSet<Object>();
		for (Object op: operands) {
			set.add(op);
		}
		return set;
	}

}
