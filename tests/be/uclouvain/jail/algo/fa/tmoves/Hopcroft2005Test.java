package be.uclouvain.jail.algo.fa.tmoves;

import junit.framework.TestCase;
import be.uclouvain.jail.algo.fa.determinize.NFADeterminizer;
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

	/** NFA of page 73. */
	private final String NFA73 = "Q0[@isAccepting=false] = epsilon->Q1|plus->Q1|minus->Q1,"
                               + "Q1[@isAccepting=false] = digit->Q1|dot->Q2|digit->Q4,"
                               + "Q2[@isAccepting=false] = digit->Q3,"
                               + "Q3[@isAccepting=false] = digit->Q3|epsilon->Q5,"
                               + "Q4[@isAccepting=false] = dot->Q3."; 
	
	/** Equivalent DFA of page 78. */
	private final String DFA78 = "Q01 [@isAccepting=false]  = plus->Q1|minus->Q1|digit->Q14|dot->Q2,"
                               + "Q1  [@isAccepting=false]  = digit->Q14|dot->Q2,"
                               + "Q2  [@isAccepting=false]  = digit->Q35,"
                               + "Q14 [@isAccepting=false]  = digit->Q14|dot->Q235,"
                               + "Q235                      = digit->Q35,"
                               + "Q35                       = digit->Q35.";
	

	/** Tests tau removal. */
	public void testTMoves() throws Exception {
		INFA nfa73 = new GraphNFA(SEQPGraphLoader.load(NFA73));
		IDFA dfa73 = new NFADeterminizer(nfa73).getResultingDFA();
		INFA ntnfa73 = new TauRemover(dfa73, new ITauInformer() {
			public boolean isEpsilon(Object letter) {
				return "epsilon".equals(letter);
			}
		}).getResultingNFA();
		IDFA ntdfa73 = new NFADeterminizer(ntnfa73).getResultingDFA();
		
		IDFA expected = new GraphDFA(SEQPGraphLoader.load(DFA78));
		assertTrue(new DFAEquiv(expected,ntdfa73).areEquivalent());
	}
	
}
