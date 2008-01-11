package be.uclouvain.jail.algo.graph.walk;

import java.util.Random;

import be.uclouvain.jail.common.IPredicate;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.IDirectedGraphPath;

/**
 * Provides required input information of {@link RandomWalkAlgo}.
 * 
 * @author blambeau
 */
public interface IRandomWalkInput {

	/** Returns graph to walk through. */
	public IDirectedGraph getInputGraph();
	
	/** Choose a root vertex from the graph. */
	public Object chooseRootVertex(IDirectedGraph g, Random r);
	
	/** Returns the stop walk predicate. */
	public IPredicate<IRandomWalkResult> getWalkStopPredicate(Random r);
	
	/** Returns the stop walk predicate. */
	public IPredicate<IDirectedGraphPath> getPathStopPredicate(Random r);
	
}
