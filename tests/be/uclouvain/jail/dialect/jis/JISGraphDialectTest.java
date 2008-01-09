package be.uclouvain.jail.dialect.jis;

import java.io.IOException;

import junit.framework.TestCase;
import net.chefbe.autogram.ast2.parsing.ParseException;
import be.uclouvain.jail.algo.fa.equiv.DFAEquiv;
import be.uclouvain.jail.algo.induct.sample.ISample;
import be.uclouvain.jail.algo.induct.sample.ISampleString;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.tests.JailTestUtils;

/**
 * Tests JIS Graph Dialect.
 * 
 * @author blambeau
 */
public class JISGraphDialectTest extends TestCase {

	/** Tests parsing the empty sample. */
	public void testOnEmptySample() throws IOException, ParseException {
		String input = "";
		ISample sample = new JISGraphDialect().parse(input);
		assertEquals(0,sample.size());
	}

	/** Tests parsing a sample with empty positive string. */
	public void testOnEmptyPositiveStringSample() throws IOException, ParseException {
		String input = "+";
		
		// sample contains one string 
		ISample<String> sample = new JISGraphDialect().parse(input);
		assertEquals(1,sample.size());
		
		// string is of size 0
		ISampleString<String> s = sample.iterator().next();
		assertEquals(0,s.size());
		assertTrue(s.isPositive());
		assertFalse(s.isNegative());
		
		// assert equivalence with EMPTY DFA
		IDFA pta = (IDFA) sample.adapt(IDFA.class);
		IDFA dfa = JailTestUtils.EMPTY_DFA();
		assertTrue(DFAEquiv.isEquivalentTo(pta, dfa));
	}
	
	/** Tests parsing a sample with empty strings. */
	public void testOnEmptyNegativeStringSample() throws IOException, ParseException {
		// CHECK ON ONE NEGATIVE EMPTY STRING
		String input = "-";
		
		// sample contains one string 
		ISample<String> sample = new JISGraphDialect().parse(input);
		assertEquals(1,sample.size());
		
		// string is of size 0
		ISampleString<String> s = sample.iterator().next();
		assertEquals(0,s.size());
		assertFalse(s.isPositive());
		assertTrue(s.isNegative());

		// assert equivalence with EMPTY NEG DFA
		IDFA pta = (IDFA) sample.adapt(IDFA.class);
		IDFA dfa = JailTestUtils.EMPTY_NEG_DFA();
		assertTrue(DFAEquiv.isEquivalentTo(pta, dfa));
	}
	
	/** Tests parsing a sample with one positive string of size 1. */
	public void testOnOnePositiveStringSample() throws IOException, ParseException {
		String input = "+ a";
		
		// sample contains one string 
		ISample<String> sample = new JISGraphDialect().parse(input);
		assertEquals(1,sample.size());
		
		// string is of size 0
		ISampleString<String> s = sample.iterator().next();
		assertEquals(1,s.size());
		assertTrue(s.isPositive());
		assertFalse(s.isNegative());
		assertEquals("a", s.iterator().next());
		
		// assert equivalence with EMPTY DFA
		IDFA pta = (IDFA) sample.adapt(IDFA.class);
		IDFA dfa = JailTestUtils.loadSeqPDFA("Q0[@kind='PASSAGE'] = a->Q1, Q1[@kind='ACCEPTING'].");
		assertTrue(DFAEquiv.isEquivalentTo(pta, dfa));
	}
	
}
