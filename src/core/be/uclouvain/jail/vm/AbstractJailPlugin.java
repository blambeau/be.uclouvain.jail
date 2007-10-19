package be.uclouvain.jail.vm;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.MethodUtils;

/** 
 * Provides a plugin implementation base which works with java reflexion.
 */ 
public abstract class AbstractJailPlugin implements IJailVMPlugin {

	/** Finds a method named with command name and delegates. */
	public Object execute(JailVMCommand command, JailVM vm) throws JailVMException {
		String name = command.name();
		try {
			return MethodUtils.invokeExactMethod(this, name, new Object[]{command,vm});
		} catch (NoSuchMethodException e) {
			throw new JailVMException("No such command " + namespace() + ":" + name);
		} catch (IllegalAccessException e) {
			throw new JailVMException("Bad command usage " + namespace() + ":" + name,e);
		} catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			if (cause instanceof JailVMException) {
				throw (JailVMException) cause;
			} else {
				throw new JailVMException("Bad command usage " + namespace() + ":" + name,e);
			}
		}
	}

	/** Asserts that some file exists and returns it. */
	public File assertFileExists(String path) throws JailVMException {
		File f = new File(path);
		if (f.exists()) {
			return f;
		} else {
			throw new JailVMException("Unable to find file " + path);
		}
	}
	
	/** Asserts that a file exists and is readable. */
	public File assertReadableFile(String path) throws JailVMException {
		File f = assertFileExists(path);
		if (f.canRead()) {
			return f;
		} else {
			throw new JailVMException("Unable to read file " + path);
		}
	}
	
	/** Asserts that some value is not null. */
	public void assertNotNull(Object value, String msg) throws JailVMException {
		if (value == null) {
			throw new JailVMException(msg);
		}
	}
	
}
