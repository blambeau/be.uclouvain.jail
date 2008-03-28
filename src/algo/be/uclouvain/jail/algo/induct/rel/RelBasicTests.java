package be.uclouvain.jail.algo.induct.rel;

import junit.framework.TestCase;

/**
 * Tests Rel base classes. 
 * 
 * @author blambeau
 */
public class RelBasicTests extends TestCase {

	/** Tests the heading. */
	public void testHeading() {
		RelFactory rel = new RelFactory("source","letter","target");
		
		// create a heading
		Heading heading = rel.heading(Integer.class, String.class, Integer.class);

		// create a relation
		Relation r = new Relation(heading);
		
		// add some tuples
		r.addTuple(rel.tuple(1,"a",2));
		r.addTuple(rel.tuple(2,"b",1));
		r.addTuple(rel.tuple(2,"c",2));
		r.addTuple(rel.tuple(1,"a",2));
		r.addTuple(rel.tuple(1,"f",3));
		r.addTuple(rel.tuple(1,"a",2));
		r.addTuple(rel.tuple(3,"a",2));
		r.addTuple(rel.tuple(1,"a",2));
		
		System.out.println(r);
	}
	
}
