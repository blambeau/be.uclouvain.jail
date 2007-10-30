package be.uclouvain.jail.vm;

import java.util.HashMap;
import java.util.Map;

/** Decorates a map as a scope. */
public class JailVMMapScope implements IJailVMScope {

	/** Variables. */
	private Map<String,Object> variables;

	/** Creates an empty scope instance. */
	public JailVMMapScope() {
		variables = new HashMap<String,Object>();
	}
	
	/** Checks if a variable is known. */
	public boolean knows(String var) {
		return variables.containsKey(var);
	}

	/** Returns value of a variable. */
	public Object valueOf(String var) {
		return variables.get(var);
	}

	/** Affects a value to a variable. */
	public void affect(String var, Object value) {
		variables.put(var, value);
	}

}
