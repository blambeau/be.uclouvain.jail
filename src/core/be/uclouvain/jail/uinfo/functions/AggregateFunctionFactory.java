package be.uclouvain.jail.uinfo.functions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/** Factory for aggregate functions. */
public final class AggregateFunctionFactory {

	/** Not intended to be implemented. */
	private AggregateFunctionFactory() {}
	
	/** Factors a sum function. */
	public static IAggregateFunction<?> sum() {
		return new SumFunction();
	}
	
	/** Factors a max function. */
	public static IAggregateFunction<?> max() {
		return new MaxFunction();
	}
	
	/** Factors a min function. */
	public static IAggregateFunction<?> min() {
		return new MinFunction();
	}
	
	/** Factors a bool AND function. */
	public static IAggregateFunction<?> boolAnd() {
		return new BoolAndFunction();
	}
	
	/** Factors a bool OR function. */
	public static IAggregateFunction<?> boolOr() {
		return new BoolOrFunction();
	}
	
	/** Factors a pick-up function. */
	public static IAggregateFunction<?> pickUp() {
		return new PickUpFunction();
	}

	/** Returns a function by name. */
	public static IAggregateFunction getAggregateFunction(String name) {
		try {
			Method m = AggregateFunctionFactory.class.getMethod(name, new Class[0]);
			if (m == null) {
				return null;
			} else {
				return (IAggregateFunction) m.invoke(null, new Object[0]);
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
