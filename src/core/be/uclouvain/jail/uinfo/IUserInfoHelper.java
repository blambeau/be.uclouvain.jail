package be.uclouvain.jail.uinfo;

import net.chefbe.javautils.adapt.IAdapter;

/**
 * Helps creating user infos.
 * 
 * @author blambeau
 */
public interface IUserInfoHelper {

	/** Adds an adapter from string. */
	public void addAdapter(Class<?> type, IAdapter adapter);

	/** Registers a type. */
	public void register(String attr, Class<?> type);

	/** Adds a key/value pair. */
	public void addKeyValue(String key, Object value);

	/** Puts some keys to install. */
	public void keys(String... keys);

	/** Install some values. */
	public void values(Object... values);

	/** Installs a user info. */
	public IUserInfo keyValue(String key, Object value);
	
	/** Installs a user info. */
	public IUserInfo install(Object...vs);
	
	/** Installs some values. */
	public IUserInfo install(IUserInfo info, Object... vs);

}