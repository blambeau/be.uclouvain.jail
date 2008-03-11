package be.uclouvain.jail.fa.utils;

import be.uclouvain.jail.algo.graph.shortest.dsp.DSPAlgo;
import be.uclouvain.jail.algo.graph.shortest.dsp.DefaultDSPInput;
import be.uclouvain.jail.algo.graph.shortest.dsp.DefaultDSPOutput;
import be.uclouvain.jail.common.base.AbstractQueryable;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.graph.IDirectedGraph;

/**
 * Provides information about a graph.
 * 
 * @author blambeau
 */
public class DFAQueryable extends AbstractQueryable<IDFA> {

	/** Creates a queryable for a graph. */
	public DFAQueryable(IDFA dfa) {
		super(dfa);
	}

	/** Computes graph depth. */
	public int getDepth() {
		int depth = -1;
		IDirectedGraph g = queried.getGraph();

		DefaultDSPInput input = new DefaultDSPInput(g,queried.getInitialState());
		DefaultDSPOutput<Integer> output = new DefaultDSPOutput<Integer>();
		new DSPAlgo<Integer>().execute(input, output);
		for (Object state: g.getVertices()) {
			int dist = output.getDistance(state);
			if (dist > depth) {
				depth = dist;
			}
		}
		return depth;
	}
		
}
