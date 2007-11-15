package be.uclouvain.jail.algo.fa.utils;

import be.uclouvain.jail.algo.graph.utils.IGraphPartitionner;
import be.uclouvain.jail.fa.IFA;
import be.uclouvain.jail.fa.functions.FAStateKind;

/**
 * Partitions FA states according to accepting vs. non accepting
 * vs. error heuristic.
 */
public class FAStateKindPartitionner implements IGraphPartitionner<Object> {

	/** Automaton to use. */
	private IFA fa;
	
	/** Creates a partitionner instance. */
	public FAStateKindPartitionner(IFA fa) {
		this.fa = fa;
	}

	/** Computes the partition identifier for a state. */
	public Object getPartitionOf(Object value) {
		if (fa.isError(value)) {
			return FAStateKind.ERROR;
		} else if (fa.isAccepting(value)) {
			return FAStateKind.ACCEPTING;
		} else {
			return FAStateKind.PASSAGE;
		}
	}

}
