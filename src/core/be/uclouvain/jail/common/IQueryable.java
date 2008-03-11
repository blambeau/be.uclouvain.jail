package be.uclouvain.jail.common;

/**
 * Provides a query support on Jail components.
 * 
 * @author blambeau
 */
public interface IQueryable {

	/** Returns a component property. */
	public Object getProperty(String property);
	
}
