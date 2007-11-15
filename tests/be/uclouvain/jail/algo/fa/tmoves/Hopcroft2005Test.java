package be.uclouvain.jail.algo.fa.tmoves;

import junit.framework.TestCase;
import be.uclouvain.jail.algo.fa.determinize.NFADeterminizer;
import be.uclouvain.jail.algo.fa.equiv.DFAEquiv;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.INFA;
import be.uclouvain.jail.tests.JailTestUtils;

/**
 * Determinization tests, inspired from Hopcroft 2005 book.
 * 
 * @author blambeau
 */
public class Hopcroft2005Test extends TestCase {

	/** Tests tau removal. */
	public void testTMoves() throws Exception {
		INFA nfa73 = JailTestUtils.HOP_NFA73();
		
		// determinize it
		IDFA dfa73 = new NFADeterminizer(nfa73).getResultingDFA();
		
		// removes tau transitions
		INFA ntnfa73 = new TauRemover(dfa73, new ITauInformer() {
			public boolean isEpsilon(Object letter) {
				return "epsilon".equals(letter);
			}
		}).getResultingNFA();
		
		// determinize result
		IDFA ntdfa73 = new NFADeterminizer(ntnfa73).getResultingDFA();
		
		// load expected 
		IDFA expected = JailTestUtils.HOP_DFA78();
		assertTrue(new DFAEquiv(expected,ntdfa73).areEquivalent());
	}
	
}
