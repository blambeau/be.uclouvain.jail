package be.uclouvain.jail.algo.induct.rel;

import java.util.HashMap;
import java.util.Map;

/**
 * Heading of a relation.
 * 
 * @author blambeau
 */
public class Heading {

	/** Names of the attributes. */
	private String[] names;

	/** Indexes by name. */
	private Map<String,Integer> indexes;
	
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
		this.names = names;
		this.types = types;
		this.indexes = new HashMap<String,Integer>();

		// install map
		int size = names.length;
		for (int i=0; i<size; i++) {
			indexes.put(names[i],i);
		}
	}
	
	/** Returns names. */
	public String[] getNames() {
		return names;
	}

	/** Returns index of a name. */
	protected int getIndexOf(String name, boolean check) {
		Integer i = indexes.get(name);
		if (i==null) {
			if (check) {
				throw new IllegalArgumentException("Unknown attribute " + name);
			} else {
				return -1;
			}
		}
		return i;
	}
	
	/** Returns the type of an attribute. */
	public Class getTypeOf(String name) {
		return types[getIndexOf(name,true)];
	}
	
	/** Returns equivalent map. */
	public Map<String,Class> asMap() {
		Map<String,Class> map = new HashMap<String,Class>();
		int size = names.length;
		for (int i=0; i<size; i++) {
			map.put(names[i], types[i]);
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
	
}
