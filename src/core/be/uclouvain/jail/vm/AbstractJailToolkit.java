package be.uclouvain.jail.vm;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides an implementation of IJailVMToolkit that allows user-defined
 * operators to be attached. 
 *  
 * @author blambeau
 */
public class AbstractJailToolkit implements IJailVMToolkit {

	/** Attached commands. */
	private Map<String,JailVMUserCommand> commands;
	
	/** Installs the toolkiy on a virtual machine. */
	public void install(JailVM vm) {
	}
	
	/** Returns true if a command is recognized. */
	public boolean hasCommand(String command) {
		return (commands != null && commands.containsKey(command));
	}
	
	/** Executes a command on the virtual machine. */
	public Object executeCommand(
			String command, 
			JailVM vm, 
			JailVMStack stack, 
			JailVMOptions options) throws JailVMException {
		if (commands != null && commands.containsKey(command)) {
			return commands.get(command).execute(command,vm,stack,options);
		} else {
			throw new JailVMException("Unknown command: " + command);
		}
	}
	
	/** Adds a user command. */
	public void addUserCommand(JailVMUserCommand command) {
		if (commands == null) {
			commands = new HashMap<String,JailVMUserCommand>(); 
		}
		commands.put(command.getName(), command);
	}
	
	/** Extracts an exception. */
	protected JailVMException extractException(String command, Exception e) {
		Throwable cause = e.getCause();
		if (cause instanceof JailVMException) {
			return (JailVMException) cause;
		} else {
			return new JailVMException("Toolkit command " + command + " failed.",e);
		}
	}

}
