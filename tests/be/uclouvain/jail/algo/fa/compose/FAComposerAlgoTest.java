package be.uclouvain.jail.algo.fa.compose;

import junit.framework.TestCase;
import be.uclouvain.jail.algo.fa.equiv.DFAEquiv;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.IFA;
import be.uclouvain.jail.fa.INFA;
import be.uclouvain.jail.tests.JailTestUtils;
import be.uclouvain.jail.vm.toolkits.AutomatonFacade;

/**
 * Tests the FA composer algorithm.
 * 
 * @author blambeau
 */
public class FAComposerAlgoTest extends TestCase {

	/** Asserts that two DFAs are equivalent. */
	public void assertEquivalent(IDFA dfa1, IDFA dfa2) throws Exception {
		assertTrue(new DFAEquiv(dfa1,dfa2).areEquivalent());
	}

	/** Execute synchronous product. */
	public DefaultFAComposerResult execSynchroProduct(IFA[] fas) {
		DefaultFAComposerInput input = new DefaultFAComposerInput(fas);
		DefaultFAComposerResult result = new DefaultFAComposerResult();
		new FAComposerAlgo().execute(input, result);
		return result;
	}
	
	/** Tests composer on identity. */
	public void testOnIdentity() throws Exception {
		for (IDFA dfa: JailTestUtils.getAllDFAs()) {
			IFA[] fas = new IFA[]{dfa, dfa};
			DefaultFAComposerResult result = execSynchroProduct(fas);
			assertEquivalent(dfa, (IDFA) result.adapt(IDFA.class));
		}
	}

	/** Tests on disjoint alphabet (cartesian product). */
	public void testOnDisjointAlphabet_DFA() throws Exception {
		IDFA d1 = JailTestUtils.loadSeqPDFA(
			"Q0[@isInitial=true @kind='ACCEPTING']   = a->Q1,   " +
	        "Q1[@isInitial=false @kind='PASSAGE']    = b->Q0.    ");
		IDFA d2 = JailTestUtils.loadSeqPDFA(
			"Q0[@isInitial=true @kind='PASSAGE']     = c->Q1,   " +
        	"Q1[@isInitial=false @kind='ACCEPTING']  = d->Q0.    ");
		IDFA expected = JailTestUtils.loadSeqPDFA(
			"Q00[@isInitial=true @kind='PASSAGE']    = a->Q10|c->Q01," +
			"Q01[@isInitial=false @kind='ACCEPTING'] = a->Q11|d->Q00," +
			"Q10[@isInitial=false @kind='PASSAGE']   = b->Q00|c->Q11," +
			"Q11[@isInitial=false @kind='PASSAGE']   = b->Q01|d->Q10."
		);		
		DefaultFAComposerResult result = execSynchroProduct(new IFA[]{d1, d2});
		assertEquivalent(expected, (IDFA) result.adapt(IDFA.class));
        //AutomatonFacade.show(d1, d2, expected, (IDFA) result.adapt(IDFA.class));
	}
	
	/** Tests on disjoint alphabet (cartesian product). */
	public void testOnSimpleNFA() throws Exception {
		INFA n1 = JailTestUtils.loadSeqPNFA(
			"Q0[@kind='ACCEPTING']   = a->Q1|a->Q2,   " +
			"Q1[@kind='PASSAGE']     = b->Q0,         " +
			"Q2[@kind='PASSAGE']     = c->Q0.         "
		);
		IDFA d2 = JailTestUtils.loadSeqPDFA(
			"Q0[@kind='ACCEPTING']   = b->Q1|c->Q2,   " +
			"Q1[@kind='PASSAGE']     = bx->Q0,        " +
			"Q2[@kind='PASSAGE']     = cx->Q0.        "
		);
		INFA expected = JailTestUtils.loadDotNFA(
			"digraph G {" +
			"	graph [rankdir='LR'];" +
			"	node [shape='circle'];" + 
			"	0 [isInitial='true' kind='ACCEPTING'];" +
			"	1 [isInitial='false' kind='PASSAGE'];" +
			"	2 [isInitial='false' kind='PASSAGE'];" +
			"	3 [isInitial='false' kind='PASSAGE'];" +
			"	4 [isInitial='false' kind='PASSAGE'];" +
			"	5 [isInitial='false' kind='PASSAGE'];" +
			"	6 [isInitial='false' kind='PASSAGE'];" +
			"	7 [isInitial='false' kind='PASSAGE'];" +
			"	8 [isInitial='false' kind='PASSAGE'];" +
			"	0 -> 1 [letter='a'];" +
			"	0 -> 2 [letter='a'];" +
			"	2 -> 3 [letter='c'];" +
			"	3 -> 4 [letter='a'];" +
			"	3 -> 5 [letter='a'];" +
			"	3 -> 0 [letter='cx'];" +
			"	4 -> 1 [letter='cx'];" +
			"	1 -> 6 [letter='b'];" +
			"	6 -> 0 [letter='bx'];" +
			"	6 -> 7 [letter='a'];" +
			"	6 -> 8 [letter='a'];" +
			"	7 -> 1 [letter='bx'];" +
			"	8 -> 2 [letter='bx'];" +
			"	5 -> 2 [letter='cx'];" +
			"	}"
		);
		DefaultFAComposerResult result = execSynchroProduct(new IFA[]{n1, d2});
		INFA result_n = (INFA) result.adapt(INFA.class); 
		IDFA expected_d = AutomatonFacade.determinize(expected);
		IDFA result_d = AutomatonFacade.determinize(result_n);
		assertEquivalent(expected_d,result_d);
	}

	
	/** Main method. */
	public static void main(String[] args) throws Exception {
		new FAComposerAlgoTest().testOnSimpleNFA();
	}
	
}
