package be.uclouvain.jail.algo.graph.connex;

import junit.framework.TestCase;
import be.uclouvain.jail.algo.graph.shortest.dsp.CitiesDirectedGraph;

/** Tests ConnexGraphConstraint. */
public class ConnexGraphConstraintTest extends TestCase {

	/** Cities graph. */
	private CitiesDirectedGraph cities;
		
	/** Sets the test up. */
	@Override
	protected void setUp() throws Exception {
		cities = new CitiesDirectedGraph();
	}
	
	/** Checks that the constraint returns true on a connex graph. */
	public void testOnConnexGraph() {
		assertTrue(new ConnexGraphConstraint().isRespectedBy(cities));
	}
	
}
