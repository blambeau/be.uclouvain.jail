package be.uclouvain.jail.uinfo;

import java.util.HashSet;
import java.util.Set;

/** Copies many attributes at once. */
public class CopyPopulator implements IUserInfoPopulator<IUserInfo> {
	
	/** Attributes. */
	private Set<String> attrs;

	/** Copies all but specified attributes? */
	private boolean allbut;
	
	/** Copies all attributes. */
	public CopyPopulator() {
		this(false,(String[])null);
	}
	
	/** Copies only specified attributes. */
	public CopyPopulator(String...attrs) {
		this(false,attrs);
	}
	
	/** All-but copies specified attributes. */
	public CopyPopulator(boolean allbut, String...attrs) {
		this.allbut = allbut;
		if (attrs != null) {
			this.attrs = new HashSet<String>();
			for (String s: attrs) {
				if (s==null) {
					throw new IllegalArgumentException("Attribute key cannot be null.");
				}
				this.attrs.add(s);
			}
		}
	}
	
	/** Copies source[srcAttr] into target[trgAttr]. */
	public void populate(IUserInfo target, IUserInfo source) {
		if (this.attrs == null || allbut) {
			Iterable<String> keys = source.getKeys();
			if (keys == null) {
				// source does not provide iterable on keys, 
				// force Map usage
				keys = source.getAttributes().keySet();
			}
			for (String key: keys) {
				if (this.attrs != null && this.attrs.contains(key)) {
					continue;
				}
				Object value = source.getAttribute(key);
				target.setAttribute(key, value);
			}
		} else {
			// attrs != null && !allbut
			for (String attr: attrs) {
				target.setAttribute(attr, source.getAttribute(attr));
			}
		}
	}

}