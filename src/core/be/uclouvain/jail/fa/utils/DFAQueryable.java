package be.uclouvain.jail.fa.utils;

import java.util.HashSet;
import java.util.Set;

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
	
	/** Returns number of states. */
	public int getStateCount() {
		return queried.getGraph().getVerticesTotalOrder().size();
	}

	/** Returns number of states. */
	public int getEdgeCount() {
		return queried.getGraph().getEdgesTotalOrder().size();
	}

	/** Returns alphabet size. */
	public int getAlphabetSize() {
		return queried.getAlphabet().getLetters().size();
	}

	/** Returns alphabet size. */
	public int getActualAlphabetSize() {
		Set<Object> letters = new HashSet<Object>();
		for (Object edge: queried.getEdges()) {
			letters.add(queried.getEdgeLetter(edge));
		}
		return letters.size();
	}

	/** Returns average out degree of states. */
	public double getAverageOutDegree() {
		double edges = getEdgeCount();
		double states = getStateCount();
		return (edges/states)/2;
	}
		
}
