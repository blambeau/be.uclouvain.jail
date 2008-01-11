package be.uclouvain.jail.algo.graph.walk;

import net.chefbe.javautils.adapt.IAdaptable;
import be.uclouvain.jail.graph.IDirectedGraphPath;

/**
 * Abstracts result of {@link RandomWalkAlgo}.
 * 
 * @author blambeau
 */
public interface IRandomWalkResult extends IAdaptable {

	/** Fired when algo starts. */
	public void started(IRandomWalkInput input);
	
	/** Fired when algo ends. */
	public void ended(IRandomWalkInput input);
	
	/** Returns number of paths. */
	public int size();
	
	/** Adds a walk. */
	public void addWalkPath(IDirectedGraphPath path);
	
	
}
