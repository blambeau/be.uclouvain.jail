package be.uclouvain.jail.algo.fa.determinize;

import junit.framework.TestCase;
import be.uclouvain.jail.algo.fa.equiv.DFAEquiv;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.INFA;
import be.uclouvain.jail.fa.impl.GraphNFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.tests.JailTestUtils;

/** Tests the NFA determinizer algorithm. */
public class NFADeterminizerAlgoTest extends TestCase {

	/** Asserts that two DFAs are equivalent. */
	public void assertEquivalent(IDFA dfa1, IDFA dfa2) throws Exception {
		assertTrue(new DFAEquiv(dfa1,dfa2).areEquivalent());
	}

	/** Tests the determinizer algorithm. */
	public void testDeterminizer(INFA nfa, IDFA expected) throws Exception {
		IDFA eqDfa = new NFADeterminizer(nfa).getResultingDFA();
		assertEquivalent(expected,eqDfa);
	}
	
	/** Tests the NFA automaton. */
	public void testDeterminizerOnSimpleExamples() throws Exception {
		INFA nfa = JailTestUtils.SINGLE_NFA();
		IDFA dfa = JailTestUtils.SINGLE_DFA();
		testDeterminizer(nfa,dfa);
	}

	/** Tests the NFA automaton. */
	public void testDeterminizerOnHopcrofExamples() throws Exception {
		INFA nfa = JailTestUtils.HOP_NFA56();
		IDFA dfa = JailTestUtils.HOP_DFA63();
		testDeterminizer(nfa,dfa);
	}
	
	/** Just checks that the determinizer does not throw an exception
	 * on NFAs. */
	public void testDeterminizerOnNFAs() throws Exception {
		INFA[] nfas = JailTestUtils.getAllNFAs();
		for (INFA nfa: nfas) {
			IDFA dfa = new NFADeterminizer(nfa).getResultingDFA();
			assertNotNull(dfa);
		}
	}
	
	/** Just checks that determinizer does not break DFAs. */
	public void testDeterminizerOnDFAs() throws Exception {
		IDFA[] dfas = JailTestUtils.getAllDFAs();
		for (IDFA dfa: dfas) {
			IDirectedGraph graph = dfa.getGraph();
			INFA nfa = new GraphNFA(graph);
			testDeterminizer(nfa,dfa);
		}
	}
	
	
}
