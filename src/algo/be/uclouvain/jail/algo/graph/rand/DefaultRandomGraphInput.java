package be.uclouvain.jail.algo.graph.rand;

import be.uclouvain.jail.algo.utils.AbstractAlgoInput;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.IGraphPredicate;
import be.uclouvain.jail.graph.constraints.AbstractGraphPredicate;

/**
 * Provides a basic implementation of {@link IRandomGraphInput}.
 * 
 * @author blambeau
 */
public class DefaultRandomGraphInput extends AbstractAlgoInput implements IRandomGraphInput {

	/** Number of states generated/to generate. */
	protected int stateCount=10;
	
	/** Number of edges generated/to generate. */
	protected int edgeCount=25;
	
	/** Number of tries allowed before failure. */
	private int maxTry=100;

	/** Number of tries already done. */
	private int nbTries;
	
	/** Install options. */
	@Override
	protected void installOptions() {
		super.installOptions();
		super.addOption("stateCount", false, Integer.class, null);
		super.addOption("edgeCount", false, Integer.class, null);
		super.addOption("maxTry", false, Integer.class, null);
	}

	/** Sets state count. */
	public void setStateCount(int stateCount) {
		this.stateCount = stateCount;
	}
	
	/** Sets edge count. */
	public void setEdgeCount(int edgeCount) {
		this.edgeCount = edgeCount;
	}
	
	/** Sets number of tries before failure. */
	public void setMaxTry(int maxTry) {
		this.maxTry = maxTry;
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
				return graph.getVerticesTotalOrder().size() == stateCount;
			}
		};
	}

	/** Returns true when created edge reach edgesCount. */
	public IGraphPredicate getEdgeStopPredicate() {
		return new AbstractGraphPredicate() {
			public boolean evaluate(IDirectedGraph graph) {
				return graph.getEdgesTotalOrder().size() == edgeCount;
			}
		};
	}

	/** Returns number of tries allowed. */
	public IGraphPredicate getTryStopPredicate() {
		return new AbstractGraphPredicate() {
			public boolean evaluate(IDirectedGraph graph) {
				return (nbTries++ == maxTry);
			}
		};
	}

}
