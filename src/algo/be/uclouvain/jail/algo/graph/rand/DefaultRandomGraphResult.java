package be.uclouvain.jail.algo.graph.rand;

import java.util.Random;

import net.chefbe.javautils.adapt.AdaptUtils;
import be.uclouvain.jail.Jail;
import be.uclouvain.jail.algo.commons.Unable;
import be.uclouvain.jail.algo.graph.connex.GraphConXDetector;
import be.uclouvain.jail.algo.graph.copy.match.GMatchPopulator;
import be.uclouvain.jail.algo.graph.utils.IGraphMemberGroup;
import be.uclouvain.jail.algo.graph.utils.IGraphPartition;
import be.uclouvain.jail.algo.utils.AbstractAlgoResult;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.adjacency.AdjacencyDirectedGraph;
import be.uclouvain.jail.graph.utils.ITotalOrder;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.UserInfoRandomizer;

/**
 * Provides a base implementation of IRandomGraphOutput.
 * 
 * @author blambeau
 */
public class DefaultRandomGraphResult extends AbstractAlgoResult implements IRandomGraphResult {

	/** Random generator. */
	protected Random randomizer = Jail.randomizer();
	
	/** Resulting graph. */
	protected IDirectedGraph g;
	
	/** Ensure connex graph? */
	protected boolean connex = false;
	
	/** Vertex creator to use. */
	protected UserInfoRandomizer vertexRandomizer;
	
	/** Edge creator to use. */
	protected UserInfoRandomizer edgeRandomizer;
	
	/** Creates a result instance. */
	public DefaultRandomGraphResult() {
		vertexRandomizer = new UserInfoRandomizer();
		edgeRandomizer = new UserInfoRandomizer();
	}
	
	/** Install options. */
	@Override
	protected void installOptions() {
		super.installOptions();
		super.addOption("connex", false, Boolean.class, null);
		super.addOption("vertex", "vertexPopulator", false, GMatchPopulator.class, null);
		super.addOption("edge", "edgePopulator", false, GMatchPopulator.class, null);
	}

	/** Returns the vertex random creator. */
	public UserInfoRandomizer getVertexRandomizer() {
		return vertexRandomizer;
	}
	
	/** Adds a gmatch vertex populator. */
	public void setVertexPopulator(GMatchPopulator<Random> populator) {
		vertexRandomizer.addPopulator(populator);
	}
	
	/** Returns the edge random creator. */
	public UserInfoRandomizer getEdgeRandomizer() {
		return edgeRandomizer;
	}
	
	/** Adds a gmatch edge populator. */
	public void setEdgePopulator(GMatchPopulator<Random> populator) {
		edgeRandomizer.addPopulator(populator);
	}
	
	/** Force one connex composant in resulting graph? */
	public void setConnex(Boolean connex) {
		this.connex = connex;
	}
	
	/** Lets the output know that the algo has started. */
	public void started(IRandomGraphInput input) {
	}
	
	/** Factors a graph instance. */
	public IDirectedGraph factorGraph() {
		return new AdjacencyDirectedGraph();
	}

	/** {@inheritDoc} */
	public IUserInfo createVertexInfo(IDirectedGraph graph) {
		return vertexRandomizer.create(randomizer);
	}
	
	/** {@inheritDoc} */
	public IUserInfo createEdgeInfo(IDirectedGraph graph, Object source, Object target) {
		return edgeRandomizer.create(randomizer);
	}

	/** {@inheritDoc} */
	public void failed() {
		throw new Unable("Failed to generate random graph");
	}

	/** {@inheritDoc} */
	public IDirectedGraph clean(IDirectedGraph g) {
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
		
		return g;
	}
	
	/** {@inheritDoc} */
	public void success(IDirectedGraph g) {
		this.g = g;
	}

	/** Picks up a source state. */
	public Object pickUpSource(IDirectedGraph graph) {
		ITotalOrder<Object> vertices = graph.getVerticesTotalOrder();
		return vertices.getElementAt(randomizer.nextInt(vertices.size()));
	}

	/** Picks up a target state. */
	public Object pickUpTarget(IDirectedGraph graph, Object source) {
		ITotalOrder<Object> vertices = graph.getVerticesTotalOrder();
		return vertices.getElementAt(randomizer.nextInt(vertices.size()));
	}
	
	/** Sets the resulting directed graph. */
	protected void setDirectedGraph(IDirectedGraph g) {
		this.g = g;
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
