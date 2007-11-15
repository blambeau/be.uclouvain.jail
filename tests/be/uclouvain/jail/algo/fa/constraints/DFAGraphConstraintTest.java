package be.uclouvain.jail.algo.fa.constraints;

import junit.framework.TestCase;
import be.uclouvain.jail.algo.graph.shortest.dsp.CitiesDirectedGraph;
import be.uclouvain.jail.dialect.dot.DOTDirectedGraphLoader;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.INFA;
import be.uclouvain.jail.fa.constraints.DFAGraphConstraint;
import be.uclouvain.jail.fa.impl.GraphDFA;
import be.uclouvain.jail.fa.impl.GraphNFA;
import be.uclouvain.jail.graph.IDirectedGraph;

/** Tests DFAGraphConstraint class. */
public class DFAGraphConstraintTest extends TestCase {

	/** A graph. */
	private IDirectedGraph graph;
	
	/** A nfa. */
	private INFA nfa;

	/** a DFA. */
	private IDFA dfa;
	
	/** Loads NFA.dot and DFA.dot */
	@Override
	protected void setUp() throws Exception {
		nfa = new GraphNFA();
		DOTDirectedGraphLoader.loadGraph(nfa.getGraph(),DFAGraphConstraintTest.class.getResource("NFA.dot"));
		dfa = new GraphDFA();
		DOTDirectedGraphLoader.loadGraph(dfa.getGraph(),DFAGraphConstraintTest.class.getResource("DFA.dot"));
		graph = new CitiesDirectedGraph();
	}

	/** Checks that it correctly answers. */
	public void testIsRespectedBy() {
		DFAGraphConstraint c = new DFAGraphConstraint();
		assertTrue(c.isRespectedBy(dfa.getGraph()));
		assertFalse(c.isRespectedBy(nfa.getGraph()));
		assertFalse(c.isRespectedBy(graph));
	}
	
}
