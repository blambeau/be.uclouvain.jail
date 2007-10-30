package be.uclouvain.jail.vm;

/**
 * Defines a scope.
 * 
 * <p>This interface is a internal interface definition and is not 
 * intended to be instanciated by users.</p>
 * 
 * @author blambeau
 */
public interface IJailVMScope {

	/** Returns true if a variable is known. */
	public boolean knows(String var);
	
	/** Returns the value of some variable. */
	public Object valueOf(String var);
	
	/** Affects a value to a variable. */
	public void affect(String var, Object value);
	
}
