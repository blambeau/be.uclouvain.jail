package be.uclouvain.jail.algo.fa.decorate;

import be.uclouvain.jail.algo.utils.AbstractAlgoResult;
import be.uclouvain.jail.fa.IFA;
import be.uclouvain.jail.graph.IDirectedGraph;

/**
 * Default implementation of {@link IFADecorationResult}.
 * 
 * @author blambeau
 */
public abstract class AbstractFADecorationResult extends AbstractAlgoResult implements IFADecorationResult {

	/** Source DFA. */
	protected IFA source;
	
	/** Underlying graph. */
	protected IDirectedGraph graph;
	
	/** Fired when the algorithm starts. */
	public void started(IFADecorationInput input) {
		this.source = input.getSource();
		this.graph = source.getGraph();
	}
	
	/** Fired when the algorithm ends. */
	public void ended() {
	}

	/** Extracts a state attribute. */
	public Object getStateAttr(Object state, String attr) {
		return graph.getVertexInfo(state).getAttribute(attr);
	}

	/** Sets a state attribute. */
	public void setStateAttr(Object state, String attr, Object value) {
		graph.getVertexInfo(state).setAttribute(attr, value);
	}

	/** Extracts an edge attribute. */
	public Object getEdgeAttr(Object edge, String attr) {
		return graph.getEdgeInfo(edge).getAttribute(attr);
	}
	
	/** Sets an edge attribute. */
	public void setEdgeAttr(Object edge, String attr, Object value) {
		graph.getEdgeInfo(edge).setAttribute(attr, value);
	}
	
	/** Letter on an edge. */
	public Object getEdgeLetter(Object edge) {
		return source.getEdgeLetter(edge);
	}

	/** Computes initial decoration of a state. */
	public boolean initDeco(Object state, boolean isInitial) {
		return false;
	}

	/** Propagates a decoration. */
	public boolean propagate(Object source, Object edge, Object target) {
		return false;
	}


}
