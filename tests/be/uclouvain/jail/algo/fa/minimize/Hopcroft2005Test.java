package be.uclouvain.jail.algo.fa.minimize;

import junit.framework.TestCase;
import be.uclouvain.jail.algo.fa.equiv.DFAEquiv;
import be.uclouvain.jail.algo.graph.utils.GraphMemberGroup;
import be.uclouvain.jail.algo.graph.utils.IGraphMemberGroup;
import be.uclouvain.jail.algo.graph.utils.IGraphPartition;
import be.uclouvain.jail.dialect.seqp.SEQPGraphLoader;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.impl.GraphDFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.constraints.AbstractGraphConstraint;
import be.uclouvain.jail.graph.constraints.GraphUniqueIndex;
import be.uclouvain.jail.graph.deco.DirectedGraph;

/**
 * Minimization tests, inspired from Hopcroft 2005 book.
 * 
 * @author blambeau
 */
public class Hopcroft2005Test extends TestCase {

	/** DFA of page 155. */
	private final String DFA155 = "A [@kind='PASSAGE']    = l0->B|l1->F,"
                                + "B [@kind='PASSAGE']    = l0->G|l1->C,"
                                + "C [@kind='ACCEPTING']  = l0->A|l1->C,"
                                + "D [@kind='PASSAGE']    = l0->C|l1->G,"
                                + "E [@kind='PASSAGE']    = l0->H|l1->F,"
                                + "F [@kind='PASSAGE']    = l0->C|l1->G,"
                                + "G [@kind='PASSAGE']    = l0->G|l1->E,"
                                + "H [@kind='PASSAGE']    = l0->G|l1->C."; 
	
	/** Equivalent DFA of page 162. */
	private final String DFA162 = "AE [@kind='PASSAGE']    = l0->BH|l1->DF,"
                                + "G  [@kind='PASSAGE']    = l0->G|l1->AE,"
                                + "DF [@kind='PASSAGE']    = l0->C|l1->G,"
                                + "BH [@kind='PASSAGE']    = l0->G|l1->C,"
                                + "C  [@kind='ACCEPTING']  = l0->AE|l1->C.";
	
	/** Tests returned partition. */
	public void testPartition() throws Exception {
		IDFA dfa155 = new GraphDFA(SEQPGraphLoader.load(DFA155));
		
		IDirectedGraph dfag = dfa155.getGraph();
		DirectedGraph g = (DirectedGraph) dfag.adapt(DirectedGraph.class);
		GraphUniqueIndex index = new GraphUniqueIndex(AbstractGraphConstraint.VERTEX,"label",true).installOn(g);
		
		// partition it
		IGraphPartition p = new DFAMinimizer(dfa155).getStatePartition();
		assertEquals(5,p.size());
		
		// get states
		Object A = index.getVertex("A");
		Object B = index.getVertex("B");
		Object C = index.getVertex("C");
		Object D = index.getVertex("D");
		Object E = index.getVertex("E");
		Object F = index.getVertex("F");
		Object G = index.getVertex("G");
		Object H = index.getVertex("H");
		
		// create groups manually
		IGraphMemberGroup gAE = new GraphMemberGroup(dfag,g.getVerticesTotalOrder());
		gAE.addMembers(A,E);
		IGraphMemberGroup gG = new GraphMemberGroup(dfag,g.getVerticesTotalOrder());
		gG.addMembers(G);
		IGraphMemberGroup gDF = new GraphMemberGroup(dfag,g.getVerticesTotalOrder());
		gDF.addMembers(D,F);
		IGraphMemberGroup gBH = new GraphMemberGroup(dfag,g.getVerticesTotalOrder());
		gBH.addMembers(B,H);
		IGraphMemberGroup gC = new GraphMemberGroup(dfag,g.getVerticesTotalOrder());
		gC.addMembers(C);
		
		// check conformance
		assertEquals(gAE,p.getGroupOf(A));
		assertEquals(gBH,p.getGroupOf(B));
		assertEquals(gC,p.getGroupOf(C));
		assertEquals(gDF,p.getGroupOf(D));
		assertEquals(gAE,p.getGroupOf(E));
		assertEquals(gDF,p.getGroupOf(F));
		assertEquals(gG,p.getGroupOf(G));
		assertEquals(gBH,p.getGroupOf(H));
	}
	
	/** Tests the minimizer. */
	public void testMinimizer() throws Exception {
		IDFA dfa155 = new GraphDFA(SEQPGraphLoader.load(DFA155));
		IDFA expected = new GraphDFA(SEQPGraphLoader.load(DFA162));
		IDFA min = new DFAMinimizer(dfa155).getMinimalDFA();
		assertTrue(new DFAEquiv(expected,min).areEquivalent());
	}
	
}
