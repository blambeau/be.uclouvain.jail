package be.uclouvain.jail.algo.fa.minimize;

import junit.framework.TestCase;
import be.uclouvain.jail.algo.fa.equiv.DFAEquiv;
import be.uclouvain.jail.dialect.seqp.SEQPGraphLoader;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.impl.GraphDFA;

/**
 * Minimization tests, inspired from Hopcroft 2005 book.
 * 
 * @author blambeau
 */
public class Hopcroft2005Test extends TestCase {

	/** DFA of page 155. */
	private final String DFA155 = "A[@isAccepting=false] = l0->B|l1->F,"
                                + "B[@isAccepting=false] = l0->G|l1->C,"
                                + "C[@isAccepting=true] = l0->A|l1->C,"
                                + "D[@isAccepting=false] = l0->C|l1->G,"
                                + "E[@isAccepting=false] = l0->H|l1->F,"
                                + "F[@isAccepting=false] = l0->C|l1->G,"
                                + "G[@isAccepting=false] = l0->G|l1->E,"
                                + "H[@isAccepting=false] = l0->G|l1->C."; 
	
	/** Equivalent DFA of page 162. */
	private final String DFA162 = "AE [@isAccepting=false] = l0->BH|l1->DF,"
                                + "G  [@isAccepting=false] = l0->G|l1->AE,"
                                + "DF [@isAccepting=false] = l0->C|l1->G,"
                                + "BH [@isAccepting=false] = l0->G|l1->C,"
                                + "C  [@isAccepting=true]  = l0->AE|l1->C.";
	
	/** Tests the minimizer. */
	public void testMinimizer() throws Exception {
		IDFA dfa155 = new GraphDFA(SEQPGraphLoader.load(DFA155));
		IDFA expected = new GraphDFA(SEQPGraphLoader.load(DFA162));
		IDFA min = new DFAMinimizer(dfa155).getMinimalDFA();
		assertTrue(new DFAEquiv(expected,min).areEquivalent());
	}
	
}
