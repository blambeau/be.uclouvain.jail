package be.uclouvain.jail.algo.lsm;

import junit.framework.TestCase;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.graph.utils.ITotalOrder;
import be.uclouvain.jail.tests.JailTestUtils;

/**
 * Tests the total order creator.
 * 
 * @author blambeau
 */
public class TotalOrderTests extends TestCase {

	/** Asserts that two orders are equal. */
	private void assertSameOrder(ITotalOrder<Object> v1, ITotalOrder<Object> v2) {
		assertEquals("Same number of states",v1.size(),v2.size());
		int size = v1.size();
		for (int i=0; i<size; i++) {
			assertEquals("Same state",v1.getElementAt(i),v2.getElementAt(i));
		}
	}
	
	/** Tests the creator on a (already ordered) dfa. */
	private void testOn(IDFA dfa) {
		TotalOrderCreator c = new TotalOrderCreator();
		ITotalOrder<Object> o = c.compute(dfa);
		assertSameOrder(dfa.getGraph().getVerticesTotalOrder(),o);
	}

	/** Tests on a PTA. */
	public void testOnOneStateDFA() throws Exception {
		IDFA dfa = JailTestUtils.SINGLE_DFA();
		testOn(dfa);
	}
	
	/** Tests on the train. */
	public void testOnTrainDFA() throws Exception {
		IDFA dfa = JailTestUtils.loadDotDFA(JailTestUtils.resource(this, "TrainDFA_ordered.dot"));
		testOn(dfa);
	}
	
	/** Tests on the train PTA. */
	public void testOnTrainPTA() throws Exception {
		IDFA dfa = JailTestUtils.loadDotDFA(JailTestUtils.resource(this, "TrainPTA_ordered.dot"));
		testOn(dfa);
	}
	
	/** Main method. */
	public static void main(String[] args) throws Exception {
		new TotalOrderTests().testOnTrainPTA();
	}

}
