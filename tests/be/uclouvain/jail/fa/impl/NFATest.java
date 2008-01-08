package be.uclouvain.jail.fa.impl;

import java.util.Iterator;

import junit.framework.TestCase;
import be.uclouvain.jail.dialect.dot.DOTDirectedGraphLoader;
import be.uclouvain.jail.fa.INFA;
import be.uclouvain.jail.graph.utils.ITotalOrder;
import be.uclouvain.jail.uinfo.IUserInfoHelper;
import be.uclouvain.jail.uinfo.UserInfoHelper;

/**
 * Tests GraphNFA implementation.
 * 
 * @author blambeau
 */
public class NFATest extends TestCase {

	/** Default helper instance. */
	private IUserInfoHelper helper = UserInfoHelper.instance();
	
	/** Tests a basic NFA. */
	public void testBasicNFA() throws Exception {
		INFA nfa = new GraphNFA();
		DOTDirectedGraphLoader.loadGraph(nfa.getGraph(),
				NFATest.class.getResource("NFA.dot"),helper);
		
		// tests states
		ITotalOrder states = nfa.getGraph().getVerticesTotalOrder();
		assertEquals(3,states.size());
		
		// tests edges
		ITotalOrder edges = nfa.getGraph().getEdgesTotalOrder();
		assertEquals(5,edges.size());
		
		// tests initial states
		Iterable<Object> initialStates = nfa.getInitialStates();
		Iterator i = initialStates.iterator();
		int count = 0;
		while (i.hasNext()) {
			i.next();
			count++;
		}
		assertEquals(1,count);
	}
	
}
