package be.uclouvain.jail.algo.fa.determinize;

import junit.framework.TestCase;
import be.uclouvain.jail.Jail;
import be.uclouvain.jail.algo.fa.equiv.DFAEquiv;
import be.uclouvain.jail.dialect.IPrintable;
import be.uclouvain.jail.dialect.dot.DOTDirectedGraphLoader;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.INFA;
import be.uclouvain.jail.fa.impl.GraphDFA;
import be.uclouvain.jail.fa.impl.GraphNFA;

/** Tests the NFA determinizer algorithm. */
public class NFADeterminizerAlgoTest extends TestCase {

	/** NFA to determinize. */
	private INFA nfa;
	
	/** Expected DFA. */
	private IDFA expected;

	/** Loads NFA.dot and DFA.dot */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		nfa = new GraphNFA();
		DOTDirectedGraphLoader.loadGraph(nfa.getGraph(),NFADeterminizerAlgoTest.class.getResource("NFA.dot"));
		expected = new GraphDFA();
		DOTDirectedGraphLoader.loadGraph(expected.getGraph(),NFADeterminizerAlgoTest.class.getResource("DFA.dot"));
	}
	
	/** Debugs a DFA. */
	@SuppressWarnings("unused")
	private void debug(IDFA dfa) throws Exception {
		Jail.install();
		IPrintable p = (IPrintable) dfa.getGraph().adapt(IPrintable.class);
		Jail.setProperty("DirectedGraphPrintable.dot.edge.label.uinfo","letter");
		p.print(System.out);
	}
	
	/** Tests the determinizer algorithm. */
	public void testDeterminizer() throws Exception {
		IDFA result = new NFADeterminizer(nfa).getResultingDFA();
		assertTrue(new DFAEquiv(expected,result).areEquivalent());
	}
	
	
}
