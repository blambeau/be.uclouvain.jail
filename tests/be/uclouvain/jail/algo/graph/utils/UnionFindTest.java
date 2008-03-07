package be.uclouvain.jail.algo.graph.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

/** Tests the UnionFind class. */
public class UnionFindTest extends TestCase {

	/** Asserts that the two ufds are equals according to find(). */
	private void assertValidFind(BasicUnionFind expected, UnionFind<Integer> ufds) {
		int size = expected.size();
		for (int i=0; i<size; i++) {
			assertEquals("Same find(" + i + ") on the two ufds.",
					new Integer(expected.find(i)),ufds.find(i));
		}
	}

	/** Asserts that the two ufds are equals according to list(). */
	private void assertValidSet(BasicUnionFind expected, UnionFind<Integer> ufds) {
		int size = expected.size();
		for (int i=0; i<size; i++) {
			if (expected.find(i) == i) {
				Set<Integer> expe = expected.set(i);
				Set<Integer> list = ufds.set(i);
				assertEquals("Same sets on " + i, expe, list);
			}
		}
	}
	
	/** Creates a union find instance. */
	private UnionFind<Integer> createUnionFind(int size) {
		List<Integer> members = new ArrayList<Integer>();
		for (int i=0; i<size; i++) {
			members.add(i);
		}
		return new UnionFind<Integer>(members);
	}
	
	/** Tests simple UnionFind configuration. */
	public void testConstruction() {
		int[] sizes = new int[]{0,1,2,10,100,1000};
		for (int i=0; i<sizes.length; i++) {
			UnionFind<Integer> f = createUnionFind(sizes[i]);
			assertEquals("Correct size at construction",sizes[i], f.size());

			// test each element
			for (int j=0; j<sizes[i]; j++) {
				assertEquals("Correct element at construction",new Integer(j),f.find(j));
			}
		}
	}
	
	/** Makes a Union on the two ufds. */
	private void union(UnionFind<Integer> ufds, BasicUnionFind expected, int victim, int target) {
		ufds.union(victim, target);
		expected.union(victim,target);
	}
	
	/** Tests some simple unions. */
	public void testSimpleUnions() {
		// create expected result and tested union find
		int size = 10;
		BasicUnionFind expected = new BasicUnionFind(size);
		UnionFind<Integer> f = createUnionFind(size);
		
		// identity merge
		union(f,expected,3,3);
		assertValidFind(expected,f);
		
		// merge 5 with 1
		union(f,expected,5,1);
		assertValidFind(expected,f);
		
		// no way mergei
		union(f,expected,5,1);
		assertValidFind(expected,f);

		// merge 6 with 1
		union(f,expected,6,1);
		assertValidFind(expected,f);
		
		// merge 9 with 2
		union(f,expected,2,9);
		assertValidFind(expected,f);
		
		// merge 7 with 6
		union(f,expected,6,7);
		assertValidFind(expected,f);
		
		assertValidSet(expected,f);
	}
	
	/** Tests some simple unions. */
	public void testTransactionSupport() {
		// create expected result and tested union find
		int size = 10;
		BasicUnionFind expected = new BasicUnionFind(size);
		UnionFind<Integer> f = createUnionFind(size);
		
		union(f,expected,6,2);
		union(f,expected,1,9);
		union(f,expected,9,4);
		union(f,expected,3,2);
		
		assertValidFind(expected,f);
		assertValidSet(expected,f);
		
		// start a fake transaction
		f.startTransaction();
		expected.startTransaction();
		union(f, expected, 5, 7);
		union(f, expected, 8, 5);
		assertValidFind(expected,f);
		assertValidSet(expected,f);
		f.rollback();
		expected.rollback();
		
		assertValidFind(expected,f);
		assertValidSet(expected,f);

		// start a real transaction
		f.startTransaction();
		expected.startTransaction();
		union(f, expected, 5, 7);
		union(f, expected, 8, 5);
		assertValidFind(expected,f);
		assertValidSet(expected,f);
		f.commit();
		expected.commit();

		assertValidFind(expected,f);
		assertValidSet(expected,f);
	}
	
}
