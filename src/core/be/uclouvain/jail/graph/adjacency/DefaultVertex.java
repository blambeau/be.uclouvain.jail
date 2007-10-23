package be.uclouvain.jail.graph.adjacency;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.UserInfoCapable;

/**
 * <p>Implementation of the Vertex contract, using an HashMap for vertex attributes. This map 
 * allows null key and value that can thus be seen as particular attribute keys and/or values.
 * This map is returned by getAttributes() and can be used as external way to write attributes.
 * Incoming and outgoing edges are keeped in linked lists, leading to quite efficient access 
 * when adding and/or removing edges as well as iterating them.</p>
 * 
 * @author LAMBEAU Bernard
 */
public class DefaultVertex extends UserInfoCapable implements IVertex {

	/* instance variables SECTION ------------------------------------------------------------------- */
	/** The adjacent list for incoming edges. */
	protected List<IEdge> incomingEdges = new LinkedList<IEdge>();

	/** The adjacent list for outgoing edges. */
	protected List<IEdge> outgoingEdges = new LinkedList<IEdge>();

	/* CONSTRUCTORS SECTION ---------------------------------------------------------------------- */
	/** Empty constructor. Installs a MapUserInfo. */
	public DefaultVertex() {
		super();
	}

	/** Constructor with specific user info. */
	public DefaultVertex(IUserInfo info) {
		super(info);
	}

	/* incoming edges COLLECTION PROPERTY SECTION ------------------------------------------------ */
	/** Returns incoming edges. */
	public Collection<IEdge> getIncomingEdges(IDirectedGraph graph) {
		return incomingEdges;
	}

	/** Adds an incoming edge. */
	public void addIncomingEdge(IDirectedGraph graph, IEdge e) {
		incomingEdges.add(e);
	}

	/** Removes an incoming edge. */
	public void removeIncomingEdge(IDirectedGraph graph, IEdge e) {
		incomingEdges.remove(e);
	}

	/* outgoing edges COLLECTION PROPERTY SECTION ------------------------------------------------ */
	/** Returns outgoing edges. */
	public Collection<IEdge> getOutgoingEdges(IDirectedGraph graph) {
		return outgoingEdges;
	}

	/** Adds an outgoing edge. */
	public void addOutgoingEdge(IDirectedGraph graph, IEdge e) {
		outgoingEdges.add(e);
	}

	/** Removes an outgoing edge. */
	public void removeOutgoingEdge(IDirectedGraph graph, IEdge e) {
		outgoingEdges.remove(e);
	}

}