package be.uclouvain.jail.algo.induct.rel;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.chefbe.javautils.comparisons.HashCodeUtils;

/**
 * Provides a relation data structure.
 * 
 * @author blambeau
 */
public class Relation implements Iterable<Tuple> {

	/** Relation heading. */
	private Heading heading;
	
	/** Relation tuples. */
	private Set<Tuple> tuples;
	
	/** Creates an empty relation with a given heading. */
	public Relation(Heading heading) {
		this.heading = heading;
		this.tuples = new HashSet<Tuple>();
	}

	/** Returns relation heading. */
	public Heading getHeading() {
		return heading;
	}

	/** Adds a tuple. */
	public boolean addTuple(Tuple t) {
		if (!heading.equals(t.getHeading())) {
			throw new IllegalArgumentException("Not same heading.");
		}
		return tuples.add(t);
	}
	
	/** Returns an iterator on tuples. */
	public Iterator<Tuple> iterator() {
		return tuples.iterator();
	}

	/** Computes an hash code. */
	public int hashCode() {
		int hash = HashCodeUtils.SEED;
		hash = HashCodeUtils.hash(hash,heading);
		hash = HashCodeUtils.hash(hash,tuples);
		return hash;
	}
	
	/** Checks equality with another object. */
	public boolean equals(Object o) {
		if (o == this) { return true; }
		if (o instanceof Relation == false) { return false; }
		Relation r = (Relation) o;
		if (heading.equals(r.heading) == false) { return false; }
		return tuples.equals(r.tuples);
	}
	
	/** Returns a string representation. */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("RELATION ")
		  .append(heading.toString())
		  .append(" {\n");
		for (Tuple tuple: tuples) {
			sb.append("\t")
			  .append(tuple.toString())
			  .append("\n");
		}
		sb.append("}");
		return sb.toString();
	}
	
}
