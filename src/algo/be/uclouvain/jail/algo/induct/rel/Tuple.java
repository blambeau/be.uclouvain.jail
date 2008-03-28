package be.uclouvain.jail.algo.induct.rel;

import java.util.List;

import net.chefbe.javautils.comparisons.HashCodeUtils;

/** A tuple. */
public class Tuple {
	
	/** Heading of the tuple. */
	private Heading heading;
	
	/** Values. */
	private Object[] values;
	
	/** Creates a tuple. */
	public Tuple(Heading heading, Object...values) {
		if (heading == null) {
			throw new IllegalArgumentException("Heading cannot be null");
		}
		heading.verify(values);
		this.heading = heading;
		this.values = values;
	}

	/** Returns tuple heading. */
	public Heading getHeading() {
		return heading;
	}
	
	/** Returns the value associated to an attribute. */
	public Object getValue(String attr) {
		return values[heading.indexOf(attr, true)];
	}
	
	/** Computes an hash code. */
	private int hash = -1;
	public int hashCode() {
		if (hash == -1) {
			hash = HashCodeUtils.SEED;
			hash = HashCodeUtils.hash(hash, heading);
			for (Object v: values) {
				hash = HashCodeUtils.hash(hash, v);
			}
		}
		return hash;
	}
	
	/** Checks equality with another object. */
	public boolean equals(Object o) {
		if (o == this) { return true; }
		if (o instanceof Tuple == false) { return false; }
		Tuple t = (Tuple) o;
		if (hashCode() != t.hashCode()) { return false; }
		if (!heading.equals(t.heading)) { return false; }
		int size = values.length;
		for (int i=0; i<size; i++) {
			Object p = values[i];
			Object q = t.values[i];
			if (!(p==null) ? (q==null) : p.equals(q)) {
				return false;
			}
		}
		return true;
	}
	
	/** Returns a string representation. */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("TUPLE {");
		
		List<String> names = heading.getNames();
		int size = names.size();
		for (int i=0; i<size; i++) {
			if (i != 0) {
				sb.append(", ");
			}
			sb.append(names.get(i))
			  .append(": ")
			  .append(values[i]);
		}
		
		sb.append("}");
		return sb.toString();
	}
	
}