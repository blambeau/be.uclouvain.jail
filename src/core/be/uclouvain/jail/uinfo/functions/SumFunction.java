package be.uclouvain.jail.uinfo.functions;

/**
 * Sum function.
 */
public class SumFunction extends AbstractCommutativeFunction<Number> {

	/** Base class. */
	private Class base;

	/** Creates a sum function. */
	public SumFunction(Class base) {
		if (!Number.class.isAssignableFrom(base) || 
			 Number.class.equals(base) ||
			 Short.class.equals(base) ||
			 Byte.class.equals(base)) {
			throw new IllegalArgumentException("Only Integer.class, Double.Class, Float.class and Long.class " +
					"are supported as base parameter.");
		}
		this.base = base;
	}
	
	/** Returns 0. */
	@Override
	protected Number neutral() {
		return new Integer(0);
	}

	/** Computes the sum of two numbers. */
	@Override
	public Number compute(Number op1, Number op2) {
		if (Integer.class.equals(base)) {
			return new Integer(op1.intValue()+op2.intValue());
		} else if (Long.class.equals(base)) {
			return new Long(op1.longValue()+op2.longValue());
		} else if (Float.class.equals(base)) {
			return new Float(op1.floatValue()+op2.floatValue());
		} else if (Double.class.equals(base)) {
			return new Double(op1.doubleValue()+op2.doubleValue());
		} else {
			throw new IllegalStateException("Unrecognized base class " + base);
		}
	}

}
