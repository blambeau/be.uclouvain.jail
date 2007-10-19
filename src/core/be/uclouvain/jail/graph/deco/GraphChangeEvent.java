package be.uclouvain.jail.graph.deco;

/** A graph change event. */
public class GraphChangeEvent {
	
	/** Vertex created. */
	public static final int VERTEX_CREATED = 0x0001;

	/** Vertex removed. */
	public static final int VERTEX_REMOVED = 0x0002; 
	
	/** Vertex informations changed. */
	public static final int VERTEX_CHANGED = 0x0004; 
	
	/** Edge created. */
	public static final int EDGE_CREATED = 0x0008;

	/** Edge removed. */
	public static final int EDGE_REMOVED = 0x0010; 
	
	/** Edge informations changed. */
	public static final int EDGE_CHANGED = 0x0020; 

	/** All events mask. */
	public static final int ALL_EVENTS = VERTEX_CREATED | VERTEX_REMOVED | VERTEX_CHANGED
	                                   | EDGE_CREATED | EDGE_REMOVED | EDGE_CHANGED;
	
	/** Delta parent. */
	private IGraphDelta delta;
	
	/** Type of the event. */
	private int type;
	
	/** Changed component. */
	private Object component;

	/** Creates an event. */
	protected GraphChangeEvent(IGraphDelta delta, int type, Object component) {
		this.delta = delta;
		this.type = type;
		this.component = component;
	}

	/** Returns changed graph. */
	public DirectedGraph getDirectedGraph() {
		return delta.getDirectedGraph();
	}
	
	/** Returns event type. */
	public int type() {
		return type;
	}
	
	/** Returns changed component. */
	public Object component() {
		return component;
	}
	
}