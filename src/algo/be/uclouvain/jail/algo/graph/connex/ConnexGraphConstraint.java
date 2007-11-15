package be.uclouvain.jail.algo.graph.connex;

import be.uclouvain.jail.algo.graph.utils.IGraphPartition;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.deco.DirectedGraph;
import be.uclouvain.jail.graph.deco.GraphConstraintViolationException;
import be.uclouvain.jail.graph.deco.IGraphConstraint;

/**
 * Force a graph to be connex.
 * 
 * @author blambeau
 */
public class ConnexGraphConstraint implements IGraphConstraint {

	/** Installs the constraint on a graph. */
	public <T extends IGraphConstraint> T installOn(DirectedGraph graph)
			throws GraphConstraintViolationException {
		throw new UnsupportedOperationException();
	}

	/** Uninstalls this constraint from the graph. */
	public void uninstall() {
		throw new UnsupportedOperationException();
	}

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
