package be.uclouvain.jail.graph.deco;

import junit.framework.TestCase;
import be.uclouvain.jail.graph.GraphConstraintViolationException;
import be.uclouvain.jail.graph.adjacency.AdjacencyDirectedGraph;
import be.uclouvain.jail.graph.constraints.AbstractGraphConstraint;
import be.uclouvain.jail.graph.constraints.GraphUniqueIndex;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.IUserInfoHelper;
import be.uclouvain.jail.uinfo.UserInfoHelper;

/**
 * Tests GraphUniqueIndex class. 
 * 
 * @author blambeau
 */
public class GraphUniqueIndexTest extends TestCase {

	/** Graph to use. */
	private DirectedGraph graph;
	
	/** Default helper instance. */
	private IUserInfoHelper helper = UserInfoHelper.instance();
	
	/** Sets the test up. */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		graph = (DirectedGraph) new AdjacencyDirectedGraph().adapt(DirectedGraph.class);
		assertNotNull("Adaptation worked.",graph);
	}

	/** Creates a user info. */
	private IUserInfo info(Integer id) {
		helper.addKeyValue("id",id);
		return helper.install();
	}
	
	/** Tests that the graph constraint works. */
	public void testGraphMandatoryUniqueConstraintOK() {
		new GraphUniqueIndex(AbstractGraphConstraint.VERTEX,"id",true).installOn(graph);
		graph.createVertex(info(1));
		graph.createVertex(info(2));
		graph.createVertex(info(3));
		graph.createVertex(info(4));
		graph.createVertex(info(5));
		graph.createVertex(info(6));
	}
	
	/** Tests that the graph constraint works. */
	public void testGraphOptionalUniqueConstraintOK() {
		new GraphUniqueIndex(AbstractGraphConstraint.VERTEX,"id",false).installOn(graph);
		graph.createVertex(info(1));
		graph.createVertex(info(null));
		graph.createVertex(info(3));
		graph.createVertex(info(null));
		graph.createVertex(info(5));
		graph.createVertex(info(6));
	}
	
	/** Tests that the graph constraint works. */
	public void testGraphMandatoryUniqueConstraintKO() throws Exception {
		// Create unique/mandatory constraint on id
		setUp();
		new GraphUniqueIndex(AbstractGraphConstraint.VERTEX,"id",true).installOn(graph);
		
		// Check mandatory catched
		try {
			graph.createVertex(info(null));
			assertFalse("Mandatory failure catched.",true);
		} catch (GraphConstraintViolationException ex) {
		}
		
		// Create unique/mandatory constraint on id
		setUp();
		new GraphUniqueIndex(AbstractGraphConstraint.VERTEX,"id",true).installOn(graph);
		
		// Check uniqueness catched
		boolean atLeastOne = false;
		try {
			graph.createVertex(info(1));
			atLeastOne = true; 
			graph.createVertex(info(1));
			assertFalse("Uniqueness failure catched.",true);
		} catch (GraphConstraintViolationException ex) {
			assertTrue("First state ok",atLeastOne);
		}
	}
	
	/** Tests that index gives correct results. */
	public void testIndexOK() throws Exception {
		GraphUniqueIndex index = new GraphUniqueIndex(AbstractGraphConstraint.VERTEX,"id",true);
		index.installOn(graph);
		
		Object[] v = new Object[10];
		for (int i=0; i<10; i++) {
			v[i]  = graph.createVertex(info(i));
		}
		
		for (int i=0; i<10; i++) {
			assertEquals("Index is correct",v[i],index.getVertex(i));
		}
	}

}
