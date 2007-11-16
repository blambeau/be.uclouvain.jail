package be.uclouvain.jail.graph.constraints;

import be.uclouvain.jail.graph.GraphConstraintViolationException;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.IGraphConstraint;
import be.uclouvain.jail.graph.deco.DirectedGraph;
import be.uclouvain.jail.graph.deco.GraphChangeEvent;

/**
 * This class acts as an adapter for graph constraints which are not (or
 * cannot) be active.
 * 
 * <p>This class is introduced for two reasons: 1) many graph constraints
 * are not active and cannot implement install() and uninstall() methods ;
 * 2) IGraphPredicate implementation is provided and delegates the job to
 * {@link #isRespectedBy(IDirectedGraph)}.</p>
 * 
 * <p>Please note that this class throws UnsupportedOperationException
 * for all active constraint methods.</p>
 * 
 * @author blambeau
 */
public abstract class AbstractGraphConstraint extends AbstractGraphPredicate 
	implements IGraphConstraint {

	/** Index on edge. */
	public static final int EDGE = GraphChangeEvent.EDGE_CREATED | GraphChangeEvent.EDGE_REMOVED 
	                             | GraphChangeEvent.EDGE_CHANGED;
	/** Index on vertex. */
	public static final int VERTEX = GraphChangeEvent.VERTEX_CREATED | GraphChangeEvent.VERTEX_REMOVED 
	                               | GraphChangeEvent.VERTEX_CHANGED;

	/** Throws an UnsupportedOperationException. */
	public <T extends IGraphConstraint> T installOn(DirectedGraph graph)
			throws GraphConstraintViolationException {
		throw new UnsupportedOperationException();
	}

	/** Throws an UnsupportedOperationException. */
	public void uninstall() {
		throw new UnsupportedOperationException();
	}

	/** Directly delegated to {@link #isRespectedBy(IDirectedGraph)}. */
	@Override
	public final boolean evaluate(IDirectedGraph graph) {
		return isRespectedBy(graph);
	}

	/** To be implemented. */
	public abstract boolean isRespectedBy(IDirectedGraph graph);

}
