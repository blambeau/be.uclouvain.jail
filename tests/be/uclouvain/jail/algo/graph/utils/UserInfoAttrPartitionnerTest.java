package be.uclouvain.jail.algo.graph.utils;

import junit.framework.TestCase;
import be.uclouvain.jail.algo.graph.shortest.dsp.CitiesDirectedGraph;

/**
 * Tests UserInfoAttrPartitionner class.
 * 
 * @author blambeau
 */
public class UserInfoAttrPartitionnerTest extends TestCase {

	/** Cities graph. */
	private CitiesDirectedGraph graph;
	
	/** Installs graph. */
	@Override
	protected void setUp() throws Exception {
		graph = new CitiesDirectedGraph();
	}
	
	/** Tests partitionner on label key. */
	public void testUserInfoAttrOnLabel() {
		UserInfoAttrPartitionner p = new UserInfoAttrPartitionner(graph,"label");
		for (Object vertex: graph.getVertices()) {
			assertEquals(graph.getCityName(vertex),p.getPartitionOf(vertex));
		}
	}
	
	/** Tests partitionner on label key. */
	public void testUserInfoAttrOnIndex() {
		UserInfoAttrPartitionner p = new UserInfoAttrPartitionner(graph,"index");
		for (Object vertex: graph.getVertices()) {
			assertEquals(graph.getVerticesTotalOrder().indexOf(vertex),p.getPartitionOf(vertex));
		}
	}
	
}
