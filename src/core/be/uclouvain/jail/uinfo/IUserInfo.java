package be.uclouvain.jail.uinfo;

import java.util.Map;

import be.uclouvain.jail.adapt.IAdaptable;

/** 
 * Encapsulates user information inside all graph and automata 
 * components.
 * 
 * @author blambeau
 */
public interface IUserInfo extends IAdaptable {

	/** Returns a user info attribute mapped to a key. */
	public Object getAttribute(String key);

	/** Returns all attributes. */
	public Map<String, Object> getAttributes();

	/** Sets a user info attribute. */
	public void setAttribute(String key, Object value);

	/** Removes a user info attribute. */
	public void removeAttribute(String key);

	/** Copies this user info as deeply as possible. */
	public IUserInfo copy();
	
}
