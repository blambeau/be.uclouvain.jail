package be.uclouvain.jail.vm;

/** 
 * Defines a JAIL Virtual Machine plugin.
 * 
 * @author blambeau
 */
public interface IJailVMPlugin {

	/** Returns plugin namespace. */
	public String namespace();
	
	/** Executes some command on the virtual machine. */
	public Object execute(JailVMCommand command, JailVM vm) throws JailVMException;
	
}