package be.uclouvain.jail.algo.fa.utils;

import junit.framework.TestCase;
import be.uclouvain.jail.fa.FAStateKind;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.impl.AttributeGraphFAInformer;
import be.uclouvain.jail.fa.impl.GraphDFA;
import be.uclouvain.jail.fa.utils.PTADepthFirst;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.utils.ITotalOrder;
import be.uclouvain.jail.tests.JailTestUtils;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.UserInfoHelper;

/**
 * Tests PTADepthFirst class.
 * 
 * @author blambeau
 */
public class PTADepthFirstTest extends TestCase {

	/** User info helper. */
	private UserInfoHelper helper = new UserInfoHelper();
	
	/** Creates a vertex info. */
	private IUserInfo vInfo(boolean init) {
		helper.addKeyValue(AttributeGraphFAInformer.STATE_INITIAL_KEY, init);
		helper.addKeyValue(AttributeGraphFAInformer.STATE_KIND_KEY,FAStateKind.ACCEPTING);
		return helper.install();
	}

	/** Creates an edge info. */
	private IUserInfo eInfo(String letter) {
		return helper.keyValue(AttributeGraphFAInformer.EDGE_LETTER_KEY, letter);
	}
	
	/** Tests it on empty PTA. */
	public void testOnEmptyPTA() {
		IDFA dfa = new GraphDFA();
		IDirectedGraph graph = dfa.getGraph();
		
		Object init = graph.createVertex(vInfo(true));
		
		PTADepthFirst it = new PTADepthFirst(dfa);
		assertTrue(it.hasNext());
		assertEquals(init, it.next());
		assertFalse(it.hasNext());
	}

	/** Tests it on a two state PTA. */
	public void testOnTwoStatesPTA() {
		IDFA dfa = new GraphDFA();
		IDirectedGraph graph = dfa.getGraph();
		
		Object v1 = graph.createVertex(vInfo(true));
		Object v2 = graph.createVertex(vInfo(false));
		graph.createEdge(v1, v2, eInfo("a"));
		
		PTADepthFirst it = new PTADepthFirst(dfa);
		assertTrue(it.hasNext());
		assertEquals(v1, it.next());
		assertTrue(it.hasNext());
		assertEquals(v2, it.next());
		assertFalse(it.hasNext());
	}

	/** Tests it on a three state PTA. */
	public void testOnTreeStatesPTA() {
		IDFA dfa = new GraphDFA();
		IDirectedGraph graph = dfa.getGraph();
		
		Object v1 = graph.createVertex(vInfo(true));
		Object v2 = graph.createVertex(vInfo(false));
		Object v3 = graph.createVertex(vInfo(false));
		graph.createEdge(v1, v3, eInfo("b"));
		graph.createEdge(v1, v2, eInfo("a"));
		
		PTADepthFirst it = new PTADepthFirst(dfa);
		assertTrue(it.hasNext());
		assertEquals(v1, it.next());
		assertTrue(it.hasNext());
		assertEquals(v2, it.next());
		assertTrue(it.hasNext());
		assertEquals(v3, it.next());
		assertFalse(it.hasNext());
	}
	
	/** Tests on PTA.dot. */
	public void testOnComplexPTA() throws Exception {
		IDFA dfa = JailTestUtils.loadDotDFA(JailTestUtils.resource(this, "pta.dot"));
		ITotalOrder<Object> states = dfa.getGraph().getVerticesTotalOrder();
		int i=0;

		PTADepthFirst it = new PTADepthFirst(dfa);
		while (it.hasNext()) {
			Object state = it.next();
			if (i==0) { 
				assertEquals("0 state is the initial one",state,dfa.getInitialState());
			}
			assertEquals("States are in order.",i,states.indexOf(state));
		}
	}

}
