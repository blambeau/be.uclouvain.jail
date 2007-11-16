package be.uclouvain.jail.tests;

import junit.framework.TestCase;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.constraints.GraphCheckUtils;

/**
 * Checks that properties of declared graphs and FAs is indeed respected.
 * 
 * @author blambeau
 */
public class JailTestUtilsTest extends TestCase {
	
	/** Tests that the FA graphs are indeed FAs. */
	public void testFAs() throws Exception {
		for (IDirectedGraph g: JailTestUtils.getAllDFAGraphs()) {
			assertTrue(GraphCheckUtils.isDFA(g));
		}
		for (IDirectedGraph g: JailTestUtils.getAllNFAGraphs()) {
			assertTrue(GraphCheckUtils.isFA(g));
		}
	}
	
	/** Tests that the DFA graphs are indeed DFAs. */
	public void testDFAs() throws Exception {
		for (IDirectedGraph g: JailTestUtils.getAllDFAGraphs()) {
			assertTrue(GraphCheckUtils.isDFA(g));
		}
	}
	
}
