package be.uclouvain.jail.graph.adjacency;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.chefbe.javautils.adapt.AdaptUtils;
import net.chefbe.javautils.adapt.Adaptations;
import net.chefbe.javautils.adapt.IAdapter;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.deco.DirectedGraph;
import be.uclouvain.jail.graph.utils.ITotalOrder;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.UserInfoCapable;

/**
 * Provides a mutable directed graph using adjacency list structure. 
 * 
 * <p>The aim of this implementation is to provide an efficient mutable directed graph 
 * object to be used in the JAIL library.</p>
 * 
 * @author blambeau
 */
public class AdjacencyDirectedGraph extends UserInfoCapable implements IDirectedGraph {

	/* instance variables SECTION ---------------------------------------------------------------- */
	/** Vertices indexed collection. */
	protected List<IVertex> vertices = new ArrayList<IVertex>();

	/** Vertices ids are in order compared to the vertices collection ? */
	protected boolean verticesAreInOrder = true;

	/** Edges indexed collection. */
	protected List<IEdge> edges = new ArrayList<IEdge>();

	/** Edge ids are in order compared to the edges collection ? */
	protected boolean edgesAreInOrder = true;

	/** Factory to use for states and edges. */
	protected IGraphComponentFactory componentFactory;

	/** External instance adaptations. */
	protected Adaptations adaptations;
	
	/* CONSTRUCTORS SECTION ---------------------------------------------------------------------- */
	/** Creates a directed graph with DefaultXXX factory. */  
	public AdjacencyDirectedGraph() {
		this(new DefaultGraphComponentFactory());
	}

	/** Constructor specifying the component factory to use. */
	public AdjacencyDirectedGraph(IGraphComponentFactory componentFactory) {
		this.componentFactory = componentFactory;
	}

	/* vertices collection methods --------------------------------------------------------------- */
	/** Asserts that a vertex is recognized. */
	protected void assertCorrectVertex(Object vertex) {
		if (vertex instanceof IVertex == false) {
			throw new IllegalArgumentException("Not a correct vertex " + vertex);
		}
		if (verticesAreInOrder) {
			int id = ((IVertex)vertex).getId();
			IVertex v = vertices.get(id);
			if (!vertex.equals(v)) {
				throw new IllegalArgumentException("Not a correct vertex " + vertex);
			}
		} else if (!vertices.contains(vertex)) {
			throw new IllegalArgumentException("Not a correct vertex " + vertex);
		}
	}
	
	/** Returns UserInfo attached to a vertex or an edge. */
	public IUserInfo getUserInfoOf(Object vertexOrEdge) {
		if (vertexOrEdge instanceof IVertex) {
			return ((IVertex)vertexOrEdge).getUserInfo();
		} else if (vertexOrEdge instanceof IEdge) {
			return ((IEdge)vertexOrEdge).getUserInfo();
		} else {
			throw new IllegalArgumentException("Bad edge or vertex.");
		}
	}

	/** Returns informations attached to a vertex. */
	public IUserInfo getVertexInfo(Object vertex) {
		assertCorrectVertex(vertex);
		return ((IVertex)vertex).getUserInfo();
	}
	
	/** Sets informations attached to a vertex. */
	public void setVertexInfo(Object vertex, IUserInfo info) {
		assertCorrectVertex(vertex);
		((IVertex)vertex).setUserInfo(info);
	}
	
	/** Returns all graph vertices as a Collection. */
	@SuppressWarnings("unchecked")
	public Collection getVertices() {
		return Collections.unmodifiableCollection(vertices);
	}

	/** Returns the number of vertices in the graph. */
	public int getVerticesCount() {
		return getVertices().size();
	}

	/** Factors a vertex instance. */
	protected IVertex factorVertex(IUserInfo info) {
		return componentFactory.createVertex(info);
	}

	/** Adds a vertex in the graph. */
	protected void addVertex(IVertex vertex) {
		vertex.setId(getVerticesCount());
		vertices.add(vertex);
	}

	/** Creates a vertex in the graph. */
	public Object createVertex(IUserInfo info) {
		IVertex vertex = factorVertex(info);
		addVertex(vertex);
		return vertex;
	}

	/**
	 * <p>Removes a vertex from the graph. All edges connected to the vertex are also
	 * removed from the graph to keep its 'correctly connected property'.</p>
	 * 
	 * <p>This method does not keep vertices in order in the most general case. However,
	 * removing the last added vertex keep vertices in order. The verticesAreInOrder 
	 * flag is update accordingly. As this method reuse the removeEdge(Edge) one, edges
	 * does not stay in order in the most general case.</p>
	 * 
	 * <p>This method works in O(m*deg(v) + n), being no such efficient !</p> 
	 * 
	 * @param vertex a graph vertex to remove from the graph. 
	 */
	protected void removeVertex(IVertex vertex) {
		assertCorrectVertex(vertex);

		/* remove all incoming and outgoing edges */
		for (Object edge : vertex.getIncomingEdges(this).toArray()) {
			removeEdge(edge);
		}
		for (Object edge : vertex.getOutgoingEdges(this).toArray()) {
			removeEdge(edge);
		}

		/* remove the vertex */
		vertices.remove(vertex);

		/* stay in order only if vertex was the last vertex */
		verticesAreInOrder = (vertex.getId() == getVerticesCount());
	}

	/** Removes a vertex from the graph. */
	public void removeVertex(Object vertex) {
		assertCorrectVertex(vertex);
		removeVertex((IVertex) vertex);
	}

	
	
	
	
	/** Asserts that an edge is recognized. */
	protected void assertCorrectEdge(Object edge) {
		if (edge instanceof IEdge == false) {
			throw new IllegalArgumentException("Not a correct edge " + edge);
		}
		if (edgesAreInOrder) {
			int id = ((IEdge)edge).getId();
			IEdge e = edges.get(id);
			if (!edge.equals(e)) {
				throw new IllegalArgumentException("Not a correct edge " + edge);
			}
		} else if (!edges.contains(edge)) {
			throw new IllegalArgumentException("Not a correct edge " + edge);
		}
	}
	
	/** Returns informations attached to an edge. */
	public IUserInfo getEdgeInfo(Object edge) {
		assertCorrectEdge(edge);
		return ((IEdge)edge).getUserInfo();
	}
	
	/** Sets informations attached to a vertex. */
	public void setEdgeInfo(Object edge, IUserInfo info) {
		assertCorrectEdge(edge);
		((IEdge)edge).setUserInfo(info);
	}

	/** Returns all graph edges as a Collection. */
	@SuppressWarnings("unchecked")
	public Collection getEdges() {
		return Collections.unmodifiableCollection(edges);
	}

	/** Returns the number of edges in the graph. */
	public int getEdgesCount() {
		return getEdges().size();
	}

	/** Factors an edge instance. */
	protected IEdge factorEdge(IUserInfo info) {
		return componentFactory.createEdge(info);
	}

	/** Adds an edge in the graph. */
	protected void addEdge(IEdge edge, IVertex source, IVertex target) {
		/* identification */
		edge.setId(getEdgesCount());

		/* connection */
		edge.setSource(source);
		edge.setTarget(target);
		
		/* connection continued. */
		source.addOutgoingEdge(this,edge);
		target.addIncomingEdge(this,edge);

		/* collection adding */
		edges.add(edge);
	}

	/** Creates an edge in the graph. */
	public Object createEdge(Object source, Object target, IUserInfo info) {
		assertCorrectVertex(source);
		assertCorrectVertex(target);
		IEdge edge = factorEdge(info);
		addEdge(edge,(IVertex)source,(IVertex)target);
		return edge;
	}

	/**
	 * <p>Removes an edge from the graph. Source and target vertex structures are
	 * updated.</p>
	 * 
	 * <p>This method does not keep edges in order in the most general case. However,
	 * removing the last added edge keep edges in order. The edgesAreInOrder flag is 
	 * updated accordingly.</p>
	 * 
	 * <p>This method works in O(m), being no such efficient !</p> 
	 * 
	 * @param edge a graph edge to remove from the graph. 
	 */
	protected void removeEdge(IEdge edge) {
		assertCorrectEdge(edge);

		/* remove from source and target */
		IVertex source = edge.getSource();
		source.removeOutgoingEdge(this,edge);
		IVertex target = edge.getTarget();
		target.removeIncomingEdge(this,edge);

		/* remove edge */
		edges.remove(edge);

		/* stay in order only if edge was the last edge */
		edgesAreInOrder = (edge.getId() == getEdgesCount());
	}

	/** Removes an edge from the graph. */
	public void removeEdge(Object edge) {
		assertCorrectEdge(edge);
		removeEdge((IEdge) edge);
	}

	/* ----------------------------------------------------------------------------------------------
	 * DirectedGraph semantics methods.
	 *   + vertices semantics methods. 
	 *   + edges semantics methods.
	 * ------------------------------------------------------------------------------------------- */

	/* vertices semantics methods ---------------------------------------------------------------- */
	/** Returns incoming edges of a vertex. */
	protected Collection getIncomingEdges(IVertex vertex) {
		return Collections.unmodifiableCollection(vertex.getIncomingEdges(this));
	}

	/** Returns incoming edges of a vertex. */
	@SuppressWarnings("unchecked")
	public Collection<Object> getIncomingEdges(Object vertex) {
		assertCorrectVertex(vertex);
		return getIncomingEdges((IVertex) vertex);
	}

	/** Returns outgoing edges of a vertex. */
	protected Collection getOutgoingEdges(IVertex vertex) {
		return Collections.unmodifiableCollection(vertex.getOutgoingEdges(this));
	}

	/** Returns outgoing edges of a vertex. */
	@SuppressWarnings("unchecked")
	public Collection<Object> getOutgoingEdges(Object vertex) {
		assertCorrectVertex(vertex);
		return getOutgoingEdges((IVertex) vertex);
	}

	/** Returns the source vertex of an edge. */
	protected IVertex getEdgeSource(IEdge edge) {
		return edge.getSource();
	}

	/** Returns the source vertex of an edge. */
	public Object getEdgeSource(Object edge) {
		assertCorrectEdge(edge);
		return getEdgeSource((IEdge) edge);
	}

	/** Returns the target vertex of an edge. */
	protected IVertex getEdgeTarget(IEdge edge) {
		return edge.getTarget();
	}

	/** Returns the target vertex of an edge. */
	public Object getEdgeTarget(Object edge) {
		assertCorrectEdge(edge);
		return getEdgeTarget((IEdge) edge);
	}


	
	
	
	
	/* ----------------------------------------------------------------------------------------------
	 * DirectedGraph low level methods
	 *   - states and edges identification utilities.
	 *   - spacial complexity utilities.
	 *   - sorting utilities.
	 * ------------------------------------------------------------------------------------------- */

	/* identification utilities ------------------------------------------------------------------ */
	/**
	 * <p>This method reidentifies all graph vertices to ensure that their identifiers correspond to 
	 * their positions in the vertices array list.</p> 
	 * 
	 * <p>No check is done on the verticesAreInOrder flag before reidentifying. The flag is set to 
	 * true when reidentification is done.</p> 
	 * 
	 * <p>This method takes O(n) time.</p> 
	 */
	protected void reidentifyVertices() {
		int size = getVerticesCount();
		for (int i = 0; i < size; i++) {
			IVertex v = vertices.get(i);
			v.setId(i);
		}
		verticesAreInOrder = true;
	}

	/**
	 * <p>This method reidentifies all graph edges to ensure that their identifiers correspond to 
	 * their positions in the edges array list.</p> 
	 * 
	 * <p>No check is done on the edgesAreInOrder flag before reidentifying. The flag is set to true 
	 * when reidentification is done.</p>
	 *  
	 * <p>This method takes O(m) time.</p> 
	 */
	protected void reidentifyEdges() {
		int size = getEdgesCount();
		for (int i = 0; i < size; i++) {
			IEdge e = edges.get(i);
			e.setId(i);
		}
		edgesAreInOrder = true;
	}

	/**
	 * <p>This method proposes a complete clean of vertices and edges collections.
	 * Collection sizes are trimmed, then identification is called on all values.</p>
	 * 
	 * <p>This method is semantically equivalent to the calls :<ol> 
	 *   <li>reidentifyVertexes(true);</li>
	 *   <li>reidentifyEdges(true);</li></ol>
	 * </p>
	 * 
	 * <p>In other words, an autonumber of vertices and edges is made. This autonumbering 
	 * ensures that vertex and edge identifiers correspond to their index in the collections.</p>
	 */
	protected void reidentifyAll() {
		reidentifyVertices();
		reidentifyEdges();
	}

	/* Cleaning utilities ------------------------------------------------------------------------ */
	/**
	 * <p>Removes any lost space in the different physical structures.</p>
	 */
	protected void cleanSizes() {
		if (vertices instanceof ArrayList) {
			((ArrayList) vertices).trimToSize();
		}
		if (edges instanceof ArrayList) {
			((ArrayList) edges).trimToSize();
		}
	}

	/* Sort utilities ---------------------------------------------------------------------------- */
	/**
	 * <p>Sorts vertices according to a comparator order ; reidentification can be forced using the 
	 * reidentify parameter.</p>
	 * 
	 * <p>After sorting, vertices are identified according to the reidentify parameter value. 
	 * The verticesAreInOrder flag is updated accordingly.</p> 
	 * 
	 * @param comparator the comparator used as ordering information of vertices.
	 * @param reidentify vertices must be reidentified after sorting ?
	 */
	public void sortVertices(Comparator<IVertex> comparator, boolean reidentify) {
		Collections.sort(vertices, comparator);
		if (reidentify) {
			reidentifyVertices();
		}
		verticesAreInOrder = reidentify;
	}

	/**
	 * <p>Sorts edges according to a comparator order ; reidentification can be forced using the 
	 * reidentify parameter.</p>
	 * 
	 * <p>After sorting, edges are identified according to the reidentify parameter value. 
	 * The edgesAreInOrder flag is updated accordingly.</p> 
	 * 
	 * @param comparator the comparator used as ordering information of edges.
	 * @param reidentify edges must be reidentified ?
	 */
	public void sortEdges(Comparator<IEdge> comparator, boolean reidentify) {
		Collections.sort(edges, comparator);
		if (reidentify) {
			reidentifyEdges();
		}
		edgesAreInOrder = reidentify;
	}

	/* ----------------------------------------------------------------------------------------------
	 * Total ordering utilities.
	 *   + vertices total ordering. 
	 *   + edges total ordering.
	 * ------------------------------------------------------------------------------------------- */
	/**
	 * <p>Returns a total order informer on vertices. The default implementation is overrided to
	 * provide a really efficient informer. The graph should not be changed in any way while using the 
	 * informer. Note also that vertices must potentially be reidentified before returning the informer.</p> 
	 */
	public ITotalOrder<Object> getVerticesTotalOrder() {
		if (!verticesAreInOrder) {
			reidentifyVertices();
		}
		return new VerticesTotalOrder();
	}

	/**
	 * <p>Returns a total ordering informer on edges. The default implementation is overrided to
	 * provide a really efficient informer. The graph should not be changed in any way while using the 
	 * informer. Note also that edges must potentially be reidentified before returning the informer.</p> 
	 */
	public ITotalOrder<Object> getEdgesTotalOrder() {
		if (!edgesAreInOrder) {
			reidentifyEdges();
		}
		return new EdgesTotalOrder();
	}

	/**
	 * <p>The total order informer used for vertices. This class has efficient spacial and temporal
	 * complexities : no adding space is used and all methods execute in O(1). However the graph should 
	 * never change in any way when using the informer.</p> 
	 * 
	 * @author LAMBEAU Bernard
	 */
	class VerticesTotalOrder implements ITotalOrder<Object> {

		/** Forces vertices to be in order. */
		public VerticesTotalOrder() {
			if (!verticesAreInOrder) {
				reidentifyVertices();
			}
		}
		
		/** Returns vertices collection size. */
		public int size() {
			return vertices.size();
		}

		/** Returns all vertices of the graph in a array. */
		public Object[] getTotalOrder() {
			return vertices.toArray();
		}

		/** Returns a vertice index. */
		public int indexOf(Object element) {
			return ((IVertex) element).getId();
		}

		/** Returns the index-th vertices in the graph. */
		public Object getElementAt(int index) {
			return vertices.get(index);
		}

		/** Comparison on the total order. */
		public int compare(Object arg0, Object arg1) {
			int iIndex = indexOf(arg0);
			int jIndex = indexOf(arg1);
			return iIndex == jIndex ? 0 : iIndex < jIndex ? -1 : 1;
		}

		/** Returns an iterator on vertices. */
		@SuppressWarnings("unchecked")
		public Iterator<Object> iterator() {
			Iterator i = vertices.iterator();
			return i;
		}

	}

	/**
	 * <p>The total order informer used for edges. This class has efficient spacial and temporal
	 * complexities : no adding space is used and all methods execute in O(1). However the graph 
	 * should never change in any way when using the informer.</p> 
	 * 
	 * @author LAMBEAU Bernard
	 */
	class EdgesTotalOrder implements ITotalOrder<Object> {

		/** Forces vertices to be in order. */
		public EdgesTotalOrder() {
			if (!edgesAreInOrder) {
				reidentifyEdges();
			}
		}
		
		/** Returns vertices collection size. */
		public int size() {
			return edges.size();
		}

		/** Returns all edges of the graph in a array. */
		public Object[] getTotalOrder() {
			return edges.toArray();
		}

		/** Returns an edge index. */
		public int indexOf(Object element) {
			return ((IEdge) element).getId();
		}

		/** Returns the index-th edges in the graph. */
		public Object getElementAt(int index) {
			return edges.get(index);
		}

		/** Comparison on the total order. */
		public int compare(Object arg0, Object arg1) {
			int iIndex = indexOf(arg0);
			int jIndex = indexOf(arg1);
			return iIndex == jIndex ? 0 : iIndex < jIndex ? -1 : 1;
		}

		/** Returns an iterator on vertices. */
		@SuppressWarnings("unchecked")
		public Iterator<Object> iterator() {
			Iterator i = edges.iterator();
			return i;
		}

	}

	/** Adds an instance adaptation. */
	public void addAdaptation(Class c, IAdapter adapter) {
		if (adaptations == null) {
			adaptations = new Adaptations();
		}
		adaptations.register(c, adapter);
	}
	
	/** Adaptability implementation. */
	public <T> Object adapt(Class<T> c) {
		if (c.isAssignableFrom(this.getClass())) {
			return this;
		}
		
		if (adaptations != null) {
			// try instance adaptation
			Object iAdapt = adaptations.adapt(this, c);
			if (iAdapt != null) { return iAdapt; }
		}
		
		// try specific class adaptation
		if (DirectedGraph.class.equals(c)) {
			return new DirectedGraph(this);
		}
		
		// try external class adaptation
		return AdaptUtils.externalAdapt(this,c);
	}

}
