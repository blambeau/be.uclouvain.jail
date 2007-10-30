package be.uclouvain.jail.vm;

/** 
 * Defines a Jail pluggable command.
 * 
 * <p>This interface is not intended to be implemented directly. 
 * Use toolkits instead.</p>
 * 
 * @author blambeau
 */
public interface IJailVMCommand {

	/** Returns toolkit where this command is extracted from. */
	public IJailVMToolkit getToolkit();
	
	/** Returns command name. */
	public String getName();
	
	/** Returns command signature. */ 
	public String getSignature();
	
	/** Returns command help. */
	public String getHelp();
	
	/** Executes the command on the virtual machine. */
	public Object execute(JailVM vm, JailVMStack stack, 
			JailVMOptions options) throws JailVMException;
	
	
}
