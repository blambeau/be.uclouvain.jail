package be.uclouvain.jail.fa.utils;

import junit.framework.TestCase;
import be.uclouvain.jail.graph.utils.ITotalOrder;

/**
 * Tests IntegerAlphabet class.
 * 
 * @author blambeau
 */
public class IntegerAlphabetTest extends TestCase {

	/** Tests an integer alphabet. */
	public void testIntegerAlphabet(IntegerAlphabet alphabet, int start, int offset, int size) {
		// test iterable
		int i=start; 
		for (Integer letter: alphabet) {
			assertEquals(new Integer(i),letter);
			i+=offset;
		}

		// tests comparable
		Integer previous = null;
		for (Integer current: alphabet) {
			if (previous != null) {
				assertTrue(alphabet.compare(previous, current) < 0);
			}
			previous = current;
		}

		// test total order
		ITotalOrder<Integer> order = alphabet.getLetters();
		Object[] letters = order.getTotalOrder();
		assertEquals(size,order.size());
		for (int j=0; j<size; j++) {
			Integer expected = new Integer(start+(j*offset)); 
			assertEquals(expected,order.getElementAt(j));
			assertEquals(j,order.indexOf(expected));
			assertEquals(expected.intValue(),letters[j]);
		}
	}
	
	/** Tests the default alphabet with a normal size. */
	public void testDefaultAlphabet() {
		int SIZE = 10;
		IntegerAlphabet alphabet = new IntegerAlphabet(SIZE);
		testIntegerAlphabet(alphabet,0,1,SIZE);
	}
	
	/** Tests the one size alphabet. */
	public void testOneSizeDefaultAlphabet() {
		int SIZE = 1;
		IntegerAlphabet alphabet = new IntegerAlphabet(SIZE);
		testIntegerAlphabet(alphabet,0,1,SIZE);
	}
	
	/** Tests the empty alphabet. */
	public void testEmptyDefaultAlphabet() {
		int SIZE = 0;
		IntegerAlphabet alphabet = new IntegerAlphabet(SIZE);
		testIntegerAlphabet(alphabet,0,1,SIZE);
	}
	
	/** Tests some complex alphabets. */
	public void testComplexAlphabet() {
		int start = 0;
		int offset = 1;
		int size = 0;

		size = 0;
		testIntegerAlphabet(new IntegerAlphabet(start,offset,size),start,offset,size);
		size = 1;
		testIntegerAlphabet(new IntegerAlphabet(start,offset,size),start,offset,size);
		size = 2;
		testIntegerAlphabet(new IntegerAlphabet(start,offset,size),start,offset,size);
		size = 999;
		testIntegerAlphabet(new IntegerAlphabet(start,offset,size),start,offset,size);

		start = 1000;
		size = 0;
		testIntegerAlphabet(new IntegerAlphabet(start,offset,size),start,offset,size);
		size = 1;
		testIntegerAlphabet(new IntegerAlphabet(start,offset,size),start,offset,size);
		size = 2;
		testIntegerAlphabet(new IntegerAlphabet(start,offset,size),start,offset,size);
		size = 999;
		testIntegerAlphabet(new IntegerAlphabet(start,offset,size),start,offset,size);

		offset = 5;
		size = 0;
		testIntegerAlphabet(new IntegerAlphabet(start,offset,size),start,offset,size);
		size = 1;
		testIntegerAlphabet(new IntegerAlphabet(start,offset,size),start,offset,size);
		size = 2;
		testIntegerAlphabet(new IntegerAlphabet(start,offset,size),start,offset,size);
		size = 999;
		testIntegerAlphabet(new IntegerAlphabet(start,offset,size),start,offset,size);

		offset = 10;
		size = 0;
		testIntegerAlphabet(new IntegerAlphabet(start,offset,size),start,offset,size);
		size = 1;
		testIntegerAlphabet(new IntegerAlphabet(start,offset,size),start,offset,size);
		size = 2;
		testIntegerAlphabet(new IntegerAlphabet(start,offset,size),start,offset,size);
		size = 999;
		testIntegerAlphabet(new IntegerAlphabet(start,offset,size),start,offset,size);

		start = 0;
		size = 0;
		testIntegerAlphabet(new IntegerAlphabet(start,offset,size),start,offset,size);
		size = 1;
		testIntegerAlphabet(new IntegerAlphabet(start,offset,size),start,offset,size);
		size = 2;
		testIntegerAlphabet(new IntegerAlphabet(start,offset,size),start,offset,size);
		size = 999;
		testIntegerAlphabet(new IntegerAlphabet(start,offset,size),start,offset,size);

		start = 1;
		size = 0;
		testIntegerAlphabet(new IntegerAlphabet(start,offset,size),start,offset,size);
		size = 1;
		testIntegerAlphabet(new IntegerAlphabet(start,offset,size),start,offset,size);
		size = 2;
		testIntegerAlphabet(new IntegerAlphabet(start,offset,size),start,offset,size);
		size = 999;
		testIntegerAlphabet(new IntegerAlphabet(start,offset,size),start,offset,size);
	}
	
}
