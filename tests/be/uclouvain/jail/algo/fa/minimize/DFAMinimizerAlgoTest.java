package be.uclouvain.jail.algo.fa.minimize;

import java.util.Set;

import junit.framework.TestCase;
import be.uclouvain.jail.Jail;
import be.uclouvain.jail.algo.fa.equiv.DFAEquiv;
import be.uclouvain.jail.dialect.IPrintable;
import be.uclouvain.jail.dialect.dot.DOTDirectedGraphLoader;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.impl.GraphDFA;

/** Tests the NFA determinizer algorithm. */
public class DFAMinimizerAlgoTest extends TestCase {

	/** Expected minimal DFA. */
	private IDFA expected;
	
	/** Non minimal DFA. */
	private IDFA dfa;
	
	/** Loads NFA.dot and DFA.dot */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		dfa = new GraphDFA();
		DOTDirectedGraphLoader.loadGraph(dfa.getGraph(),DFAMinimizerAlgoTest.class.getResource("DFA.dot"));
		expected = new GraphDFA();
		DOTDirectedGraphLoader.loadGraph(expected.getGraph(),DFAMinimizerAlgoTest.class.getResource("EXPECTED.dot"));
	}
	
	/** Debugs a DFA. */
	@SuppressWarnings("unused")
	private void debug(IDFA dfa) throws Exception {
		Jail.install();
		IPrintable p = (IPrintable) dfa.getGraph().adapt(IPrintable.class);
		Jail.setProperty("DirectedGraphPrintable.dot.edge.label.uinfo","letter");
		p.print(System.out);
	}
	
	/** Tests tau remover on identity. */
	public void testMinimizerOnExpected() throws Exception {
		DFAMinimizer minimizer = new DFAMinimizer(expected);
		IBlockStructure<Object> equiv = minimizer.getStatePartition();
		
		// check partition
		assertEquals(expected.getGraph().getVerticesTotalOrder().size(),equiv.size());
		for (Set<Object> block: equiv) {
			assertEquals(1,block.size());
		}
		
		// check DFA
		IDFA dfa = minimizer.getMinimalDFA();
		assertTrue(new DFAEquiv(expected,dfa).areEquivalent());
	}
	
	/** Tests the determinizer algorithm. */
	public void testMinimizer() throws Exception {
		DFAMinimizer minimizer = new DFAMinimizer(dfa);
		IBlockStructure struct = minimizer.getStatePartition();
		
		// checks number of vertices and edges
		assertEquals(expected.getGraph().getVerticesTotalOrder().size(),struct.size());

		// check DFA
		IDFA equiv = minimizer.getMinimalDFA();
		assertTrue(new DFAEquiv(expected,equiv).areEquivalent());
	}
	
	
}
