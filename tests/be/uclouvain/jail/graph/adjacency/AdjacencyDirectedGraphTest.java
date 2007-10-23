package be.uclouvain.jail.graph.adjacency;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.utils.ITotalOrder;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.MapUserInfo;

/**
 * Tests for AdjacencyDirectedGraph class.
 * 
 * @author blambeau
 */
public class AdjacencyDirectedGraphTest extends TestCase {

	/** Size of the created graph. */
	private int size = 10, eSize;
	
	/** Vertices. */
	private Object[] vertices;
	
	/** Edges. */
	private Object[][] outEdges;
	
	/** Graph to test. */
	private IDirectedGraph graph;

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
		
		//DFA dfa = new DefaultDFA();
		//graph = dfa.getGraph();
		graph = new AdjacencyDirectedGraph();
		vertices = new Object[size];
		outEdges = new Object[size][];
		for (int i=0; i<size; i++) {
			vertices[i] = graph.createVertex(info("v" + i));
		}
		
		for (int i=0; i<size; i++) {
			outEdges[i] = new Object[i+1];
			for (int j=0; j<=i; j++) {
				outEdges[i][j] = graph.createEdge(vertices[i], vertices[j], 
						info(label(vertices[i]) + "->" + label(vertices[j])));
				eSize++;
			}
		}
	}
	
	/** Tests getUserInfo method. */
	public void testGetVertexInfo() {
		ITotalOrder i = graph.getVerticesTotalOrder();
		int size = i.size();
		for (int j=0; j<size; j++) {
			Object v = i.getElementAt(j);
			assertEquals("v"+j + "==" + label(v),"v" + j,label(v));
		}
	}
	
	/** Tests getUserInfo method. */
	public void testSetVertexInfo() {
		ITotalOrder i = graph.getVerticesTotalOrder();
		int size = i.size();
		for (int j=0; j<size; j++) {
			Object v = i.getElementAt(j);
			graph.setVertexInfo(v, info("v"+j));
		}
		testGetVertexInfo();
	}
	
	/** Tests getVertices method. */
	public void testGetVerticesTotalOrder() {
		ITotalOrder info = graph.getVerticesTotalOrder();
		super.assertEquals(size,info.size());
	}
	
	/** Returns outgoing edges of a vertex. */
	public void testGetOutgoingEdges() {
		ITotalOrder<Object> i = graph.getVerticesTotalOrder();
		for (Object vertex : graph.getVertices()) {
			List<Object> out = new ArrayList<Object>();
			for (Object edge: graph.getOutgoingEdges(vertex)) {
				out.add(edge);
			}
			int index = i.getElementIndex(vertex);
			for (int j=0; j<outEdges[index].length; j++) {
				assertTrue(out.contains(outEdges[index][j]));
			}
		}
	}
	
	/** Returns incoming edges of a vertex. */
	public void testGetIncomingEdges() {
		
	}
	
	/** Tests getUserInfo method. */
	public void testGetEdgeInfo() {
		ITotalOrder i = graph.getEdgesTotalOrder();
		int size = i.size();
		for (int j=0; j<size; j++) {
			Object edge = i.getElementAt(j);
			Object src = graph.getEdgeSource(edge);
			Object trg = graph.getEdgeTarget(edge);
			assertEquals(label(edge) + "==" + label(src) + "->" + label(trg),label(edge),label(src) + "->" + label(trg));
		}
	}
	
	/** Tests getUserInfo method. */
	public void testSetEdgeInfo() {
		ITotalOrder i = graph.getEdgesTotalOrder();
		int size = i.size();
		for (int j=0; j<size; j++) {
			Object edge = i.getElementAt(j);
			Object src = graph.getEdgeSource(edge);
			Object trg = graph.getEdgeTarget(edge);
			graph.setEdgeInfo(edge, info(label(src) + "->" + label(trg)));
		}
		testGetEdgeInfo();
	}
	
	/** Tests getVertices method. */
	public void testGetEdgesTotalOrder() {
		ITotalOrder info = graph.getEdgesTotalOrder();
		super.assertEquals(eSize,info.size());
	}
	
	/** Tests that removing a vertex works. */
	public void testRemoveVertex() {
		ITotalOrder i = graph.getVerticesTotalOrder();
		graph.removeVertex(i.getElementAt(0));
		vertices[0] = graph.createVertex(info("v0"));

		for (int j=0; j<size; j++) {
			outEdges[j][0] = graph.createEdge(vertices[j], vertices[0], 
					info(label(vertices[j]) + "->" + label(vertices[0])));
		}
		this.testGetVerticesTotalOrder();
		this.testGetEdgesTotalOrder();
	}
	
}
