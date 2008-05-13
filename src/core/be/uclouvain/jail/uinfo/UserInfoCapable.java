package be.uclouvain.jail.uinfo;

import java.util.Map;


/** 
 * Provides an implementation for all objects which allow attaching some user info
 * class. 
 *   
 * @author blambeau
 */
public class UserInfoCapable {

	/** Some identifier. */
	protected int id;
	
	/** Attached user info. */
	protected IUserInfo info;
	
	/** Empty constructor. Installs a MapUserInfo. */
	public UserInfoCapable() {
		this(new MapUserInfo());
	}

	/** Constructor with specific user info. */
	public UserInfoCapable(IUserInfo info) {
		this.info = info == null ? new MapUserInfo() : info;
	}

	/** Returns the identifier. */
	public int getId() {
		return id;
	}

	/** Sets the identifier. */
	public void setId(int id) {
		this.id = id;
	}
	
	/** Returns attached user info. */
	public final IUserInfo getUserInfo() {
		return info;
	}

	/** Sets the user info to use. */
	public final void setUserInfo(IUserInfo info) {
		this.info = info;
	}

	/** Short form for <pre>getUserInfo().getAttribute(key)</pre>. */
	public final Object getAttribute(String key) {
		return info.getAttribute(key);
	}

	/** Short form for <pre>getUserInfo().getAttributes()</pre>. */
	public final Map<String,Object> getAttributes() {
		return info.getAttributes();
	}

	/** Short form for <pre>getUserInfo().setAttribute(key,value)</pre>. */
	public final void setAttribute(String key, Object value) {
		info.setAttribute(key, value);
	}

	/** Short form for <pre>getUserInfo().removeAttribute(key)</pre>. */
	public final void removeAttribute(String key) {
		info.removeAttribute(key);
	}
	
	/** Returns a string. */
	public String toString() {
		return Integer.toString(id);
	}
	
}
