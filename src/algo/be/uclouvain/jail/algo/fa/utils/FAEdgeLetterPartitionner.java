package be.uclouvain.jail.algo.fa.utils;

import be.uclouvain.jail.algo.graph.utils.IGraphPartitionner;
import be.uclouvain.jail.fa.IFA;

/**
 * Partitions edges according to the letter.
 * 
 * @author blambeau
 */
public class FAEdgeLetterPartitionner implements IGraphPartitionner<Object> {

	/** Automaton to use. */
	private IFA fa;
	
	/** Creates a partitionner instance. */
	public FAEdgeLetterPartitionner(IFA fa) {
		this.fa = fa;
	}

	/** Returns the edge letter as partition identifier. */
	public Object getPartitionOf(Object state) {
		return fa.getEdgeLetter(state);
	}

}
