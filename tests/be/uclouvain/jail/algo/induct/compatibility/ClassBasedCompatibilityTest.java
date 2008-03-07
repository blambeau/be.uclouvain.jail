package be.uclouvain.jail.algo.induct.compatibility;

import java.util.TreeSet;

import junit.framework.TestCase;
import be.uclouvain.jail.algo.induct.compatibility.ClassBasedCompatibility;

/** Tests some methods of ClassBasedCompatibility. */
public class ClassBasedCompatibilityTest extends TestCase {

	/** Tests dijointness test method. */
	public void testIsDisjoint() {
		ClassBasedCompatibility c = new ClassBasedCompatibility();
		
		TreeSet<Integer> ts1 = new TreeSet<Integer>();
		TreeSet<Integer> ts2 = new TreeSet<Integer>();
		
		assertTrue("Empty sets are disjoint.", c.isDisjoint(ts1,ts2));
		ts1.add(1);
		
		assertFalse("Set with itself is not disjoint.", c.isDisjoint(ts1,ts1));
		assertTrue("One empty set => disjoints.", c.isDisjoint(ts1,ts2));
		
		ts2.add(1);
		assertFalse("1 is in common => not disjoint.", c.isDisjoint(ts1,ts2));
		ts2.remove(1);
		
		ts2.add(2);
		assertTrue("{1} vs. {2} => disjoints.", c.isDisjoint(ts1,ts2));
		
		ts1.add(7);
		ts1.add(5);
		ts2.add(3);
		ts2.add(9);
		assertTrue("{1,5,7} vs. {2,3,9} => disjoints.", c.isDisjoint(ts1,ts2));
		
		ts2.add(7);
		assertFalse("{1,5,7} vs. {2,3,7,9} => not disjoints.", c.isDisjoint(ts1,ts2));
	}
	
}
