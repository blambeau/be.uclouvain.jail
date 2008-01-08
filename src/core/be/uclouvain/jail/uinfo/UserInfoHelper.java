package be.uclouvain.jail.uinfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.chefbe.javautils.adapt.IAdapter;
import net.chefbe.javautils.adapt.StringAdaptationTool;
import be.uclouvain.jail.fa.FAStateKind;
import be.uclouvain.jail.fa.impl.AttributeGraphFAInformer;

/**
 * Helps creating user infos.
 * 
 * @author blambeau
 */
public class UserInfoHelper implements IUserInfoHelper {

	/** From string adaptation tool. */
	private StringAdaptationTool fromString = new StringAdaptationTool();
	
	/** Attributes to convert. */
	private Map<String,Class<?>> types = new HashMap<String,Class<?>>();
	
	/** Next keys. */
	private List<String> keys = new ArrayList<String>();
	
	/** Next values. */
	private List<Object> values = new ArrayList<Object>();
	
	/** Static instance. */
	private static IUserInfoHelper instance;
	
	/** Returns a default instance. */
	public static synchronized IUserInfoHelper instance() {
		if (instance == null) {
			instance = new UserInfoHelper();
			instance.addAdapter(FAStateKind.class, StringAdaptationTool.TO_ENUM);
			instance.register(AttributeGraphFAInformer.STATE_INITIAL_KEY, Boolean.class);
			instance.register(AttributeGraphFAInformer.STATE_KIND_KEY, FAStateKind.class);
		}
		return instance;
	}
	
	/** Sets the helper to use. */
	public static synchronized void setInstance(IUserInfoHelper instance) {
		UserInfoHelper.instance = instance;
	}
	
	/* (non-Javadoc)
	 * @see be.uclouvain.jail.uinfo.IUserInfoHelper#addAdapter(java.lang.Class, net.chefbe.javautils.adapt.IAdapter)
	 */
	public void addAdapter(Class<?> type, IAdapter adapter) {
		fromString.register(String.class, type, adapter);
	}
	
	/* (non-Javadoc)
	 * @see be.uclouvain.jail.uinfo.IUserInfoHelper#register(java.lang.String, java.lang.Class)
	 */
	public void register(String attr, Class<?> type) {
		types.put(attr, type);
	}

	/* (non-Javadoc)
	 * @see be.uclouvain.jail.uinfo.IUserInfoHelper#addKeyValue(java.lang.String, java.lang.Object)
	 */
	public void addKeyValue(String key, Object value) {
		this.keys.add(key);
		this.values.add(value);
	}
	
	/* (non-Javadoc)
	 * @see be.uclouvain.jail.uinfo.IUserInfoHelper#keys(java.lang.String)
	 */
	public void keys(String...keys) {
		if (keys == null) { 
			throw new IllegalArgumentException("Keys cannot be null"); 
		}
		for (String k: keys) {
			if (k == null) {
				throw new IllegalArgumentException("Keys cannot be null");
			} else {
				this.keys.add(k);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see be.uclouvain.jail.uinfo.IUserInfoHelper#values(java.lang.Object)
	 */
	public void values(Object...values) {
		if (values == null) { 
			throw new IllegalArgumentException("Values cannot be null"); 
		}
		for (Object value: values) {
			if (value == null) {
				throw new IllegalArgumentException("Values cannot be null");
			} else {
				this.values.add(value);
			}
		}
	}
	
	/** Installs a user info. */
	public IUserInfo keyValue(String key, Object value) {
		addKeyValue(key,value);
		return install();
	}
	
	/** Installs a user info. */
	public IUserInfo install(Object...vs) {
		return install(null,vs);
	}
	
	/* (non-Javadoc)
	 * @see be.uclouvain.jail.uinfo.IUserInfoHelper#install(be.uclouvain.jail.uinfo.IUserInfo, java.lang.Object)
	 */
	public IUserInfo install(IUserInfo info, Object...vs) {
		if (vs != null) { values(vs); }
		int size = keys.size();
		if (values.size() != size) {
			throw new IllegalArgumentException("Keys and values must have same length.");
		}
		
		// make required conversions
		for (int i=0; i<size; i++) {
			String key = keys.get(i);
			if (types.containsKey(key)) {
				Class<?> type = types.get(key);
				Object value = values.get(i);
				if (!type.isInstance(value)) {
					Object converted = fromString.adapt(value, type);
					if (converted == null) {
						throw new IllegalArgumentException("Unable to convert " + value + " to " + type.getSimpleName());
					} else {
						values.set(i, converted);
					}
				}
			}
			i++;
		}
		
		// create a user info
		if (info == null) { info = new MapUserInfo(); }
		for (int i=0; i<size; i++) {
			info.setAttribute(keys.get(i), values.get(i));
		}
		
		this.keys.clear();
		this.values.clear();
		return info;
	}
	
}
