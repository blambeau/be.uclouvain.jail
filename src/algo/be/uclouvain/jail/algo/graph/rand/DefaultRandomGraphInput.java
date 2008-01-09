package be.uclouvain.jail.algo.graph.rand;

import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.IGraphPredicate;
import be.uclouvain.jail.graph.constraints.AbstractGraphPredicate;

/**
 * Provides a basic implementation of {@link IRandomGraphInput}.
 * 
 * @author blambeau
 */
public class DefaultRandomGraphInput implements IRandomGraphInput {

	/** Number of states generated/to generate. */
	private int nbStates=0;
	private int stateCount=10;
	
	/** Number of edges generated/to generate. */
	private int nbEdges=0;
	private int edgeCount=25;
	
	/** Number of tries allowed. */
	private int nbTries;
	private int tries=5;
	
	/** Sets state count. */
	public void setStateCount(int stateCount) {
		this.stateCount = stateCount;
	}
	
	/** Sets edge count. */
	public void setEdgeCount(int edgeCount) {
		this.edgeCount = edgeCount;
	}
	
	/** Returns always true. */
	public IGraphPredicate getAcceptPredicate() {
		return new AbstractGraphPredicate() {
			public boolean evaluate(IDirectedGraph graph) {
				return true;
			}
		};
	}

	/** Returns true when created states reach stateCount. */
	public IGraphPredicate getVertexStopPredicate() {
		return new AbstractGraphPredicate() {
			public boolean evaluate(IDirectedGraph graph) {
				return (nbStates++ == stateCount);
			}
		};
	}

	/** Returns true when created edge reach edgesCount. */
	public IGraphPredicate getEdgeStopPredicate() {
		return new AbstractGraphPredicate() {
			public boolean evaluate(IDirectedGraph graph) {
				return (nbEdges++ == edgeCount);
			}
		};
	}

	/** Returns number of tries allowed. */
	public IGraphPredicate getTryStopPredicate() {
		return new AbstractGraphPredicate() {
			public boolean evaluate(IDirectedGraph graph) {
				return (nbTries++ == tries);
			}
		};
	}

}
