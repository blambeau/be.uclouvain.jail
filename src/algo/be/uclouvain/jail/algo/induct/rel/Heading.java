package be.uclouvain.jail.algo.induct.rel;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.chefbe.javautils.collections.list.OrderedList;
import net.chefbe.javautils.comparisons.HashCodeUtils;

/**
 * Heading of a relation.
 * 
 * @author blambeau
 */
public class Heading {

	/** Names of the attributes. */
	private List<String> names;

	/** Types of the attributes. */
	private Class[] types;
	
	/** Creates a heading instance. */
	public Heading(String[] names, Class[] types) {
		if (names == null || types == null) {
			throw new IllegalArgumentException("Names and types must be provided.");
		}
		if (names.length != types.length) {
			throw new IllegalArgumentException("Same length expected.");
		}
		
		// install instance variables
		this.names = new OrderedList<String>(names);
		
		// rebing types
		int size = names.length;
		this.types = new Class[size];
		for (int i=0; i<size; i++) {
			int rebind = this.names.indexOf(names[i]);
			this.types[rebind] = types[i];
		}
	}
	
	/** Returns names. */
	public List<String> getNames() {
		return Collections.unmodifiableList(names);
	}

	/** Returns heading's degree. */
	public int degree() {
		return names.size();
	}
	
	/** Returns index of a name. */
	protected int indexOf(String name, boolean check) {
		int i = names.indexOf(name);
		if (i == -1 && check) {
			throw new IllegalArgumentException("Unknown attribute " + name);
		}
		return i;
	}
	
	/** Returns the type of an attribute. */
	public Class getTypeOf(String name) {
		return types[indexOf(name,true)];
	}
	
	/** Returns equivalent map. */
	public Map<String,Class> asMap() {
		Map<String,Class> map = new HashMap<String,Class>();
		int size = names.size();
		for (int i=0; i<size; i++) {
			map.put(names.get(i), types[i]);
		}
		return map;
	}
	
	/** Checks that an array of values is valid. */
	public void verify(Object...values) {
		// not null
		if (values == null) {
			throw new IllegalArgumentException("Values cannot be null.");
		}
		
		// same length
		int size = types.length;
		if (values.length != size) {
			throw new IllegalArgumentException("Invalid size of values array.");
		}
		
		// valid types
		for (int i=0; i<size; i++) {
			if (!types[i].isInstance(values[i])) {
				throw new IllegalArgumentException("Invalid type for " + values[i] + " (" + types[i].getSimpleName() + ")");
			}
		}
	}
	
	/** Computes an hash code. */
	private int hash = -1;
	public int hashCode() {
		if (hash == -1) {
			hash = HashCodeUtils.SEED;
			hash = HashCodeUtils.hash(hash,names);
			for (Class c: types) {
				hash = HashCodeUtils.hash(hash,c);
			}
		}
		return hash;
	}
	
	/** Checks equality with another object. */
	public boolean equals(Object o) {
		if (o == this) { return true; }
		if (o instanceof Heading == false) { return false; }
		Heading h = (Heading) o;
		if (names.equals(h.names) == false) { return false; }
		int size = names.size();
		for (int i=0; i<size; i++) {
			if (types[i].equals(h.types[i]) == false) {
				return false;
			}
		}
		return true;
	}
	
	/** Returns a string representation. */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		
		int size = names.size();
		for (int i=0; i<size; i++) {
			if (i != 0) {
				sb.append(", ");
			}
			sb.append(names.get(i))
			  .append(": ")
			  .append(types[i].getSimpleName());
		}
		
		sb.append("}");
		return sb.toString();
	}

}
