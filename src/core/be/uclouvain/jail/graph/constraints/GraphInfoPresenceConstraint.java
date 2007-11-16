package be.uclouvain.jail.graph.constraints;

import be.uclouvain.jail.graph.GraphConstraintViolationException;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.IGraphConstraint;
import be.uclouvain.jail.graph.deco.DirectedGraph;
import be.uclouvain.jail.graph.deco.GraphChangeEvent;
import be.uclouvain.jail.graph.deco.IGraphDelta;
import be.uclouvain.jail.graph.deco.IGraphDeltaVisitor;
import be.uclouvain.jail.graph.deco.IGraphListener;

/**
 * Ensures that a user info is present on edges or vertices.
 * 
 * <p>This class implements the whole constraint interface (is active).</p>
 * 
 * @author blambeau
 */
public class GraphInfoPresenceConstraint extends AbstractGraphConstraint {

	/** Graph on which the constraint is installed. */
	private DirectedGraph graph;
	
	/** Listener. */
	private PresenceListener listener;
	
	/** Attribute to check. */
	private String attr;
	
	/** Mask. */
	private int mask;
	
	/** Listener used to check constraint. */
	class PresenceListener implements IGraphListener, IGraphDeltaVisitor {

		/** Creates a listener instance. */
		public PresenceListener() {
			if (mask != AbstractGraphConstraint.EDGE && mask != AbstractGraphConstraint.VERTEX) {
				throw new IllegalArgumentException("Incorrect index type.");
			}
		}

		/** Fired when the graph changed. */
		public void graphChanged(DirectedGraph graph, IGraphDelta delta) {
			int what = (mask == AbstractGraphConstraint.EDGE) ?
					   (GraphChangeEvent.VERTEX_CREATED | GraphChangeEvent.VERTEX_CHANGED) :
					   (GraphChangeEvent.EDGE_CHANGED | GraphChangeEvent.EDGE_CREATED);
			delta.accept(this, what);
		}

		/** Check constraint. */
		public void visit(GraphChangeEvent event) {
			DirectedGraph graph = event.getDirectedGraph();
			
			// component is edge or vertex
			Object component = event.component();
			
			// value is attribute value that must be unique
			Object value = graph.getUserInfoOf(component).getAttribute(attr);
			if (value == null) {
				throw new GraphConstraintViolationException(GraphInfoPresenceConstraint.this,
						"Duplicate " + attr + " on graph.");
			}
		}

	}
	
	/** Creates a constraint instance. */
	public GraphInfoPresenceConstraint(int mask, String attr) {
		this.attr = attr;
		this.mask = mask;
	}

	/** Install the constraint on a graph. */
	@SuppressWarnings("unchecked")
	public <T extends IGraphConstraint> T installOn(DirectedGraph graph) throws GraphConstraintViolationException {
		if (!isRespectedBy(graph)) {
			throw new GraphConstraintViolationException(this,"Duplicate " + attr + " on graph.");
		}
		this.graph = graph;
		listener = new PresenceListener();
		graph.addGraphListener(listener);
		return (T) this;
	}

	/** Uninstalls on a graph. */
	@Override
	public void uninstall() {
		if (graph == null) {
			throw new IllegalStateException("Constraint has not been installed.");
		}
		graph.removeGraphListener(listener);
	}


	/** Checks if the constraint is respected by the graph. */
	@Override
	public boolean isRespectedBy(IDirectedGraph graph) {
		Iterable<Object> comps = (mask==AbstractGraphConstraint.EDGE) ? graph.getEdges() : graph.getVertices();
		for (Object comp: comps) {
			Object value = graph.getUserInfoOf(comp).getAttribute(attr);
			if (value == null) {
				return false;
			}
		}
		return true;
	}

}
