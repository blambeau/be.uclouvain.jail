package be.uclouvain.jail.vm;

/**
 * Toolkit for JAIL command line tool.
 * 
 * <p>This interface is not intended to be implemented directly. Extend
 * {@link AbstractJailToolkit} or {@link JailReflectionToolkit} instead.</p>
 * 
 * @author blambeau
 */
public interface IJailVMToolkit {

	/** Installs the toolkiy on a virtual machine. */
	public void install(JailVM vm);
	
	/** Returns true if a command is recognized. */
	public boolean hasCommand(String command);
	
	/** Executes a command on the virtual machine. */
	public Object executeCommand(
			String command, 
			JailVM vm, 
			JailVMStack stack, 
			JailVMOptions options) throws JailVMException;
	
}
