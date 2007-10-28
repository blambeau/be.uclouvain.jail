package be.uclouvain.jail.vm;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides an extensible toolkit implementation that works
 * with reflexion on available methods.
 * 
 * @author blambeau
 */
public abstract class JailReflectionToolkit implements IJailVMToolkit {

	/** Command cache by name. */
	private Map<String,Method> commands;

	/** Current options to use. */
	private JailVMOptions options;
	
	/** Creates the toolkit by reflection. */
	public JailReflectionToolkit() {
		infer();
	}
	
	/** Infers toolkit methods. */
	private void infer() {
		commands = new HashMap<String,Method>();
		for (Method m: getClass().getMethods()) {
			String name = m.getName();
			int modifiers = m.getModifiers();
			if (Modifier.isPublic(modifiers)) {
				commands.put(name, m);
			}
		}
	}
	
	/** Checks if a command exists. */
	public boolean hasCommand(String command) {
		return commands.containsKey(command);
	}
	
	/** Create the arguments array. */
	private Object[] createArgs(Method m, JailVMStack stack, JailVM vm) throws JailVMException {
		Class<?>[] types = m.getParameterTypes();
		return stack.popArgs(types);
	}
	
	/** Executes a command on the virtual machine. */
	public Object executeCommand(String command, JailVM vm, JailVMStack stack, JailVMOptions options) throws JailVMException {
		this.options = options;
		
		// retrieve method
		Method m = commands.get(command);
		
		// create method arguments
		Object[] args = createArgs(m,stack,vm);
		
		// invoke method
		try {
			return m.invoke(this, args);
		} catch (IllegalArgumentException e) {
			throw extractException(command,e);
		} catch (IllegalAccessException e) {
			throw extractException(command,e);
		} catch (InvocationTargetException e) {
			throw extractException(command,e);
		}
	}
	
	/** Extracts an exception. */
	private JailVMException extractException(String command, Exception e) {
		Throwable cause = e.getCause();
		if (cause instanceof JailVMException) {
			return (JailVMException) cause;
		} else {
			return new JailVMException("Toolkit command " + command + " failed.",e);
		}
	}

	/** Returns an option value. */
	public <T> T getOptionValue(String name, Class type, T def) throws JailVMException {
		return options.getOptionValue(name, type, def);
	}

	/** Returns an option value. */
	public Object getOptionValue(String name) throws JailVMException {
		return options.getOptionValue(name);
	}

	/** Checks if an option exists. */
	public boolean hasOption(String name) {
		return options.hasOption(name);
	}

}
