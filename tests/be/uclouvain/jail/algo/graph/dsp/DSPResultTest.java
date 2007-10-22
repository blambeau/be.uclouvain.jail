package be.uclouvain.jail.algo.graph.dsp;

import junit.framework.TestCase;
import be.uclouvain.jail.algo.graph.shortest.dsp.DSPResult;
import be.uclouvain.jail.algo.graph.shortest.dsp.DefaultDSPInput;
import be.uclouvain.jail.algo.graph.shortest.dsp.DijkstraShortestPath;
import be.uclouvain.jail.algo.graph.shortest.dsp.IDSPInput;
import be.uclouvain.jail.algo.graph.shortest.dsp.IDSPOutput;
import be.uclouvain.jail.algo.graph.shortest.dsp.InGraphDSPOutput;
import be.uclouvain.jail.graph.IDirectedGraphPath;
import be.uclouvain.jail.graph.adjacency.AdjacencyDirectedGraph;
import be.uclouvain.jail.graph.utils.GraphQueryUtils;
import be.uclouvain.jail.uinfo.functions.SumFunction;

/** Tests the DSPResult class. */
public class DSPResultTest extends TestCase {

	/** Directed graph instance. */
	private CitiesDirectedGraph graph;
	
	/** Creates an input graph. */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		graph = new CitiesDirectedGraph();
	}

	/** Executes DSP on the cities graph and return a IDSPOutput. */
	@SuppressWarnings("unchecked")
	private DSPResult dsp() {
		IDSPInput<Integer> input = new DefaultDSPInput(graph,graph.getVertex("BWI"),"distance");
		IDSPOutput<Integer> output = new InGraphDSPOutput<Integer>(graph,"distance","inedge");
		new DijkstraShortestPath<Integer>().execute(input,output);
		return new DSPResult(input,output);
	}
	
	/** Tests the asSpanningTree method. */
	public void testAsSpanningTree() throws Exception {
		DSPResult result = dsp();
		AdjacencyDirectedGraph gresult = new AdjacencyDirectedGraph(); 
		result.asSpanningTree(gresult);
	}
	
	/** Tests getPathTo method. */
	public void testGetPathTo() {
		Object root = graph.getVertex("BWI");
		DSPResult result = dsp();
		
		for (Object vertex: graph.getVertices()) {
			if (vertex == root) { continue; }
			IDirectedGraphPath path = result.getShortestPathTo(vertex);
			assertEquals(root,GraphQueryUtils.getFirstVertex(path));
			assertEquals(vertex,GraphQueryUtils.getLastVertex(path));
			
			Object dist = graph.getVertexInfo(vertex).getAttribute("distance");
			Object cdist = graph.compute(path.edges(), "distance", new SumFunction());
			Object cdist2 = graph.compute(path, "distance", new SumFunction());
			assertEquals(dist,cdist);
			assertEquals(dist,cdist2);
		}
	}
	
}
