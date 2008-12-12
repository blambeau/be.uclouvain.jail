package be.uclouvain.jail.algo.graph.rand;

import net.chefbe.javautils.adapt.IAdaptable;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.uinfo.IUserInfo;

/**
 * Provides output of the random graph algorithm.
 * 
 * @author blambeau
 */
public interface IRandomGraphResult extends IAdaptable {

	/** Lets the output know that the algo has started. */
	public void started(IRandomGraphInput input);
	
	/** Creates an empty graph result. */
	public IDirectedGraph factorGraph();

	/** Creates a state information. */
	public IUserInfo createVertexInfo(IDirectedGraph graph);
	
	/** Creates an edge information. */
	public IUserInfo createEdgeInfo(IDirectedGraph graph, Object source, Object target);
	
	/** Cleans a directed graph before acceptation. */
	public IDirectedGraph clean(IDirectedGraph g);
	
	/** Let the output know that the algorithm succeed. */
	public void success(IDirectedGraph g);

	/** Let the output know that the algorithm failed. */
	public void failed();

	/** Picks up a source state. */
	public Object pickUpSource(IDirectedGraph graph);

	/** Picks up a target state. */
	public Object pickUpTarget(IDirectedGraph graph, Object source);
	
}
