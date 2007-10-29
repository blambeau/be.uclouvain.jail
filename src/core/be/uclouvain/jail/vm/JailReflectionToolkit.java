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
public abstract class JailReflectionToolkit extends AbstractJailToolkit {

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
		return super.hasCommand(command) 
		    || commands.containsKey(command);
	}
	
	/** Create the arguments array. */
	private Object[] createArgs(Method m, JailVMStack stack, JailVM vm) throws JailVMException {
		Class<?>[] types = m.getParameterTypes();
		return stack.popArgs(types);
	}
	
	/** Executes a command on the virtual machine. */
	public Object executeCommand(String command, JailVM vm, JailVMStack stack, JailVMOptions options) throws JailVMException {
		if (super.hasCommand(command)) {
			return super.executeCommand(command, vm, stack, options);
		}
		
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
	
	/** Returns an option value. */
	public <T> T getOptionValue(String name, Class type, T def) throws JailVMException {
		if (options == null && def == null) {
			throw new JailVMException("Option required: " + name);
		} else if (options == null && def != null) {
			return def;
		}
		return options.getOptionValue(name, type, def);
	}

	/** Returns an option value. */
	public Object getOptionValue(String name) throws JailVMException {
		if (options == null) {
			throw new JailVMException("Option required: name");
		}
		return options.getOptionValue(name);
	}

	/** Checks if an option exists. */
	public boolean hasOption(String name) {
		return options != null && options.hasOption(name);
	}

}
