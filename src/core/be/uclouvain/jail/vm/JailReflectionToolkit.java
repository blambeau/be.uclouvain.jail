package be.uclouvain.jail.vm;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Provides an extensible toolkit implementation that works
 * with reflexion on available methods.
 * 
 * @author blambeau
 */
public abstract class JailReflectionToolkit extends AbstractJailToolkit {

	/** Implementation of a jail command by execution of a java method
	 * of the toolkit. */
	class JailReflectionCommand extends AbstractJailVMCommand {
		
		/** Reflection method. */
		private Method method;

		/** Creates a command instance. */
		public JailReflectionCommand(Method method) {
			this.method = method;
		}

		/** Returns command name. */
		public String getName() {
			return method.getName();
		}

		/** Returns attached toolkit. */
		public IJailVMToolkit getToolkit() {
			return JailReflectionToolkit.this;
		}
		
		/** Create the arguments array. */
		private Object[] createArgs(JailVMStack stack, JailVM vm, JailVMOptions options) throws JailVMException {
			Class<?>[] types = method.getParameterTypes();
			return stack.popArgs(types,vm,options);
		}
		
		/** Executes the command on the virtual machine. */
		public Object doExecute(JailVM vm, JailVMStack stack, JailVMOptions options) throws JailVMException {
			// create method arguments
			Object[] args = createArgs(stack,vm,options);
			
			// invoke method
			try {
				return method.invoke(JailReflectionToolkit.this, args);
			} catch (IllegalArgumentException e) {
				throw extractException(e);
			} catch (IllegalAccessException e) {
				throw extractException(e);
			} catch (InvocationTargetException e) {
				throw extractException(e);
			}
		}

	}
	
	/** Creates the toolkit by reflection. */
	public JailReflectionToolkit() {
		infer();
	}
	
	/** Infers toolkit methods. */
	private final void infer() {
		Class me = getClass();
		for (Method m: me.getMethods()) {
			// bypass not this class methods
			if (!me.equals(m.getDeclaringClass())) {
				continue;
			}
			
			// bypass adapt method
			if ("adapt".equals(m.getName())
			  ||"install".equals(m.getName())) {
				continue;
			}
			
			// check availability and create command
			int modifiers = m.getModifiers();
			if (Modifier.isPublic(modifiers)) {
				super.addCommand(new JailReflectionCommand(m));
			}
		}
	}
	
}
