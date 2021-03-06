package be.uclouvain.jail.uinfo.functions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import be.uclouvain.jail.fa.functions.FAStateKindFunction;
import be.uclouvain.jail.vm.JailVMException;

/** Factory for aggregate functions. */
public final class AggregateFunctionFactory {

	/** Not intended to be implemented. */
	private AggregateFunctionFactory() {}
	
	/** Factors a concat function. */
	public static IAggregateFunction<?> concat() {
		return new ConcatFunction();
	}
	
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
	
	/** Factors an all-same function. */
	public static IAggregateFunction<?> allsame() {
		return new AllSameFunction();
	}
	
	/** Factors a group function. */
	public static IAggregateFunction<?> group() {
		return new GroupFunction();
	}
	
	/** Factors a list function. */
	public static IAggregateFunction<?> list() {
		return new ListFunction();
	}
	
	/** Factors a list function. */
	public static IAggregateFunction<?> stateKind(int acceptingOp, int errorOp, boolean throwOnAvoid) {
		return new FAStateKindFunction(acceptingOp,errorOp,throwOnAvoid);
	}

	/** Returns a function by name. */
	public static IAggregateFunction getAggregateFunction(String name) throws JailVMException {
		try {
			Method m = AggregateFunctionFactory.class.getMethod(name, new Class[0]);
			if (m == null) {
				return null;
			} else {
				return (IAggregateFunction) m.invoke(null, new Object[0]);
			}
		} catch (NoSuchMethodException e) {
			return null;
		} catch (IllegalAccessException e) {
			throw new AssertionError("Method invocation worked.");
		} catch (InvocationTargetException e) {
			throw new AssertionError("Method invocation worked.");
		}
	}
	
}
