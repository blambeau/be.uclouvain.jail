package be.uclouvain.jail.graph.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;
import be.uclouvain.jail.graph.IDirectedGraphPath;
import be.uclouvain.jail.graph.adjacency.AdjacencyDirectedGraph;
import be.uclouvain.jail.graph.deco.DirectedGraph;
import be.uclouvain.jail.orders.ITotalOrder;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.MapUserInfo;

/** Tests the DirectedGraphPath class. */
public class DirectedGraphPathTest extends TestCase {

	/** Graph to use. */
	private DirectedGraph graph;

	/** Creates a IUserInfo instance. */
	private IUserInfo info(Object i) {
		IUserInfo info = new MapUserInfo();
		info.setAttribute("id",i);
		return info;
	}
	
	/** Creates the graph. */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		graph = new DirectedGraph(new AdjacencyDirectedGraph());
		
		for (int i=0; i<10; i++) {
			graph.createVertex(info(i));
		}
		ITotalOrder order = graph.getVerticesTotalOrder();
		for (int i=0; i<9; i++) {
			graph.createEdge(order.getElementAt(i), order.getElementAt(i+1), info(i+"->"+(i+1)));
		}
	}

	/** Factor a path instance. */
	private IDirectedGraphPath getPath() {
		List<Object> edges = new ArrayList<Object>();
		for (Object edge: graph.getEdges()) {
			edges.add(edge);
		}
		return new DirectedGraphPath(graph,edges);
	}
	
	/** Tests size method. */
	public void testSize() {
		IDirectedGraphPath path = getPath();
		assertEquals(9,path.size());
	}
	
	/** Tests edges method. */
	public void testGetEdges() {
		IDirectedGraphPath path = getPath();
		int i=0;
		ITotalOrder order = graph.getEdgesTotalOrder();
		for (Object edge : path.edges()) {
			assertEquals(order.getElementAt(i++),edge);
		}
	}
	
	/** Tests vertices method. */
	public void testGetVertices() {
		IDirectedGraphPath path = getPath();
		int i=0;
		ITotalOrder order = graph.getVerticesTotalOrder();
		for (Object vertex: path.vertices()) {
			assertEquals(order.getElementAt(i++),vertex);
		}
	}
	
	/** Tests an empty path. */
	public void testEmptyPath() {
		IDirectedGraphPath path = new DirectedGraphPath(graph,Collections.emptyList());
		assertEquals(0,path.size());
		assertFalse(path.edges().iterator().hasNext());
		assertFalse(path.vertices().iterator().hasNext());
	}

	/** Tests a one size path. */
	public void testOneSizePath() {
		List<Object> edges = new ArrayList<Object>();
		
		// create the path
		Object edge = graph.getEdgesTotalOrder().getElementAt(0);
		edges.add(edge);
		IDirectedGraphPath path = new DirectedGraphPath(graph,edges);
		
		// size is 1
		assertEquals(1,path.size());
		
		// get iterators edges and vertices
		Iterator iedges = path.edges().iterator();
		Iterator ivertices = path.vertices().iterator();
		
		// both has next
		assertTrue(iedges.hasNext());
		assertTrue(ivertices.hasNext());
		
		assertEquals(edge,iedges.next());
		assertEquals(graph.getEdgeSource(edge),ivertices.next());
		assertEquals(graph.getEdgeTarget(edge),ivertices.next());
		
		assertFalse(iedges.hasNext());
		assertFalse(ivertices.hasNext());
		
		assertEquals(graph.getEdgeSource(edge),GraphQueryUtils.getFirstVertex(path));
		assertEquals(graph.getEdgeTarget(edge),GraphQueryUtils.getLastVertex(path));
	}
	
}
