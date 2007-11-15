package be.uclouvain.jail.adapt;

/**
 * Extension of IAdaptable to provide instance base adaptations.
 * 
 * <p>A class may implement this interface to allow specific instance 
 * adaptations. External adapters may then be registered on these instances
 * using the provided method.</p> 
 * 
 * @author blambeau
 */
public interface IOpenAdaptable extends IAdaptable {

	/** Allows the registration of an adaptation. */
	public void addAdaptation(Class c, IAdapter adapter);
	
}
