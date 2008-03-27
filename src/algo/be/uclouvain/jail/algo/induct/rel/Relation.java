package be.uclouvain.jail.algo.induct.rel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Provides a relation data structure.
 * 
 * @author blambeau
 */
public class Relation implements Iterable<be.uclouvain.jail.algo.induct.rel.Tuple> {

	/** Relation heading. */
	private Heading heading;
	
	/** Relation tuples. */
	private List<Tuple> tuples;
	
	/** Creates an empty relation with a given heading. */
	public Relation(Heading heading) {
		this.heading = heading;
		this.tuples = new ArrayList<Tuple>();
	}

	/** Returns relation heading. */
	public Heading getHeading() {
		return heading;
	}

	/** Returns an iterator on tuples. */
	public Iterator<Tuple> iterator() {
		return tuples.iterator();
	}

}
