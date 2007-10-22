package be.uclouvain.jail.uinfo;

/**
 * Creates IUserInfo instances.
 * 
 * @author blambeau
 */
public interface IUserInfoCreator<T> {

	/** Creates and returns a IUserInfo instance from another one. */
	public IUserInfo create(T info);
	
}
