package be.uclouvain.jail.uinfo.functions;

import be.uclouvain.jail.graph.utils.JavaUtils;

/**
 * Sum function.
 */
public class SumFunction extends AbstractCommutativeFunction<Number> {

	/** Creates a sum function. */
	public SumFunction() {
	}
	
	/** Computes the sum of two numbers. */
	@Override
	public Number compute(Number op1, Number op2) {
		return JavaUtils.sum(op1,op2);
	}

}
