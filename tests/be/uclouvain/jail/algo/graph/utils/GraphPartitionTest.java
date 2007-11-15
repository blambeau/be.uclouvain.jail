package be.uclouvain.jail.algo.graph.utils;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;
import be.uclouvain.jail.algo.graph.shortest.dsp.CitiesDirectedGraph;
import be.uclouvain.jail.graph.utils.ITotalOrder;

/**
 * Tests GraphPartition class.
 * 
 * @author blambeau
 */
public class GraphPartitionTest extends TestCase {

	/** Cities graph. */
	private CitiesDirectedGraph graph;
	
	/** Installs graph. */
	@Override
	protected void setUp() throws Exception {
		graph = new CitiesDirectedGraph();
	}
	
	/** Tests some common methods of a partition. */
	protected void testCommonMethods(GraphPartition part) {
		// test getGraph
		assertEquals(graph,part.getGraph());
		
		// tests that groupOf contains the vertex
		for (Object vertex: graph.getVertices()) {
			assertTrue(part.getGroupOf(vertex).contains(vertex));
		}
		
		Set<Object> seen = new HashSet<Object>();
		for (IGraphMemberGroup group: part) {
			for (Object vertex: group) {
				// check groupOf is the current group
				assertEquals(group,part.getGroupOf(vertex));
				assertEquals(group,part.getPartitionOf(vertex));
				
				// seen only once
				assertFalse(seen.contains(vertex));
				seen.add(vertex);
			}
		}
		// all seen
		assertEquals(graph.getVerticesCount(),seen.size());
	}
	
	/** Tests partitionner on label key. */
	public void testPartitionOnLabel() {
		UserInfoAttrPartitionner p = new UserInfoAttrPartitionner(graph,"label");
		GraphPartition part = new GraphPartition(graph,graph.getVertices());
		part.refine(p);

		// partition size is number of vertices
		assertEquals(graph.getVerticesCount(),part.size());
		
		// test common methods
		testCommonMethods(part);
		
		// each group contains only a vertex, no duplicates
		for (IGraphMemberGroup g: part) {
			assertTrue(g.size()==1);
		}
	}
	
	/** Tests partition on pair attribute. */
	public void testPartitionOnPair() {
		UserInfoAttrPartitionner p = new UserInfoAttrPartitionner(graph,"pair");
		GraphPartition part = new GraphPartition(graph,graph.getVertices());
		part.refine(p);

		// 2 groups
		assertEquals(2,part.size());
		
		// test common methods
		testCommonMethods(part);
		
		// each group member is correct
		ITotalOrder<Object> order = graph.getVerticesTotalOrder();
		Boolean expected = null;
		for (IGraphMemberGroup g: part) {
			for (Object vertex: g) {
				int index = order.indexOf(vertex);
				if (expected == null) {
					expected = (index%2)==0;
				} else {
					assertEquals(expected.booleanValue(),((index%2)==0));
				}
			}
			expected = !expected;
		}
	}

	/** Tests multiple partitionning. */
	public void testMultiplePartitionning() {
		final ITotalOrder<Object> order = graph.getVerticesTotalOrder();
		GraphPartition part = new GraphPartition(graph,graph.getVertices());

		// partition on pair
		UserInfoAttrPartitionner p = new UserInfoAttrPartitionner(graph,"pair");
		part.refine(p);
		
		// partition on mult of 3
		part.refine(new IGraphPartitionner<Object>() {
			public Object getPartitionOf(Object value) {
				return (order.indexOf(value) % 3);
			}
		});
		
		// test common methods
		testCommonMethods(part);
		
		// each group member is correct
		Boolean expected = null;
		int mod = -1;
		for (IGraphMemberGroup g: part) {
			for (Object vertex: g) {
				int index = order.indexOf(vertex);
				if (mod == -1) {
					expected = (index%2)==0;
					mod = (index%3);
				} else {
					assertEquals(expected.booleanValue(),((index%2)==0));
					assertEquals(mod,index%3);
				}
			}
			expected = !expected;
			mod = -1;
		}
	}
	
}
