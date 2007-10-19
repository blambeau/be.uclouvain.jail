package be.uclouvain.jail.graph.deco;

/**
 * Provides an empty implementation of {@link IGraphDeltaVisitor}, allowing
 * us to complete the IVisitor interface without hurting your code.
 */
public class GraphListenerAdapter implements IGraphListener, IGraphDeltaVisitor {
	
	/** Mask of interesting events. */
	private int mask;

	/** Creates a listener on all events. */
	public GraphListenerAdapter() {
		this(GraphChangeEvent.ALL_EVENTS);
	}
	
	/** Creates a listener for some events of interest. */
	public GraphListenerAdapter(int mask) {
		this.mask = mask;
	}

	/** Returns mask for events of interest. */
	public int getMask() {
		return mask;
	}
	
	/** Fired when a graph changes. */
	public void graphChanged(DirectedGraph graph, IGraphDelta delta) {
		delta.accept(this, getMask());
	}

	/** Visites a graph event. */
	public void visit(GraphChangeEvent event) {
		DirectedGraph graph = event.getDirectedGraph();
		Object component = event.component();
		switch (event.type()) {
			case GraphChangeEvent.VERTEX_CREATED:
				vertexCreated(graph,component);
				break;
			case GraphChangeEvent.VERTEX_REMOVED:
				vertexRemoved(graph,component);
				break;
			case GraphChangeEvent.VERTEX_CHANGED:
				vertexChanged(graph,component);
				break;
			case GraphChangeEvent.EDGE_CREATED:
				edgeCreated(graph,component);
				break;
			case GraphChangeEvent.EDGE_REMOVED:
				edgeRemoved(graph,component);
				break;
			case GraphChangeEvent.EDGE_CHANGED:
				edgeChanged(graph,component);
				break;
		}
		throw new AssertionError("Known graph change event type: " + event.type());
	}

	/** Fired when a VERTEX_CREATED event is visited. */
	public void vertexCreated(DirectedGraph graph, Object vertex) {
	}
	
	/** Fired when a VERTEX_REMOVED event is visited. */
	public void vertexRemoved(DirectedGraph graph, Object vertex) {
	}
	
	/** Fired when a VERTEX_CHANGED event is visited. */
	public void vertexChanged(DirectedGraph graph, Object vertex) {
	}
	
	/** Fired when a EDGE_CREATED event is visited. */
	public void edgeCreated(DirectedGraph graph, Object edge) {
	}
	
	/** Fired when a EDGE_REMOVED event is visited. */
	public void edgeRemoved(DirectedGraph graph, Object edge) {
	}
	
	/** Fired when a EDGE_CHANGED event is visited. */
	public void edgeChanged(DirectedGraph graph, Object edge) {
	}
	
}