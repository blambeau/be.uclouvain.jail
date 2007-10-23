package be.uclouvain.jail.graph.adjacency;

import java.util.Collection;

import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.uinfo.IUserInfo;

/**
 * Contract to be a valid vertex of a DirectedGraph implemented with Adjacency 
 * List Structure.
 * 
 * @author LAMBEAU Bernard
 */
public interface IVertex {

	/** Returns the vertex id. */
	public int getId();

	/** Sets the vertex id. */
	public void setId(int id);

	/** Returns attached user information. */
	public IUserInfo getUserInfo();

	/** Sets attached user information. */
	public void setUserInfo(IUserInfo info);

	/** Returns the incoming edges of the vertex. */
	public Collection<IEdge> getIncomingEdges(IDirectedGraph graph);

	/** Adds an incoming edge to the vertex incoming edges collection. */
	public void addIncomingEdge(IDirectedGraph graph, IEdge edge);

	/** Removes an incoming edge to the vertex incoming edges collection. */
	public void removeIncomingEdge(IDirectedGraph graph, IEdge edge);

	/** Returns the outgoing edges of the vertex. */
	public Collection<IEdge> getOutgoingEdges(IDirectedGraph graph);

	/** Adds an outgoing edge to the vertex outgoing edges collection. */
	public void addOutgoingEdge(IDirectedGraph graph, IEdge edge);

	/** Removes an outgoing edge to the vertex outgoing edges collection. */
	public void removeOutgoingEdge(IDirectedGraph graph, IEdge edge);

}