package be.uclouvain.jail.vm;

/**
 * Toolkit for JAIL command line tool.
 * 
 * <p>This interface is not intended to be implemented directly. Extend
 * {@link AbstractJailToolkit} or {@link JailReflectionToolkit} instead.</p>
 * 
 * @author blambeau
 */
public interface IJailVMToolkit extends Iterable<IJailVMCommand> {

	/** Installs the toolkiy on a virtual machine. */
	public void install(JailVM vm);
	
	/** Adds a command to the toolkit. */
	public void addCommand(IJailVMCommand command);
	
	/** Returns true if a command is recognized. */
	public boolean hasCommand(String command);
	
	/** Returns the command mapped to a specific name. */
	public IJailVMCommand getCommand(String command);
	
}
