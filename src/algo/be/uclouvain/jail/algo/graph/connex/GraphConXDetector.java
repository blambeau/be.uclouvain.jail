package be.uclouvain.jail.algo.graph.connex;

import be.uclouvain.jail.algo.graph.utils.IGraphPartition;
import be.uclouvain.jail.graph.IDirectedGraph;

/**
 * Provides a facade on connex detector algo.
 * 
 * @author blambeau
 */
public class GraphConXDetector {

	/** Input of the algorithm. */
	private DefaultGraphConXDetectorInput input;
	
	/** Result of the algorithm. */
	private DefaultGraphConXDetectorResult result;
	
	/** Executed? */
	private boolean executed = false;
	
	/** Creates a facade instance. */
	public GraphConXDetector(IDirectedGraph g) {
		this.input = new DefaultGraphConXDetectorInput(g);
		this.result = new DefaultGraphConXDetectorResult();
	}

	/** Returns computed partition. */
	public IGraphPartition getGraphPartition() {
		if (!executed) { execute(); }
		return (IGraphPartition) result.adapt(IGraphPartition.class);
	}

	/** Executes the algorithm. */
	private void execute() {
		new GraphConXDetectorAlgo().execute(input,result);
		executed = true;
	}
	
}
