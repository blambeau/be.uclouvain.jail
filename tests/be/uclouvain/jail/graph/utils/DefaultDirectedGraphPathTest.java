package be.uclouvain.jail.graph.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.IDirectedGraphPath;
import be.uclouvain.jail.graph.adjacency.AdjacencyDirectedGraph;
import be.uclouvain.jail.graph.deco.DirectedGraph;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.MapUserInfo;

/** Tests the DirectedGraphPath class. */
public class DefaultDirectedGraphPathTest extends TestCase {

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
		return new DefaultDirectedGraphPath(graph,edges);
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
	
	/** Tests edges method. */
	public void testIterable() {
		IDirectedGraphPath path = getPath();
		int i=0;
		ITotalOrder order = graph.getEdgesTotalOrder();
		for (Object edge : path) {
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
	
	/** Tests the flush method. */
	public void testFlush() {
		// flush path inside a graph
		IDirectedGraphPath path = getPath();
		IDirectedGraph flush = new AdjacencyDirectedGraph();
		path.flush(flush);
		
		// get total orders
		ITotalOrder edges = flush.getEdgesTotalOrder();
		ITotalOrder vertices = flush.getVerticesTotalOrder();
		
		// check sizes
		assertEquals(path.size(),edges.size());
		assertEquals(path.size()+1,vertices.size());
		
		// check IUserInfos
		int i=0;
		for (Object edge: path) {
			Object source = graph.getEdgeSource(edge);
			Object target = graph.getEdgeTarget(edge);
			
			assertEquals(graph.getEdgeInfo(edge),flush.getEdgeInfo(edges.getElementAt(i)));
			assertEquals(graph.getVertexInfo(source),flush.getVertexInfo(vertices.getElementAt(i)));
			assertEquals(graph.getVertexInfo(target),flush.getVertexInfo(vertices.getElementAt(i+1)));
			
			i++;
		}
	}
	
	/** Tests an empty path. */
	public void testEmptyPath() {
		IDirectedGraphPath path = new DefaultDirectedGraphPath(graph,Collections.emptyList());
		assertEquals(0,path.size());
		assertFalse(path.edges().iterator().hasNext());
		assertFalse(path.iterator().hasNext());
		assertFalse(path.vertices().iterator().hasNext());
	}

	/** Tests a one size path. */
	public void testOneSizePath() {
		List<Object> edges = new ArrayList<Object>();
		
		// create the path
		Object edge = graph.getEdgesTotalOrder().getElementAt(0);
		edges.add(edge);
		IDirectedGraphPath path = new DefaultDirectedGraphPath(graph,edges);
		
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
