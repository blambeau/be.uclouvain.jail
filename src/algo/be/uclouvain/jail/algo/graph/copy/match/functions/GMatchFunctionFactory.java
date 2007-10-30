package be.uclouvain.jail.algo.graph.copy.match.functions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import be.uclouvain.jail.vm.JailVMException;

/** Provides a factory for GMatch functions. */
public class GMatchFunctionFactory {

	/** Not intended to be implemented. */
	private GMatchFunctionFactory() {}
	
	/** Factors a concat function. */
	public static IGMatchFunction<?> concat() {
		return new ConcatFunction();
	}
	
	/** Returns a function by name. */
	public static IGMatchFunction<?> getGMatchFunction(String name) throws JailVMException {
		try {
			Method m = GMatchFunctionFactory.class.getMethod(name, new Class[0]);
			if (m == null) {
				return null;
			} else {
				return (IGMatchFunction) m.invoke(null, new Object[0]);
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
