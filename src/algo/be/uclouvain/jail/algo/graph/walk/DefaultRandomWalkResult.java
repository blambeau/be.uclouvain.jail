package be.uclouvain.jail.algo.graph.walk;

import java.util.ArrayList;
import java.util.List;

import be.uclouvain.jail.algo.utils.AbstractAlgoResult;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.IDirectedGraphPath;
import be.uclouvain.jail.graph.adjacency.AdjacencyDirectedGraph;

/**
 * Provides a default implementation of {@link IRandomWalkResult}.
 * 
 * @author blambeau
 */
public class DefaultRandomWalkResult extends AbstractAlgoResult implements IRandomWalkResult {

	/** Created paths. */
	private List<IDirectedGraphPath> paths;
	
	/** Fired when algo is started. */ 
	public void started(IRandomWalkInput input) {
		paths = new ArrayList<IDirectedGraphPath>();
	}
	
	/** Fired when algo ends. */
	public void ended(IRandomWalkInput input) {
	}
	
	/** Returns number of generated paths. */
	public int size() {
		return paths.size();
	}

	/** Adds a walk path. */
	public void addWalkPath(IDirectedGraphPath path) {
		paths.add(path);
	}

	/** Provides adaptations. */
	public <T> Object adapt(Class<T> c) {
		if (c.isAssignableFrom(getClass())) {
			return this;
		}
		
		// adaptation to a graph by flush
		if (IDirectedGraph.class.equals(c)) {
			IDirectedGraph g = new AdjacencyDirectedGraph();
			for (IDirectedGraphPath path: paths) {
				path.flush(g);
			}
			return g; 
		}
		
		return super.adapt(c);
	}

}
