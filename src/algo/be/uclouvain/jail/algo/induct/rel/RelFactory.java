package be.uclouvain.jail.algo.induct.rel;

import java.util.List;

import net.chefbe.javautils.collections.list.OrderedList;

/**
 * Provides a relation factory.
 *  
 * @author blambeau
 */
public class RelFactory {

	/** Heading. */
	private Heading heading;

	/** Ordered names. */
	private String[] names;
	
	/** Rebind indexes array. */
	private List<String> orderedNames;
	
	/** Creates a relation factory instance. */
	public RelFactory(String...names) {
		this.names = names;
		this.orderedNames = new OrderedList<String>(names);
	}

	/** Rebinds an index. */
	private int rebind(int i) {
		return orderedNames.indexOf(names[i]);
	}
	
	/** Sorts an array of types. */
	private Class[] sort(Class[] types) {
		int size = types.length;
		if (size != names.length) {
			throw new IllegalArgumentException("Invalid size.");
		}
		Class[] result = new Class[size];
		for (int i=0; i<size; i++) {
			result[rebind(i)] = types[i];
		}
		return result;
	}
	
	/** Sorts an array of values. */
	private Object[] sort(Object[] values) {
		int size = values.length;
		if (size != names.length) {
			throw new IllegalArgumentException("Invalid size.");
		}
		Object[] result = new Object[size];
		for (int i=0; i<size; i++) {
			result[rebind(i)] = values[i];
		}
		return result;
	}
	
	/** Installs the heading. */
	public Heading heading(Class...types) {
		types = sort(types);
		int size = names.length;
		this.heading = new Heading(orderedNames.toArray(new String[size]), types);
		return this.heading;
	}
	
	/** Creates a tuple. */
	public Tuple tuple(Object...values) {
		if (heading == null) {
			throw new IllegalStateException("Heading not installed.");
		}
		values = sort(values);
		return new Tuple(heading, values);
	}
	
}
