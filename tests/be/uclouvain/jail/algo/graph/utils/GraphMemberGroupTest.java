package be.uclouvain.jail.algo.graph.utils;

import java.util.List;

import junit.framework.TestCase;
import be.uclouvain.jail.algo.graph.shortest.dsp.CitiesDirectedGraph;
import be.uclouvain.jail.graph.utils.ITotalOrder;
import be.uclouvain.jail.uinfo.IUserInfo;

/**
 * Tests GraphMemberGroup class.
 * 
 * @author blambeau
 */
public class GraphMemberGroupTest extends TestCase {

	/** Cities graph. */
	private CitiesDirectedGraph graph;
	
	/** Empty group. */
	private IGraphMemberGroup empty;
	
	/** One group. */
	private IGraphMemberGroup one;

	/** Many group. */
	private IGraphMemberGroup many;
	
	/** All group. */
	private IGraphMemberGroup all;
	
	/** Installs graph. */
	@Override
	protected void setUp() throws Exception {
		graph = new CitiesDirectedGraph();
		ITotalOrder<Object> order = graph.getVerticesTotalOrder();

		// empty
		empty = new GraphMemberGroup(graph,order);

		// one
		one = new GraphMemberGroup(graph,order);
		one.addMember(order.getElementAt(0));
		
		// many
		many = new GraphMemberGroup(graph,order);
		many.addMember(order.getElementAt(0));
		many.addMember(order.getElementAt(3));
		many.addMember(order.getElementAt(6));

		// all
		all = new GraphMemberGroup(graph,order);
		all.addMembers(order.getTotalOrder());
	}
	
	/** Tests all implementations of IGraphMemberGroup. */
	public void testAll() {
		// basic implem
		tAll();
		
		// vertex group
		empty = new GraphVertexGroup(empty);
		one = new GraphVertexGroup(one);
		many = new GraphVertexGroup(many);
		all = new GraphVertexGroup(all);
		tAll();
	}

	/** Tests all method on a IGraphMemberGroup implem. */
	protected void tAll() {
		tGetGraph();
		tSize();
		tContains();
		tEquals();
		tGetUserInfos();
	}
	
	/** test getGraph(). */
	protected void tGetGraph() {
		assertEquals(graph,empty.getGraph());
		assertEquals(graph,one.getGraph());
		assertEquals(graph,many.getGraph());
		assertEquals(graph,all.getGraph());
	}
	
	/** tests size(). */
	protected void tSize() {
		assertEquals(0,empty.size());
		assertEquals(1,one.size());
		assertEquals(3,many.size());
		assertEquals(graph.getVerticesCount(),all.size());
	}
	
	/** tests contains(). */
	protected void tContains() {
		int i=0;
		for (Object vertex: graph.getVerticesTotalOrder()) {
			assertFalse(empty.contains(vertex));
			assertEquals(i==0,one.contains(vertex));
			assertEquals(i==0||i==3||i==6,many.contains(vertex));
			assertTrue(all.contains(vertex));
			i++;
		}
	}
	
	/** test equals(). */
	protected void tEquals() {
		assertTrue(empty.equals(empty));
		assertTrue(one.equals(one));
		assertTrue(many.equals(many));
		assertTrue(all.equals(all));
		
		assertFalse(empty.equals(one));
		assertFalse(empty.equals(many));
		assertFalse(empty.equals(all));

		assertFalse(one.equals(empty));
		assertFalse(one.equals(many));
		assertFalse(one.equals(all));

		assertFalse(many.equals(empty));
		assertFalse(many.equals(one));
		assertFalse(many.equals(all));

		assertFalse(all.equals(empty));
		assertFalse(all.equals(many));
		assertFalse(all.equals(one));
		
		// empty 2
		ITotalOrder<Object> order = graph.getVerticesTotalOrder();
		GraphMemberGroup empty2 = new GraphMemberGroup(graph,order);

		// one 2
		GraphMemberGroup one2 = new GraphMemberGroup(graph,order);
		one2.addMember(order.getElementAt(0));
		
		// many 2
		GraphMemberGroup many2 = new GraphMemberGroup(graph,order);
		many2.addMember(order.getElementAt(0));
		many2.addMember(order.getElementAt(3));
		many2.addMember(order.getElementAt(6));

		// all 2
		GraphMemberGroup all2 = new GraphMemberGroup(graph,order);
		all2.addMembers(order.getTotalOrder());
		
		assertTrue(empty.equals(empty2) && empty2.equals(empty));
		assertTrue(one.equals(one2) && one2.equals(one));
		assertTrue(many.equals(many2) && many2.equals(many));
		assertTrue(all.equals(all2) && all2.equals(all));
	}
	
	/** tests getUserInfos(). */
	protected void tGetUserInfos() {
		List<IUserInfo> infos = empty.getUserInfos();
		assertEquals(0,infos.size());
		
		infos = one.getUserInfos();
		assertEquals(1,infos.size());
		
		infos = many.getUserInfos();
		assertEquals(3,infos.size());
		
		infos = all.getUserInfos();
		assertEquals(graph.getVerticesCount(),infos.size());
	}
	
	
}
