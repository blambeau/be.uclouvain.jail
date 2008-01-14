package be.uclouvain.jail.adapt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.chefbe.javautils.adapt.AdaptUtils;
import net.chefbe.javautils.adapt.IAdapter;
import net.chefbe.javautils.adapt.network.NetworkEdge;
import net.chefbe.javautils.adapt.network.NetworkGraph;
import net.chefbe.javautils.adapt.network.NetworkNode;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.utils.CopyTotalOrder;
import be.uclouvain.jail.graph.utils.ITotalOrder;
import be.uclouvain.jail.graph.utils.ListTotalOrder;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.IUserInfoHelper;
import be.uclouvain.jail.uinfo.UserInfoHelper;

/**
 * Decorates the NetworkGraph to a graph.
 * 
 * @author blambeau
 */
public class NetworkGraphDecorator implements IDirectedGraph {

	/** Helper to use. */
	private IUserInfoHelper helper = UserInfoHelper.instance();
	
	/** Info. */
	private IUserInfo info = helper.install();
	
	/** Decorated graph. */
	private NetworkGraph graph;
	
	/** Decorates the network graph. */
	public NetworkGraphDecorator(NetworkGraph graph) {
		this.graph = graph;
	}

	/** Returns user info attached to the graph. */
	public IUserInfo getGraphInfo() {
		return info;
	}

	/** Returns user info attached to the graph. */
	public IUserInfo getUserInfo() {
		return info;
	}

	/** Attachs some user info to the graph. */
	public void setUserInfo(IUserInfo info) {
		this.info = info;
	}

	/** Returns UserInfo attached to an edge or a vertex. */
	public IUserInfo getUserInfoOf(Object vertexOrEdge) {
		if (vertexOrEdge instanceof NetworkEdge) {
			return getEdgeInfo(vertexOrEdge);
		} else if (vertexOrEdge instanceof NetworkNode) {
			return getVertexInfo(vertexOrEdge);
		} else {
			throw new IllegalArgumentException("Unrecognized element: " + vertexOrEdge);
		}
	}
	
	
	
	/** Returns informations attached to a vertex. */
	public IUserInfo getVertexInfo(Object vertex) {
		NetworkNode node = (NetworkNode) vertex;
		helper.addKeyValue("label", node.getDomain().getSimpleName());
		helper.addKeyValue("shape", "box");
		return helper.install();
	}
	
	/** Sets informations attached to a vertex. */
	public void setVertexInfo(Object vertex, IUserInfo info) {
		throw new UnsupportedOperationException();
	}
	
	/** Returns an iterable on graph vertices. */
	@SuppressWarnings("unchecked")
	public Iterable<Object> getVertices() {
		Collection col = graph.getDomains();
		return col;
	}

	/** 
	 * Creates a new vertex in the graph with associated infos.
	 * 
	 * @param info user informations to attach to the vertex.
	 * @return created vertex.
	 */
	public Object createVertex(IUserInfo info) {
		throw new UnsupportedOperationException();
	}

	/** Removes a vertex and all its connected edges from the graph. */
	public void removeVertex(Object vertex) {
		throw new UnsupportedOperationException();
	}

	/** Returns outgoing edges of a vertex. */
	@SuppressWarnings("unchecked")
	public Collection<Object> getOutgoingEdges(Object vertex) {
		List nodes = ((NetworkNode)vertex).getOutgoingEdges();
		return nodes;
	}
	
	/** Returns incoming edges of a vertex. */
	@SuppressWarnings("unchecked")
	public Collection<Object> getIncomingEdges(Object vertex) {
		throw new UnsupportedOperationException();
	}
	

	/** Returns informations attached to an edge. */
	public IUserInfo getEdgeInfo(Object edge) {
		NetworkEdge e = (NetworkEdge) edge;
		return helper.keyValue("label", e.getAdapter().toString());
	}
	
	/** Sets informations attached to an edge. */
	public void setEdgeInfo(Object edge, IUserInfo info) {
		throw new UnsupportedOperationException();
	}
	
	/** Returns an iterable on graph edges. */
	public Iterable<Object> getEdges() {
		List<Object> edges = new ArrayList<Object>();
		for (NetworkNode node: graph) {
			edges.addAll(node.getOutgoingEdges());
		}
		return edges;
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
		throw new UnsupportedOperationException();
	}

	/** Removes an edge from the graph. */
	public void removeEdge(Object edge) {
		throw new UnsupportedOperationException();
	}
	
	/** Returns source of an edge. */
	public Object getEdgeSource(Object edge) {
		return ((NetworkEdge)edge).getSource();
	}
	
	/** Returns target of an edge. */
	public Object getEdgeTarget(Object edge) {
		return ((NetworkEdge)edge).getTarget();
	}
	

	
	/** Returns a total order on vertices. */
	@SuppressWarnings("unchecked")
	public ITotalOrder<Object> getVerticesTotalOrder() {
		return new CopyTotalOrder(graph.getDomains());
	}

	/** Returns a total order on edges. */
	public ITotalOrder<Object> getEdgesTotalOrder() {
		List<Object> edges = new ArrayList<Object>();
		for (NetworkNode node: graph) {
			edges.addAll(node.getOutgoingEdges());
		}
		return new ListTotalOrder<Object>(edges);
	}

	/** Adds an external adaptation. */
	public void addAdaptation(Class c, IAdapter adapter) {
		throw new UnsupportedOperationException();
	}

	/** Adapts to some type. */
	public <T> Object adapt(Class<T> c) {
		return AdaptUtils.externalAdapt(this,c);
	}

}
