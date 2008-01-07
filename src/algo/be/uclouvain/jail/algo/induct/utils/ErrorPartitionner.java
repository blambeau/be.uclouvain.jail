package be.uclouvain.jail.algo.induct.utils;

import be.uclouvain.jail.algo.induct.open.IPartitionner;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.utils.ITotalOrder;

/** Partitioner for error vs. non error states. */
public class ErrorPartitionner implements IPartitionner {

	/** Creates a partitionner instance. */
	public ErrorPartitionner() {
	}

	/** Computes the partition. */
	public int[] partition(IDFA pta) {
		IDirectedGraph g = pta.getGraph();
		ITotalOrder<Object> states = g.getVerticesTotalOrder();
		int partition[] = new int[states.size()];
		int i = 0;
		for (Object state : g.getVertices()) {
			if (pta.isError(state)) {
				partition[i] = -1;
			} else {
				partition[i] = 0;
			}
			i++;
		}

		return partition;
	}
}
