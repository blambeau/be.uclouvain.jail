package be.uclouvain.jail.uinfo;

import java.util.HashMap;
import java.util.Map;

import be.uclouvain.jail.adapt.AdaptUtils;

/** User info based on a HashMap. */
public class MapUserInfo implements IUserInfo {

	/** Used map. */
	private Map<String, Object> info;

	/** Creates an empty info. */
	public MapUserInfo() {
		this.info = new HashMap<String,Object>();
	}
	
	/** Copy constructor. */
	private MapUserInfo(MapUserInfo copy) {
		this.info = new HashMap<String,Object>();
		this.info.putAll(copy.info);
	}
	
	/** Creates single (key,value) user info. */ 
	public static IUserInfo factor(String key, Object value) {
		MapUserInfo info = new MapUserInfo();
		info.setAttribute(key, value);
		return info;
	}
	
	/* (non-Javadoc)
	 * @see be.uclouvain.jail.uinfo.IUserInfo#getKeys()
	 */
	public Iterable<String> getKeys() {
		return info.keySet();
	}

	/*
	 * (non-Javadoc)
	 * @see be.uclouvain.jail.uinfo.IUserInfo#getAttribute(java.lang.String)
	 */
	public Object getAttribute(String key) {
		return info.get(key);
	}

	/*
	 * (non-Javadoc)
	 * @see be.uclouvain.jail.uinfo.IUserInfo#getAttributes()
	 */
	public Map<String, Object> getAttributes() {
		return info;
	}

	/*
	 * (non-Javadoc)
	 * @see be.uclouvain.jail.uinfo.IUserInfo#removeAttribute(java.lang.String)
	 */
	public void removeAttribute(String key) {
		info.remove(key);
	}

	/*
	 * (non-Javadoc)
	 * @see be.uclouvain.jail.uinfo.IUserInfo#setAttribute(java.lang.String, java.lang.Object)
	 */
	public void setAttribute(String key, Object value) {
		info.put(key, value);
	}

	/*
	 * (non-Javadoc)
	 * @see be.uclouvain.jail.uinfo.IUserInfo#copy()
	 */
	public IUserInfo copy() {
		return new MapUserInfo(this);
	}
	
	/* (non-Javadoc)
	 * @see be.uclouvain.jail.adapt.IAdaptable#adapt(java.lang.Class)
	 */
	public <T> Object adapt(Class<T> c) {
		if (c.isAssignableFrom(this.getClass())) {
			return this;
		}
		return AdaptUtils.externalAdapt(this,c);
	}

	/** Returns a string representation. */
	public String toString() {
		return info.toString();
	}

}
