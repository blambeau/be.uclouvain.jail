package be.uclouvain.jail.graph;

import java.util.Collection;

import net.chefbe.javautils.adapt.IOpenAdaptable;
import be.uclouvain.jail.graph.adjacency.AdjacencyDirectedGraph;
import be.uclouvain.jail.graph.utils.ITotalOrder;
import be.uclouvain.jail.uinfo.IUserInfo;

/**
 * DirectedGraph abstraction. 
 * 
 * <p>A directed graph is basically a data structure providing vertices connected 
 * by directed edges. Vertices and edges may contain arbitrary values installed as 
 * (key,value) pairs in {@link IUserInfo} instances.</p> 
 * 
 * <p>This interface is kept as simple and opened as possible in order to allow
 * different implementations to be used in JAIL. For this reason, the following 
 * choices have been made:</p>
 * <ul>
 *     <li>Vertices and edges are considered as <code>Object</code>S, which leads to
 *         a non user-friendly API but allows strong hidding information with respect 
 *         to the actual graph implementation.</li>
 *     <li>Vertices and edges collections are abstracted as <code>Iterable</code>S to
 *         allow <code>Set</code>, <code>List</code> or any kind of collection to be
 *         used. As many algorithms require some indexing or ordering of vertices 
 *         and/or edges, this interface provides {@link ITotalOrder}S for these
 *         collections.</li>
 *     <li>All utillity (shortcut) methods as well as high level utilities (listening,
 *         constraints, etc.) are left outside this interface.</li>
 * </ul>
 * 
 * <p>This interface may be implemented. Standard implementation is provided by 
 * {@link AdjacencyDirectedGraph} class.</p>
 * 
 * @author LAMBEAU Bernard
 */
public interface IDirectedGraph extends IOpenAdaptable, IDirectedGraphWriter {

	/** Returns user info attached to the graph. */
	public IUserInfo getUserInfo();

	/** Attachs some user info to the graph. */
	public void setUserInfo(IUserInfo info);
	

	/** Returns UserInfo attached to an edge or a vertex. */
	public IUserInfo getUserInfoOf(Object vertexOrEdge);
	
	
	
	/** Returns informations attached to a vertex. */
	public IUserInfo getVertexInfo(Object vertex);
	
	/** Sets informations attached to a vertex. */
	public void setVertexInfo(Object vertex, IUserInfo info);
	
	/** Returns an iterable on graph vertices. */
	public Iterable<Object> getVertices();

	/** 
	 * Creates a new vertex in the graph with associated infos.
	 * 
	 * @param info user informations to attach to the vertex.
	 * @return created vertex.
	 */
	public Object createVertex(IUserInfo info);

	/** Removes a vertex and all its connected edges from the graph. */
	public void removeVertex(Object vertex);

	/** Returns outgoing edges of a vertex. */
	public Collection<Object> getOutgoingEdges(Object vertex);
	
	/** Returns incoming edges of a vertex. */
	public Collection<Object> getIncomingEdges(Object vertex);
	

	/** Returns informations attached to an edge. */
	public IUserInfo getEdgeInfo(Object edge);
	
	/** Sets informations attached to an edge. */
	public void setEdgeInfo(Object edge, IUserInfo info);
	
	/** Returns an iterable on graph edges. */
	public Iterable<Object> getEdges();

	/** 
	 * Creates an edge in the graph with attached info. 
	 * 
	 * @param source source vertex of the edge to create.
	 * @param target target vertex of the edge to create.
	 * @param info user informations to attach to the edge.
	 * @return created edge.
	 */
	public Object createEdge(Object source, Object target, IUserInfo info);

	/** Removes an edge from the graph. */
	public void removeEdge(Object edge);
	
	/** Returns source of an edge. */
	public Object getEdgeSource(Object edge); 
	
	/** Returns target of an edge. */
	public Object getEdgeTarget(Object edge); 
	

	
	/** Returns a total order on vertices. */
	public ITotalOrder<Object> getVerticesTotalOrder();

	/** Returns a total order on edges. */
	public ITotalOrder<Object> getEdgesTotalOrder();

}