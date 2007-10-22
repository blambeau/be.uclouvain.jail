package be.uclouvain.jail.algo.graph.dsp;

import junit.framework.TestCase;
import be.uclouvain.jail.Jail;
import be.uclouvain.jail.algo.graph.shortest.dsp.DefaultDSPInput;
import be.uclouvain.jail.algo.graph.shortest.dsp.DijkstraShortestPath;
import be.uclouvain.jail.algo.graph.shortest.dsp.IDSPInput;
import be.uclouvain.jail.algo.graph.shortest.dsp.IDSPOutput;
import be.uclouvain.jail.algo.graph.shortest.dsp.InGraphDSPOutput;
import be.uclouvain.jail.graph.adjacency.AdjacencyDirectedGraph;
import be.uclouvain.jail.graph.deco.DirectedGraph;
import be.uclouvain.jail.graph.deco.GraphUniqueIndex;
import be.uclouvain.jail.io.IPrintable;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.MapUserInfo;

/** Test DijkstraShortestPath algorithm. */
public class DijkstraShortestPathTest extends TestCase {

	/** Directed graph instance. */
	private DirectedGraph graph;
	
	/** Vertices index. */
	private GraphUniqueIndex index;

	/** Creates a vertex info. */
	private IUserInfo vinfo(String label) {
		IUserInfo info = new MapUserInfo();
		info.setAttribute("label", label);
		return info;
	}
	
	/** Creates an edge info. */
	private IUserInfo einfo(Integer dist) {
		IUserInfo info = new MapUserInfo();
		info.setAttribute("distance", dist);
		return info;
	}
	
	/** Creates an input graph. */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		graph = (DirectedGraph) new AdjacencyDirectedGraph().adapt(DirectedGraph.class);
		index = new GraphUniqueIndex(GraphUniqueIndex.VERTEX,"label",true).installOn(graph);
		
		Object sfo = graph.createVertex(vinfo("SFO"));
		Object ord = graph.createVertex(vinfo("ORD"));
		Object lax = graph.createVertex(vinfo("LAX"));
		Object dfw = graph.createVertex(vinfo("DFW"));
		Object bos = graph.createVertex(vinfo("BOS"));
		Object pvd = graph.createVertex(vinfo("PVD"));
		Object jfk = graph.createVertex(vinfo("JFK"));
		Object bwi = graph.createVertex(vinfo("BWI"));
		Object mia = graph.createVertex(vinfo("MIA"));
		
		graph.createEdge(sfo, bos, einfo(2704));
		graph.createEdge(ord, sfo, einfo(1846));
		graph.createEdge(sfo, dfw, einfo(1464));
		graph.createEdge(sfo, lax, einfo(337));
		
		graph.createEdge(dfw, lax, einfo(1235));
		graph.createEdge(lax, mia, einfo(2342));
		
		graph.createEdge(dfw, mia, einfo(1121));
		graph.createEdge(ord, dfw, einfo(802));
		graph.createEdge(dfw, jfk, einfo(1391));
		
		graph.createEdge(bwi, mia, einfo(946));
		graph.createEdge(bwi, jfk, einfo(184));
		graph.createEdge(mia, jfk, einfo(1090));
		graph.createEdge(mia, bos, einfo(1258));
		
		graph.createEdge(bwi, ord, einfo(621));
		graph.createEdge(bwi, jfk, einfo(184));
		
		graph.createEdge(ord, bos, einfo(867));
		graph.createEdge(ord, pvd, einfo(849));
		graph.createEdge(ord, jfk, einfo(740));
		
		graph.createEdge(jfk, pvd, einfo(144));
		graph.createEdge(jfk, bos, einfo(187));
		
		Jail.install();
		IPrintable print = (IPrintable) graph.adapt(IPrintable.class);
		Jail.setProperty("DirectedGraphPrintable.dot.edge.label.uinfo","distance");
		print.print(System.out);
	}

	/** Asserts that some distance is the following. */
	private void assertDistance(String label, Integer dist, IDSPOutput<Integer> output) {
		Object vertex = index.getVertex(label);
		assertEquals(dist,output.getDistance(vertex));
	}
	
	/** Tests DijsktraShortestPath with default input and output. */
	@SuppressWarnings("unchecked")
	public void testDefaultClasses() {
		IDSPInput<Integer> input = new DefaultDSPInput(graph,index.getVertex("BWI"),"distance");
		IDSPOutput<Integer> output = new InGraphDSPOutput<Integer>(graph,"distance","inedge");
		new DijkstraShortestPath<Integer>().execute(input,output);
		
		assertDistance("ORD",621,output);
		assertDistance("BWI",0,output);
		assertDistance("JFK",184,output);
		assertDistance("PVD",328,output);
		assertDistance("BOS",371,output);
		assertDistance("SFO",2467,output);
		assertDistance("LAX",2658,output);
		assertDistance("DFW",1423,output);
		assertDistance("MIA",946,output);
	}
	
}
