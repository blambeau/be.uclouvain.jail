package be.uclouvain.jail.adapt;

/**
 * Extension of IAdaptable to provide instance base adaptations.
 * 
 * @author blambeau
 */
public interface IOpenAdaptable extends IAdaptable {

	/** Allows the registration of an adaptation. */
	public void addAdaptation(Class c, IAdapter adapter);
	
}
