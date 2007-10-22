package be.uclouvain.jail.algo.graph.shortest.dsp;

import be.uclouvain.jail.graph.IDirectedGraph;

/** Weight informer with all one weights. */  
public class AllOneWeightInformer implements IWeightInformer<Integer> {

	/** Returns 1. */
	public Integer weight(IDirectedGraph graph, Object edge) {
		return 1;
	}

	/** Returns 0. */
	public Integer getNullDistance() {
		return 0;
	}

	/** Returns null. */
	public Integer getInfinityDistance() {
		return null;
	}

	/** Returns d+e. */
	public Integer sum(Integer d, Integer e) {
		return (d==null||e==null) ? null : d+e;
	}

	/** Compares two integers. */
	public int compare(Integer d, Integer e) {
		if (d==null && e==null) { return 0; }
		else if (d==null) { return 1; }
		else if (e==null) { return -1; }
		return d-e;
	}

}
