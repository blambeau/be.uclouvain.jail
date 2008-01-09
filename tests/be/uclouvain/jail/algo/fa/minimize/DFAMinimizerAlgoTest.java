package be.uclouvain.jail.algo.fa.minimize;

import junit.framework.TestCase;
import be.uclouvain.jail.algo.fa.equiv.DFAEquiv;
import be.uclouvain.jail.algo.graph.utils.IGraphMemberGroup;
import be.uclouvain.jail.algo.graph.utils.IGraphPartition;
import be.uclouvain.jail.dialect.dot.DOTDirectedGraphLoader;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.constraints.DFAGraphConstraint;
import be.uclouvain.jail.fa.impl.GraphDFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.tests.JailTestUtils;

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
		DOTDirectedGraphLoader.loadGraph(dfa.getGraph(),
				DFAMinimizerAlgoTest.class.getResource("DFA.dot"));
		expected = new GraphDFA();
		DOTDirectedGraphLoader.loadGraph(expected.getGraph(),
				DFAMinimizerAlgoTest.class.getResource("EXPECTED.dot"));
	}
	
	/** Tests that bugs 0001 does not reappear. */
	public void testMinimizerOnBUG0001() throws Exception {
		// load graph and check it's a DFA
		IDirectedGraph graph = DOTDirectedGraphLoader.loadGraph(
			JailTestUtils.resource(this,"BUG0001.dot"));
		assertTrue(new DFAGraphConstraint().isRespectedBy(graph));
		
		// create DFA
		IDFA dfa = new GraphDFA(graph);
		
		// minimize it
		new DFAMinimizer(dfa).getMinimalDFA();
	}
	
	/** Tests tau remover on identity. */
	public void testMinimizerOnExpected() throws Exception {
		DFAMinimizer minimizer = new DFAMinimizer(expected);
		IGraphPartition equiv = minimizer.getStatePartition();
		
		// check partition
		assertEquals(expected.getGraph().getVerticesTotalOrder().size(),equiv.size());
		for (IGraphMemberGroup block: equiv) {
			assertEquals(1,block.size());
		}
		
		// check DFA
		IDFA dfa = minimizer.getMinimalDFA();
		assertTrue(new DFAEquiv(expected,dfa).areEquivalent());
	}
	
	/** Tests the determinizer algorithm. */
	public void testMinimizer() throws Exception {
		DFAMinimizer minimizer = new DFAMinimizer(dfa);
		IGraphPartition struct = minimizer.getStatePartition();
		
		// checks number of vertices and edges
		assertEquals(expected.getGraph().getVerticesTotalOrder().size(),struct.size());

		// check DFA
		IDFA equiv = minimizer.getMinimalDFA();
		assertTrue(new DFAEquiv(expected,equiv).areEquivalent());
	}
	
}
