package be.uclouvain.jail.graph.constraints;

import java.util.HashMap;
import java.util.Map;

import be.uclouvain.jail.graph.GraphConstraintViolationException;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.IGraphConstraint;
import be.uclouvain.jail.graph.deco.DirectedGraph;
import be.uclouvain.jail.graph.deco.GraphChangeEvent;
import be.uclouvain.jail.graph.deco.IGraphDelta;
import be.uclouvain.jail.graph.deco.IGraphDeltaVisitor;
import be.uclouvain.jail.graph.deco.IGraphListener;

/**
 * Allows uniqueness of attributes on vertices and edges.
 * 
 * @author blambeau
 */
public class GraphUniqueIndex extends AbstractGraphConstraint {

	/** Graph on which the constraint is installed. */
	private DirectedGraph graph;
	
	/** Listener. */
	private IndexListener listener;
	
	/** Unique index. */
	private Map<Object,Object> index;

	/** User info attribute which must be unique. */
	private String attr;
	
	/** Attribute is mandatory? */
	private boolean mandatory;
	
	/** Mask. */
	private int mask;
	
	/** Listener used to check constraint. */
	class IndexListener implements IGraphListener, IGraphDeltaVisitor {
		
		/** Creates a listener instance. */
		public IndexListener() {
		}

		/** Fired when the graph changed. */
		public void graphChanged(DirectedGraph graph, IGraphDelta delta) {
			int what = (mask == AbstractGraphConstraint.EDGE) ?
			           (GraphChangeEvent.EDGE_CREATED | GraphChangeEvent.EDGE_REMOVED 
			        		   | GraphChangeEvent.EDGE_CHANGED) :
			           (GraphChangeEvent.VERTEX_CREATED | GraphChangeEvent.VERTEX_REMOVED 
	                           | GraphChangeEvent.VERTEX_CHANGED);
			delta.accept(this, what);
		}

		/** Check constraint. */
		public void visit(GraphChangeEvent event) {
			DirectedGraph graph = event.getDirectedGraph();
			int type = event.type();
			
			// component is edge or vertex
			Object component = event.component();
			
			// value is attribute value that must be unique
			Object value = extractValue(graph,component);

			switch (type) {
				/* ON COMPONENT CHANGE */
				case GraphChangeEvent.VERTEX_CHANGED:
				case GraphChangeEvent.EDGE_CHANGED:
					// remove old mapping
					for (Object key: index.keySet()) {
						Object old = index.get(key);
						if (component.equals(old)) {
							index.remove(key);
							break;
						}
					}
					
				/* ON COMPONENT CREATION */
				case GraphChangeEvent.VERTEX_CREATED:
				case GraphChangeEvent.EDGE_CREATED:
					if (value == null && mandatory) {
						// 1) value is not present but mandatory 
						throw new GraphConstraintViolationException(GraphUniqueIndex.this,getMessage(value));
					} else if (value != null) {
						// 2) value is present
						if (index.containsKey(value)) {
							// already exists in index
							throw new GraphConstraintViolationException(GraphUniqueIndex.this,getMessage(value));
						} else {
							// put in index
							index.put(value, component);
						}
					}
					break;

				/* ON COMPONENT REMOVAL */
				case GraphChangeEvent.VERTEX_REMOVED:
				case GraphChangeEvent.EDGE_REMOVED:
					if (value == null) { return; }
					index.remove(value);
					break;
			}
		}

	}
	
	/** Creates a uniqueness constraint. */
	public GraphUniqueIndex(int mask, String attr, boolean mandatory) {
		if (mask != AbstractGraphConstraint.EDGE && mask != AbstractGraphConstraint.VERTEX) {
			throw new IllegalArgumentException("Incorrect index type.");
		}
		this.mask = mask;
		this.attr = attr;
		this.mandatory = mandatory;
	}

	/** Extracts a component attribute value. */
	private Object extractValue(IDirectedGraph graph, Object comp) {
		return graph.getUserInfoOf(comp).getAttribute(attr);
	}
	
	/** Install the constraint on a graph. */
	@SuppressWarnings("unchecked")
	public <T extends IGraphConstraint> T installOn(DirectedGraph graph) throws GraphConstraintViolationException {
		this.graph = graph;
		this.listener = new IndexListener();
		this.index = new HashMap<Object,Object>();

		// fill index initially
		Iterable<Object> comps = (mask==AbstractGraphConstraint.EDGE) ? graph.getEdges() : graph.getVertices();
		for (Object comp: comps) {
			Object key = extractValue(graph,comp);
			if (index.containsKey(key)) {
				throw new GraphConstraintViolationException(this,getMessage(key));
			} else {
				index.put(key, comp);
			}
		}
		
		// add listener to the graph
		graph.addGraphListener(listener);
		return (T) this;
	}

	/** Uninstalls on a graph. */
	public void uninstall() {
		if (graph == null) {
			throw new IllegalStateException("Constraint has not been installed.");
		}
		graph.removeGraphListener(listener);
	}

	/** Checks this constraint on a graph. */
	public boolean isRespectedBy(IDirectedGraph graph) {
		Map<Object,Object> index = new HashMap<Object,Object>();
		Iterable<Object> comps = (mask==AbstractGraphConstraint.EDGE) ? graph.getEdges() : graph.getVertices();
		for (Object comp: comps) {
			Object key = extractValue(graph,comp);
			if (index.containsKey(key)) {
				return false;
			} else {
				index.put(key, comp);
			}
		}
		return true;
	}
	
	/** Returns the vertex (or edge) mapped to a given value. */
	public Object getVertex(Object value) {
		return index.get(value);
	}

	/** Returns the edge (or vertex) mapped to a given value. */
	public Object getEdge(Object value) {
		return index.get(value);
	}

	/** Get a user-friendly message for constraint violation. */ 
	private String getMessage(Object key) {
		if (key == null) {
			return ((mask == AbstractGraphConstraint.VERTEX) ? "Vertex " : "Edge ") +
			       "attribute [" + attr + "] is mandatory.";
		}
		return "Duplicate " + 
		       ((mask == AbstractGraphConstraint.VERTEX) ? "vertex " : "edge ") +
		       "attribute [" + attr + "] value : " + key.toString();
	}
	
}
