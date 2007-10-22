package be.uclouvain.jail.graph.deco;

import java.util.HashMap;
import java.util.Map;

/**
 * Allows uniqueness of attributes on vertices and edges.
 * 
 * @author blambeau
 */
public class GraphUniqueIndex implements IGraphConstraint {

	/** Index on edge. */
	public static final int EDGE = GraphChangeEvent.EDGE_CREATED | GraphChangeEvent.EDGE_REMOVED 
	                             | GraphChangeEvent.EDGE_CHANGED;
	
	/** Index on vertex. */
	public static final int VERTEX = GraphChangeEvent.VERTEX_CREATED | GraphChangeEvent.VERTEX_REMOVED 
                                   | GraphChangeEvent.VERTEX_CHANGED;
	
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
	
	/** Listener used to check constraint. */
	class IndexListener implements IGraphListener, IGraphDeltaVisitor {
		
		/** Mask. */
		private int mask;
		
		/** Creates a listener instance. */
		public IndexListener(int mask) {
			if (mask != EDGE && mask != VERTEX) {
				throw new IllegalArgumentException("Incorrect index type.");
			}
			this.mask = mask;
		}

		/** Returns the mask. */
		public int getMask() {
			return mask;
		}
		
		/** Fired when the graph changed. */
		public void graphChanged(DirectedGraph graph, IGraphDelta delta) {
			delta.accept(this, mask);
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
	public GraphUniqueIndex(int type, String attr, boolean mandatory) {
		this.listener = new IndexListener(type);
		this.index = new HashMap<Object,Object>();
		this.attr = attr;
		this.mandatory = mandatory;
	}

	/** Extracts a vertex attribute value. */
	private Object extractVertexValue(DirectedGraph graph, Object vertex) {
		return graph.getVertexInfo(vertex).getAttribute(attr);
	}

	/** Extracts an edge attribute value. */
	private Object extractEdgeValue(DirectedGraph graph, Object edge) {
		return graph.getEdgeInfo(edge).getAttribute(attr);
	}

	/** Extracts a component attribute value. */
	private Object extractValue(DirectedGraph graph, Object comp) {
		int mask = listener.getMask();
		return (mask==EDGE) ? extractEdgeValue(graph,comp) : extractVertexValue(graph,comp);
	}
	
	/** Install the constraint on a graph. */
	@SuppressWarnings("unchecked")
	public <T extends IGraphConstraint> T installOn(DirectedGraph graph) throws GraphConstraintViolationException {
		this.graph = graph;
		int mask = listener.getMask();
		Iterable<Object> comps = (mask==EDGE) ? graph.getEdges() : graph.getVertices();
		for (Object comp: comps) {
			Object key = extractValue(graph,comp);
			if (index.containsKey(key)) {
				throw new GraphConstraintViolationException(this,getMessage(key));
			} else {
				index.put(key, comp);
			}
		}
		graph.addGraphListener(listener);
		return (T) GraphUniqueIndex.this;
	}

	/** Uninstalls on a graph. */
	public void uninstall() {
		if (graph == null) {
			throw new IllegalStateException("Constraint has not been installed.");
		}
		graph.removeGraphListener(listener);
	}

	/** Checks this constraint on a graph. */
	public boolean isRespectedBy(DirectedGraph graph) {
		Map<Object,Object> index = new HashMap<Object,Object>();
		int mask = listener.getMask();
		Iterable<Object> comps = (mask==EDGE) ? graph.getEdges() : graph.getVertices();
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
			return ((listener.getMask() == VERTEX) ? "Vertex " : "Edge ") +
			       "attribute [" + attr + "] is mandatory.";
		}
		return "Duplicate " + 
		       ((listener.getMask() == VERTEX) ? "vertex " : "edge ") +
		       "attribute [" + attr + "] value : " + key.toString();
	}
	
}
