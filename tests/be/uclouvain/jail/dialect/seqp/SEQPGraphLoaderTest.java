package be.uclouvain.jail.dialect.seqp;

import junit.framework.TestCase;
import net.chefbe.autogram.ast2.IASTNode;
import net.chefbe.autogram.ast2.parsing.active.ASTLoader;
import net.chefbe.autogram.ast2.parsing.active.ASTLoader.EnumTypeResolver;
import net.chefbe.autogram.ast2.parsing.peg.Input;
import net.chefbe.autogram.ast2.parsing.peg.Pos;
import be.uclouvain.jail.algo.fa.equiv.DFAEquiv;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.graph.adjacency.AdjacencyDirectedGraph;
import be.uclouvain.jail.graph.deco.DirectedGraph;
import be.uclouvain.jail.graph.utils.ITotalOrder;
import be.uclouvain.jail.tests.JailTestUtils;

/** Tests SEQPGraphLoader class. */
public class SEQPGraphLoaderTest extends TestCase {

	/** Some interesting situations. */
	private String[] situations = new String[]{
		"INIT=a.",        
		"INIT=a->b."
	};
	
	/** Parses a situation. */
	private IASTNode parse(String situation) throws Exception {
		SEQPParser parser = new SEQPParser();
		parser.setActiveLoader(
			new ASTLoader(new EnumTypeResolver<SEQPNodes>(SEQPNodes.class))
		);
		Pos pos = new Pos(Input.input(situation),0);
		return (IASTNode) parser.pSeqpDef(pos);
	}
	
	/** Tests a situation. */
	public DirectedGraph testSituation(int index) throws Exception {
		DirectedGraph graph = new DirectedGraph(new AdjacencyDirectedGraph());
		IASTNode root = parse(situations[index]);
		root.accept(new SEQPGraphLoader(graph,null,null));
		return graph;
	}
	
	/** Tests situation 1. */
	public void testSituation1() throws Exception {
		DirectedGraph g = testSituation(0);
		assertEquals(2,g.getVerticesCount());
		assertEquals(1,g.getEdgesCount());

		ITotalOrder vertices = g.getVerticesTotalOrder();
		ITotalOrder edges = g.getEdgesTotalOrder();
		Object v1 = vertices.getElementAt(0);
		Object v2 = vertices.getElementAt(1);
		Object e1 = edges.getElementAt(0);
		assertEquals(v1,g.getEdgeSource(e1));
		assertEquals(v2,g.getEdgeTarget(e1));
	}
	
	/** Tests situation 2. */
	public void testSituation2() throws Exception {
		DirectedGraph g = testSituation(1);
		assertEquals(3,g.getVerticesCount());
		assertEquals(2,g.getEdgesCount());
	}
	
	/** Tests empty accepting DFA. */
	public void testEmptyDFA() throws Exception {
		IDFA tested = JailTestUtils.loadSeqPDFA("QO[@kind='ACCEPTING'].");
		IDFA reference = JailTestUtils.EMPTY_DFA();
		assertTrue(DFAEquiv.isEquivalentTo(tested, reference));
	}
	
}
