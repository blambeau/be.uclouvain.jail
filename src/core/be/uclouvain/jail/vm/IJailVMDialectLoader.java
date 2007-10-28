package be.uclouvain.jail.vm;

/**
 * JailCoreToolkit extension interface to load files in diferent 
 * dialects.
 * 
 * @author blambeau
 */
public interface IJailVMDialectLoader {

	/** Loads from a source. */
	public Object load(Object source, String format) throws JailVMException;
	
}
