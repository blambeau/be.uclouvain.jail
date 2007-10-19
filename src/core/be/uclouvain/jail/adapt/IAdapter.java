package be.uclouvain.jail.adapt;

/** 
 * Provides external adaptability to classes.
 * 
 * @author blambeau
 */
public interface IAdapter {

	/** 
	 * Adapts who object to the requested type.
	 * 
	 * <p>This method is only invoked when the adapter has been register
	 * for the the couple (who.getClass(), type).</p>
	 * 
	 * @param who object which requests the adaptibility.
	 * @param type target adaptation type requested.
	 * @return an instance of class <code>type</code> (or one of its subclass), 
	 * null if the adaptation cannot be provided. 
	 */
	public Object adapt(Object who, Class type);
	
}
