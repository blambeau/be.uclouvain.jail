package be.uclouvain.jail.algo.graph.shortest.dsp;

import junit.framework.TestCase;
import be.uclouvain.jail.uinfo.functions.SumFunction;

/** Tests DSP class. */
public class DSPTest extends TestCase {

	/** Directed graph instance. */
	private CitiesDirectedGraph graph;
	
	/** Creates an input graph. */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		graph = new CitiesDirectedGraph();
	}

	/** Asserts that some distance is the following. */
	private void assertDistance(String label, Integer dist, DSP dsp) {
		Object vertex = graph.getVertex(label);
		assertEquals(dist,dsp.getDistance(vertex));
	}
	
	/** Tests GetDistance method. */
	public void testGetRealDistance() {
		DSP dsp = new DSP(graph,graph.getVertex("BWI"));
		dsp.attributeWeight("distance");
		assertDistance("ORD",621,dsp);
		assertDistance("BWI",0,dsp);
		assertDistance("JFK",184,dsp);
		assertDistance("PVD",328,dsp);
		assertDistance("BOS",371,dsp);
		assertDistance("SFO",2467,dsp);
		assertDistance("LAX",2658,dsp);
		assertDistance("DFW",1423,dsp);
		assertDistance("MIA",946,dsp);
	}
	
	/** Tests GetDistance method. */
	public void testGetCountDistance() {
		DSP dsp = new DSP(graph,graph.getVertex("BWI"));
		dsp.allOneWeights();
		assertDistance("ORD",1,dsp);
		assertDistance("JFK",1,dsp);
		assertDistance("PVD",2,dsp);
		assertDistance("BOS",2,dsp);
		assertDistance("BWI",0,dsp);
		assertDistance("SFO",2,dsp);
		assertDistance("DFW",2,dsp);
		assertDistance("MIA",1,dsp);
		assertDistance("LAX",3,dsp);
	}

	/** Tests markInVertices method. */
	public void testMarkInVertices() {
		DSP dsp = new DSP(graph,graph.getVertex("BWI"));

		Object root = dsp.getRootVertex();
		dsp.attributeWeight("distance");
		dsp.markInVertices("distance", "inEdge");

		for (Object vertex: graph.getVertices()) {
			assertEquals(dsp.getDistance(vertex),graph.getVertexInfo(vertex).getAttribute("distance"));
			
			if (vertex == root) {continue;}
			Object inEdge = graph.getVertexInfo(vertex).getAttribute("inEdge");
			assertCollectionContaines(graph.getIncomingEdges(vertex),inEdge);
		}
	}

	/** Asserts that some edges iterable contains the inEdge object. */
	private void assertCollectionContaines(Iterable<Object> incomingEdges, Object inEdge) {
		for (Object edge: incomingEdges) {
			if (edge == inEdge) { return; }
		}
		assertFalse("inEdge " + inEdge + " is some of the incoming edges",true);
	}

	/** Asserts that some distance is the following. */
	private void assertPathDistance(String label, Integer dist, DSP dsp) {
		Object vertex = graph.getVertex(label);
		assertEquals(dist,dsp.edgePathCompute(vertex, "distance", new SumFunction()));
	}

	/** Tests edgePathCompute method. */
	public void testEdgePathCompute() {
		DSP dsp = new DSP(graph,graph.getVertex("BWI"));
		dsp.attributeWeight("distance");
		
		assertPathDistance("ORD",621,dsp);
		assertPathDistance("BWI",0,dsp);
		assertPathDistance("JFK",184,dsp);
		assertPathDistance("PVD",328,dsp);
		assertPathDistance("BOS",371,dsp);
		assertPathDistance("SFO",2467,dsp);
		assertPathDistance("LAX",2658,dsp);
		assertPathDistance("DFW",1423,dsp);
		assertPathDistance("MIA",946,dsp);
	}
	
}
