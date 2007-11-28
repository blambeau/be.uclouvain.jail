package be.uclouvain.jail.uinfo.functions;

import java.util.ArrayList;
import java.util.List;

/** Creates a list group with values. */
public class ListFunction extends NonCommutativeFunction<Object> {

	/** Creates a set with the values. */
	public Object compute(Iterable<Object> operands) {
		List<Object> list = new ArrayList<Object>();
		for (Object op: operands) {
			list.add(op);
		}
		return list;
	}

}
