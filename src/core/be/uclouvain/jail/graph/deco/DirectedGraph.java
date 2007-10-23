package be.uclouvain.jail.graph.deco;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import be.uclouvain.jail.adapt.AdaptUtils;
import be.uclouvain.jail.adapt.IAdapter;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.utils.GraphQueryUtils;
import be.uclouvain.jail.graph.utils.ITotalOrder;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.functions.IAggregateFunction;

/**
 * Provides a high level API on {@link IDirectedGraph}S.
 * 
 * <p>This class acts as a decorator of IDirectedGraph instance in order to
 * provide many useful utilities implemented only once. Utilities are the 
 * following:</p>
 * <ul>
 *     <li>Listeners: graph changes may be listen using {@link IGraphListener}.
 *         Graph changes are encapsulated in a @{link IGraphDelta} instance and
 *         fired transactionnaly or not according to invocations of 
 *         {@link #startChanges()} and {@link #stopChanges()} methods. Please 
 *         note that nested changes are not supported.</li>
 *     <li>Constraints: constraints may be installed on graphs using XXXConstraint
 *         classes.</li>
 * </ul>
 * 
 * @author blambeau
 */
public class DirectedGraph implements IDirectedGraph {

	/** Decorated graph. */
	private IDirectedGraph graph;
	
	/** Listeners on graph. */
	private List<IGraphListener> listeners;
	
	/** Current graph modifications. */
	private GraphDelta delta;
	
	/** Graph changes implementation. */
	class GraphDelta implements IGraphDelta {

		/** Events on graph. */
		private List<GraphChangeEvent> events;

		/** Creates an empty delta. */
		public GraphDelta() {
			events = new ArrayList<GraphChangeEvent>();
		}
		
		/** Returns graph that changed. */
		public DirectedGraph getDirectedGraph() {
			return DirectedGraph.this;
		}

		/** Adds an event. */
		public void addEvent(int type, Object component) {
			events.add(new GraphChangeEvent(this,type,component));
		}
		
		/** Accepts a visitor for some events of interest. */
		public void accept(IGraphDeltaVisitor visitor, int mask) {
			for (GraphChangeEvent event: events) {
				int type = event.type();
				if ((type & mask)==type) {
					visitor.visit(event);
				}
			}
		}
		
	}
	
	/** Decorates a directed graph implementation. */
	public DirectedGraph(IDirectedGraph graph) {
		this.graph = graph;
		this.listeners = new ArrayList<IGraphListener>();
	}

	/** Registers a graph listener. */
	public void addGraphListener(IGraphListener listener) {
		this.listeners.add(listener);
	}
	
	/** Removes a graph listener. */
	public void removeGraphListener(IGraphListener listener) {
		this.listeners.remove(listener);
	}
	
	/** Starts a transaction on the graph. */
	public void startChanges() {
		if (delta != null) {
			throw new IllegalStateException("Nested changes not supported");
		}
		delta = new GraphDelta();
	}

	/** Checks if some changes have been started. */
	public boolean hasChanges() {
		return delta != null;
	}
	
	/** Commits a transaction. */
	public void stopChanges() {
		if (delta == null) {
			throw new IllegalStateException("No startChanges() invocation made before.");
		}
		for (IGraphListener l: listeners) {
			l.graphChanged(this, delta);
		}
		delta = null;
	}
	
	/** 
	 * Ensures that changes have been started. 
	 * 
	 * @return true if the changes are new, false otherwise.
	 */
	private boolean ensureChanges() {
		if (hasChanges()) { return false; }
		else { 
			startChanges();
			return true;
		}
	}
	
	/** Returns user info attached to the graph. */
	public IUserInfo getUserInfo() {
		return graph.getUserInfo();
	}

	/** Attachs some user info to the graph. */
	public void setUserInfo(IUserInfo info) {
		graph.setUserInfo(info);
	}
	
	/** Returns user info attached to a vertex or an edge. */
	public IUserInfo getUserInfoOf(Object vertexOrEdge) {
		return graph.getUserInfoOf(vertexOrEdge);
	}

	/** Returns informations attached to a vertex. */
	public IUserInfo getVertexInfo(Object vertex) {
		return graph.getVertexInfo(vertex);
	}
	
	/** Sets informations attached to a vertex. */
	public void setVertexInfo(Object vertex, IUserInfo info) {
		boolean isme = ensureChanges();
		graph.setVertexInfo(vertex, info);
		delta.addEvent(GraphChangeEvent.VERTEX_CHANGED, vertex);
		if (isme) { stopChanges(); }
	}
	
	/** Returns an iterable on graph vertices. */
	public Iterable<Object> getVertices() {
		return graph.getVertices();
	}

	/** 
	 * Creates a new vertex in the graph with associated infos.
	 * 
	 * @param info user informations to attach to the vertex.
	 * @return created vertex.
	 */
	public Object createVertex(IUserInfo info) {
		boolean isme = ensureChanges();
		Object vertex = graph.createVertex(info);
		delta.addEvent(GraphChangeEvent.VERTEX_CREATED, vertex);
		if (isme) { stopChanges(); }
		return vertex;
	}

	/** Removes a vertex and all its connected edges from the graph. */
	public void removeVertex(Object vertex) {
		boolean isme = ensureChanges();
		graph.removeVertex(vertex);
		delta.addEvent(GraphChangeEvent.VERTEX_REMOVED, vertex);
		if (isme) { stopChanges(); }
	}

	/** Returns outgoing edges of a vertex. */
	public Collection<Object> getOutgoingEdges(Object vertex) {
		return graph.getOutgoingEdges(vertex);
	}
	
	/** Returns incoming edges of a vertex. */
	public Collection<Object> getIncomingEdges(Object vertex) {
		return graph.getIncomingEdges(vertex);
	}
	

	/** Returns informations attached to an edge. */
	public IUserInfo getEdgeInfo(Object edge) {
		return graph.getEdgeInfo(edge);
	}
	
	/** Sets informations attached to an edge. */
	public void setEdgeInfo(Object edge, IUserInfo info) {
		boolean isme = ensureChanges();
		graph.setEdgeInfo(edge, info);
		delta.addEvent(GraphChangeEvent.EDGE_CHANGED, edge);
		if (isme) { stopChanges(); }
	}
	
	/** Returns an iterable on graph edges. */
	public Iterable<Object> getEdges() {
		return graph.getEdges();
	}

	/** 
	 * Creates an edge in the graph with attached info. 
	 * 
	 * @param source source vertex of the edge to create.
	 * @param target target vertex of the edge to create.
	 * @param info user informations to attach to the edge.
	 * @return created edge.
	 */
	public Object createEdge(Object source, Object target, IUserInfo info) {
		boolean isme = ensureChanges();
		Object edge = graph.createEdge(source, target, info);
		delta.addEvent(GraphChangeEvent.EDGE_CREATED, edge);
		if (isme) { stopChanges(); }
		return edge;
	}

	/** Removes an edge from the graph. */
	public void removeEdge(Object edge) {
		boolean isme = ensureChanges();
		graph.removeEdge(edge);
		delta.addEvent(GraphChangeEvent.EDGE_REMOVED, edge);
		if (isme) { stopChanges(); }
	}
	
	/** Returns source of an edge. */
	public Object getEdgeSource(Object edge) {
		return graph.getEdgeSource(edge);
	}
	
	/** Returns target of an edge. */
	public Object getEdgeTarget(Object edge) {
		return graph.getEdgeTarget(edge);
	}
	

	
	/** Returns a total order on vertices. */
	public ITotalOrder<Object> getVerticesTotalOrder() {
		return graph.getVerticesTotalOrder();
	}

	/** Returns a total order on edges. */
	public ITotalOrder<Object> getEdgesTotalOrder() {
		return graph.getEdgesTotalOrder();
	}

	/** Adds an instance adaptation. */
	public void addAdaptation(Class c, IAdapter adapter) {
		graph.addAdaptation(c, adapter);
	}

	/** Adapts the graph to some another type. */
	public <T> Object adapt(Class<T> c) {
		Object result = graph.adapt(c);
		return (result == null) ?
				AdaptUtils.externalAdapt(this,c) : 
				result;
	}
	
	/** Extracts some attribute from a vertex or edge. */
	@SuppressWarnings("unchecked")
	public <T> T extract(Object vertexOrEdge, String attr) {
		return (T) getUserInfoOf(vertexOrEdge).getAttribute(attr);
	}
	
	/** Project a collection of vertices or edges along some attribute. */
	public <T> Iterable<T> project(Iterable<Object> it, String attr) {
		return GraphQueryUtils.project(this,it,attr);
	}
	
	/** Computes a aggregate function value along a collection of vertices 
	 * or edges. */
	public <T> T compute(Iterable<Object> it, String attr, IAggregateFunction<T> f) {
		return GraphQueryUtils.compute(this, it, attr, f);
	}
	
}
