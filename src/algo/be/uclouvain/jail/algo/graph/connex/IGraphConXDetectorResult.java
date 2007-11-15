package be.uclouvain.jail.algo.graph.connex;

import be.uclouvain.jail.adapt.IAdaptable;
import be.uclouvain.jail.algo.graph.utils.IGraphPartition;

/**
 * Abstracts result of GraphConXDetector algorithm.
 *  
 * @author blambeau
 */
public interface IGraphConXDetectorResult extends IAdaptable {

	/** Algorithm started event. */
	public void started(IGraphConXDetectorInput input);
	
	/** Algorithm ended event. */
	public void ended(IGraphPartition partition);
	
}
