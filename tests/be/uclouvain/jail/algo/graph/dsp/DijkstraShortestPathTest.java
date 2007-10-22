package be.uclouvain.jail.algo.graph.dsp;

import junit.framework.TestCase;
import be.uclouvain.jail.algo.graph.shortest.dsp.DefaultDSPInput;
import be.uclouvain.jail.algo.graph.shortest.dsp.DefaultDSPOutput;
import be.uclouvain.jail.algo.graph.shortest.dsp.DijkstraShortestPath;
import be.uclouvain.jail.algo.graph.shortest.dsp.IDSPInput;
import be.uclouvain.jail.algo.graph.shortest.dsp.IDSPOutput;
import be.uclouvain.jail.algo.graph.shortest.dsp.InGraphDSPOutput;

/** Test DijkstraShortestPath algorithm. */
public class DijkstraShortestPathTest extends TestCase {

	/** Directed graph instance. */
	private CitiesDirectedGraph graph;
	
	/** Creates an input graph. */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		graph = new CitiesDirectedGraph();
	}

	/** Asserts that some distance is the following. */
	private void assertDistance(String label, Integer dist, IDSPOutput<Integer> output) {
		Object vertex = graph.getVertex(label);
		assertEquals(dist,output.getDistance(vertex));
	}
	
	/** Asserts that the algo output is correct. */
	private void assertCorrectOutput(IDSPOutput<Integer> output) {
		assertDistance("ORD",621,output);
		assertDistance("BWI",0,output);
		assertDistance("JFK",184,output);
		assertDistance("PVD",328,output);
		assertDistance("BOS",371,output);
		assertDistance("SFO",2467,output);
		assertDistance("LAX",2658,output);
		assertDistance("DFW",1423,output);
		assertDistance("MIA",946,output);
		
		Object root = graph.getVertex("BWI");
		for (Object vertex: graph.getVertices()) {
			if (root == vertex) { return; }
			Object edge = output.getIncommingEdge(vertex);
			assertNotNull("One incoming edge for vertex " + graph.getCityName(vertex),edge);
		}
	}
	
	/** Tests DijsktraShortestPath with default input and output. */
	@SuppressWarnings("unchecked")
	public void testDefaultClasses() {
		IDSPInput<Integer> input = new DefaultDSPInput(graph,graph.getVertex("BWI"),"distance");
		IDSPOutput<Integer> output = new DefaultDSPOutput<Integer>();
		new DijkstraShortestPath<Integer>().execute(input,output);
		assertCorrectOutput(output);
	}
	
	/** Tests DijsktraShortestPath with InGraphDSPOutput. */
	@SuppressWarnings("unchecked")
	public void testInGraphDSPOutput() {
		IDSPInput<Integer> input = new DefaultDSPInput(graph,graph.getVertex("BWI"),"distance");
		IDSPOutput<Integer> output = new InGraphDSPOutput<Integer>(graph,"distance","inedge");
		new DijkstraShortestPath<Integer>().execute(input,output);
		assertCorrectOutput(output);
	}
	
}
