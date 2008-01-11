package be.uclouvain.jail.algo.graph.rand;

import java.util.Random;

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

	/** Creates a state information. 
	 * @param graph TODO*/
	public IUserInfo createVertexInfo(IDirectedGraph graph, Random r);
	
	/** Creates an edge information. */
	public IUserInfo createEdgeInfo(IDirectedGraph graph, Object source, Object target, Random r);
	
	/** Cleans a directed graph before acceptation. */
	public IDirectedGraph clean(IDirectedGraph g);
	
	/** Let the output know that the algorithm succeed. */
	public void success(IDirectedGraph g);

	/** Let the output know that the algorithm failed. */
	public void failed();
	
}
