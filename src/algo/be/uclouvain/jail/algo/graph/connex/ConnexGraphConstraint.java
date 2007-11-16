package be.uclouvain.jail.algo.graph.connex;

import be.uclouvain.jail.algo.graph.utils.IGraphPartition;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.constraints.AbstractGraphConstraint;

/**
 * Force a graph to be connex.
 * 
 * <p>This constraint checks that the directed graph contains only
 * one connex component.</p>
 * 
 * @author blambeau
 */
public class ConnexGraphConstraint extends AbstractGraphConstraint {

	/** Checks if a graph is connex. */
	public boolean isRespectedBy(IDirectedGraph graph) {
		final boolean[] connex = new boolean[1];
		DefaultGraphConXDetectorInput input = new DefaultGraphConXDetectorInput(graph);
		new GraphConXDetectorAlgo().execute(input, new IGraphConXDetectorResult() {

			/** Checks that only one composant exists. */
			public void ended(IGraphPartition partition) {
				connex[0] = (partition.size()==1);
			}

			/** Does nothing. */
			public void started(IGraphConXDetectorInput input) {
			}

			/** Does nothing. */
			public <T> Object adapt(Class<T> c) {
				return null;
			}
			
		});
		return connex[0];
	}

}
