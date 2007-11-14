package be.uclouvain.jail.algo.fa.determinize;

import junit.framework.TestCase;
import be.uclouvain.jail.algo.fa.equiv.DFAEquiv;
import be.uclouvain.jail.dialect.seqp.SEQPGraphLoader;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.INFA;
import be.uclouvain.jail.fa.impl.GraphDFA;
import be.uclouvain.jail.fa.impl.GraphNFA;

/**
 * Determinization tests, inspired from Hopcroft 2005 book.
 * 
 * @author blambeau
 */
public class Hopcroft2005Test extends TestCase {

	/** NFA seqp automaton of page 56. */
	private final String NFA56 = "Q0[@isAccepting=false] = a->Q0|b->Q0|a->Q1,"
	                           + "Q1[@isAccepting=false] = b->Q2."; 
	
	/** Equivalent DFA of page 63. */
	private final String DFA63 = "Q0[@isAccepting=false]  = a->Q01|b->Q0,"
	                           + "Q01[@isAccepting=false] = a->Q01|b->Q02,"
                               + "Q02 = a->Q01|b->Q0.";
	
	/** Tests the NFA automaton. */
	public void testDeterminizer() throws Exception {
		INFA nfa = new GraphNFA(SEQPGraphLoader.load(NFA56));
		IDFA dfa = new GraphDFA(SEQPGraphLoader.load(DFA63));
		IDFA eqDfa = new NFADeterminizer(nfa).getResultingDFA();
		assertTrue(new DFAEquiv(dfa,eqDfa).areEquivalent());
	}
	
}
