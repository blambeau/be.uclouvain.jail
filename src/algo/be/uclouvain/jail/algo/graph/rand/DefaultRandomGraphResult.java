package be.uclouvain.jail.algo.graph.rand;

import java.util.Random;

import net.chefbe.javautils.adapt.AdaptUtils;
import be.uclouvain.jail.algo.graph.connex.GraphConXDetector;
import be.uclouvain.jail.algo.graph.utils.IGraphMemberGroup;
import be.uclouvain.jail.algo.graph.utils.IGraphPartition;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.adjacency.AdjacencyDirectedGraph;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.UserInfoRandomizer;

/**
 * Provides a base implementation of IRandomGraphOutput.
 * 
 * @author blambeau
 */
public class DefaultRandomGraphResult implements IRandomGraphOutput {

	/** Resulting graph. */
	private IDirectedGraph g;
	
	/** Ensure connex graph? */
	private boolean connex = false;
	
	/** Vertex creator to use. */
	private UserInfoRandomizer vertexRandomizer;
	
	/** Edge creator to use. */
	private UserInfoRandomizer edgeRandomizer;
	
	/** Creates a result instance. */
	public DefaultRandomGraphResult() {
		vertexRandomizer = new UserInfoRandomizer();
		edgeRandomizer = new UserInfoRandomizer();
	}
	
	/** Returns the vertex random creator. */
	public UserInfoRandomizer getVertexRandomizer() {
		return vertexRandomizer;
	}
	
	/** Returns the edge random creator. */
	public UserInfoRandomizer getEdgeRandomizer() {
		return edgeRandomizer;
	}
	
	/** Force one connex composant in resulting graph? */
	public void setConnex(Boolean connex) {
		this.connex = connex;
	}
	
	/** Factors a graph instance. */
	public IDirectedGraph factorGraph() {
		return new AdjacencyDirectedGraph();
	}

	/** Creates a random vertex info. */
	public IUserInfo createVertexInfo(Random r) {
		return vertexRandomizer.create(r);
	}
	
	/** Creates a random edge info. */
	public IUserInfo createEdgeInfo(Object source, Object target, Random r) {
		return edgeRandomizer.create(r);
	}

	/** Throws an UnsupportedOperationException. */
	public void failed() {
		throw new UnsupportedOperationException("Failed to generate random graph");
	}

	/** Marks the output graph as g. */ 
	public void success(IDirectedGraph g) {
		this.g = g;
		
		if (connex) {
			// compute connex composants
			IGraphPartition p = new GraphConXDetector(g).getGraphPartition();
			
			// find max group
			int max = -1;
			IGraphMemberGroup group = null;
			for (IGraphMemberGroup grp : p) {
				if (grp.size()>max) {
					max = grp.size();
					group = grp;
				}
			}
			assert (group != null) : "At least one connex composant.";
			
			Object[] vertices = g.getVerticesTotalOrder().getTotalOrder();
			for (Object vertex: vertices) {
				if (!group.contains(vertex)) {
					g.removeVertex(vertex);
				}
			}
		}
	}

	/** Adapts to a type. */
	public <T> Object adapt(Class<T> c) {
		if (c.isAssignableFrom(getClass())) {
			return this;
		}
		
		// natural adaptation to a DFA
		if (IDirectedGraph.class.equals(c)) {
			return g;
		}
		
		return AdaptUtils.externalAdapt(this,c);
	}

}
