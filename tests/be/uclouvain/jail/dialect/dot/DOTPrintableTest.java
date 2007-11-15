package be.uclouvain.jail.dialect.dot;

import java.io.IOException;

import junit.framework.TestCase;
import be.uclouvain.jail.adapt.AdaptUtils;
import be.uclouvain.jail.adapt.CreateClassAdapter;
import be.uclouvain.jail.dialect.IPrintable;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.adjacency.AdjacencyDirectedGraph;
import be.uclouvain.jail.graph.adjacency.IEdge;
import be.uclouvain.jail.graph.adjacency.IVertex;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.MapUserInfo;

/** Tests DOTPrintable class. */
public class DOTPrintableTest extends TestCase {

	/** Size of the created graph. */
	private int size = 10;
	
	/** Graph to test. */
	private IDirectedGraph graph;

	/** Vertices. */
	private Object[] vertices;
	
	/** Creates a user info structure. */
	private IUserInfo info(String name) {
		IUserInfo info = new MapUserInfo();
		info.setAttribute("label", name);
		return info;
	}
	
	/** Returns the label of a vertices or an edge. */
	private String label(Object vOrE) {
		if (vOrE instanceof IEdge) {
			return (String) graph.getEdgeInfo(vOrE).getAttribute("label");
		} else if (vOrE instanceof IVertex) {
			return (String) graph.getVertexInfo(vOrE).getAttribute("label");
		} else {
			assertTrue(false);
			return null;
		}
	}
	
	/** Creates the test graph. */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		graph = new AdjacencyDirectedGraph();
		vertices = new Object[size];
		for (int i=0; i<size; i++) {
			vertices[i] = graph.createVertex(info("v" + i));
		}
		
		for (int i=0; i<size; i++) {
			for (int j=0; j<=i; j++) {
				graph.createEdge(vertices[i], vertices[j], 
						info(label(vertices[i]) + "->" + label(vertices[j])));
			}
		}
	}

	/** Test print method. */
	public void testPrint() throws IOException {
		AdaptUtils.register(IDirectedGraph.class,IPrintable.class,new CreateClassAdapter(DOTDirectedGraphPrintable.class));
		IPrintable printable = (IPrintable) graph.adapt(IPrintable.class);
		assertNotNull("Adaptation worked.", printable);
		//printable.print(System.out);
	}
	
}
