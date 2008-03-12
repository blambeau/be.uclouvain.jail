package be.uclouvain.jail.fa.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;
import be.uclouvain.jail.fa.IAlphabet;
import be.uclouvain.jail.fa.ISample;
import be.uclouvain.jail.fa.IString;

/**
 * Tests the DefaultSample class.
 * 
 * @author blambeau
 */
public class DefaultSampleTest extends TestCase {

	private IAlphabet<String> alphabet = new StaticAlphabet<String>("a","b");
	
	/** Creates a sample. */
	private ISample<String> createSample() {
		return new DefaultSample<String>(alphabet);
	}
	
	/** Creates a string instance. */
	private IString<String> string(String...ls) {
		List<String> letters = new ArrayList<String>();
		if (ls != null) {
			for (String l: ls) {
				letters.add(l);
			}
		}
		return alphabet.string(letters);
	}
	
	/** Tests the empty sample. */
	public void testEmptySample() {
		ISample<String> sample = createSample();
		
		// test size
		assertEquals("Empty sample has a 0 size.",0,sample.size());
		
		// test iterator
		assertFalse("Empty sample has no string in the iterator.",sample.iterator().hasNext());
		
		// test contains
		assertFalse("Empty sample does not contain the empty string.",sample.contains(string()));
		assertFalse("Empty sample does not contain a non empty string.",sample.contains(string("a")));
	}
	
	/** Tests a sample with one empty string only. */
	public void testOneEmptyStringSample() {
		ISample<String> sample = createSample();
		sample.addString(string());
		
		// test size
		assertEquals("One empty string sample has a 1 size.",1,sample.size());
		
		// test iterator
		Iterator<IString<String>> it = sample.iterator();
		assertTrue("One empty string sample has one string in the iterator.",it.hasNext());
		IString<String> s = it.next();
		assertEquals("String is the empty one",0,s.size());
		assertFalse("One empty string sample does not have a second string in the iterator.",it.hasNext());
		
		// test contains
		assertTrue("One empty string sample contains the empty string.",sample.contains(string()));
		assertFalse("One empty string sample does not contain a non empty string.",sample.contains(string("a")));
		assertFalse("One empty string sample does not contain a non empty string.",sample.contains(string("a","b")));
	}
	
	/** Tests a sample with overlapping of strings. */
	public void testOverlapingStringsSample() {
		ISample<String> sample = createSample();
		sample.addString(string("a"));
		sample.addString(string("b"));
		sample.addString(string("a","b","a","b","a","b"));

		// test size at this stage
		assertEquals("Overlapping sample has a 3 size at this stage.",3,sample.size());
		
		// test iterator at this stage
		Iterator<IString<String>> it = sample.iterator();
		assertEquals("First string is [a]",string("a"),it.next());
		assertEquals("Second string is [a b a b a b]",string("a","b","a","b","a","b"),it.next());
		assertEquals("Third string is [b]",string("b"),it.next());
		
		// test contains at this stage
		assertFalse("Overlapping sample does not contain the empty string",sample.contains(string()));
		assertTrue("Overlapping sample contains [a],",sample.contains(string("a")));
		assertTrue("Overlapping sample contains [a b a b a b],",sample.contains(string("a","b","a","b","a","b")));
		assertTrue("Overlapping sample contains [b],",sample.contains(string("b")));
		
		// now, add a real overlapping string
		sample.addString(string("a", "b", "a"));
		
		// test size at this stage
		assertEquals("Overlapping sample has a 4 size at this stage.",4,sample.size());
		
		// test contains at this stage
		assertFalse("Overlapping sample does not contain the empty string",sample.contains(string()));
		assertTrue("Overlapping sample contains [a],",sample.contains(string("a")));
		assertTrue("Overlapping sample contains [a b a],",sample.contains(string("a","b","a")));
		assertTrue("Overlapping sample contains [a b a b a b],",sample.contains(string("a","b","a","b","a","b")));
		assertTrue("Overlapping sample contains [b],",sample.contains(string("b")));
	}
	
	/** Main method. */
	public static void main(String[] args) {
		new DefaultSampleTest().testOverlapingStringsSample();
	}
	
}
