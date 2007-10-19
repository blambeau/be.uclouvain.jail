package be.uclouvain.jail.fa.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.adjacency.IEdge;
import be.uclouvain.jail.graph.adjacency.IVertex;
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
public class DFAVertex extends UserInfoCapable implements IVertex {

	/** The adjacent list for outgoing edges. */
	protected Map<Object,IEdge> outgoingEdges = new TreeMap<Object,IEdge>();

	/* CONSTRUCTORS SECTION ---------------------------------------------------------------------- */
	/** Empty constructor. Installs a MapUserInfo. */
	public DFAVertex() {
		this(null);
	}

	/** Constructor with specific user info. */
	public DFAVertex(IUserInfo info) {
		super(info);
	}
	
	/** Returns DFA state info. */
	private IDFAStateInfo toDFAStateInfo() {
		IUserInfo uInfo = getUserInfo();
		IDFAStateInfo info = uInfo instanceof IDFAStateInfo ?
				            (IDFAStateInfo) uInfo :
				            (IDFAStateInfo) getUserInfo().adapt(IDFAStateInfo.class);
		if (info == null) {
			throw new IllegalArgumentException("Vertex infos must be IDFAVertexInfo adaptable to be used in a DFA.");
		} else {
			return info;
		}
	}
	/** Checks if this state is initial. */
	public boolean isInitial() {
		return toDFAStateInfo().isInitial();
	}
	
	/** Checks if this state is accepting. */
	public boolean isAccepting() {
		return toDFAStateInfo().isAccepting();
	}
	
	/** Checks if this state is initial. */
	public boolean isError() {
		return toDFAStateInfo().isError();
	}

	/* incoming edges COLLECTION PROPERTY SECTION ------------------------------------------------ */
	/* (non-Javadoc)
	 * @see be.ac.ucl.info.rq.jatc.v3model.Vertex#incomingEdges()
	 */
	public Collection<IEdge> getIncomingEdges(IDirectedGraph graph) {
		List<IEdge> list = new ArrayList<IEdge>();
		for (Object edge: graph.getEdges()) {
			if (this.equals(graph.getEdgeTarget(edge))) {
				list.add((IEdge)edge);
			}
		}
		return list;
	}

	/* (non-Javadoc)
	 * @see be.ac.ucl.info.rq.jatc.v3model.Vertex#addIncomingEdge(be.ac.ucl.info.rq.automaton.stdmodel.Edge)
	 */
	public void addIncomingEdge(IEdge e) {
	}

	/* (non-Javadoc)
	 * @see be.ac.ucl.info.rq.jatc.v3model.Vertex#removeIncomingEdge(be.ac.ucl.info.rq.automaton.stdmodel.Edge)
	 */
	public void removeIncomingEdge(IEdge e) {
	}

	/* (non-Javadoc)
	 * @see be.ac.ucl.info.rq.jatc.v3model.Vertex#outgoingEdges()
	 */
	public Collection<IEdge> getOutgoingEdges(IDirectedGraph graph) {
		return outgoingEdges.values();
	}

	/* (non-Javadoc)
	 * @see be.ac.ucl.info.rq.jatc.v3model.Vertex#addOutgoingEdge(be.ac.ucl.info.rq.automaton.stdmodel.Edge)
	 */
	public void addOutgoingEdge(IEdge e) {
		if (e instanceof DFAEdge == false) {
			throw new IllegalArgumentException("DFAEdge expected.");
		}
		outgoingEdges.put(((DFAEdge)e).letter(),e);
	}

	/* (non-Javadoc)
	 * @see be.ac.ucl.info.rq.jatc.v3model.Vertex#removeOutgoingEdge(be.ac.ucl.info.rq.automaton.stdmodel.Edge)
	 */
	public void removeOutgoingEdge(IEdge e) {
		outgoingEdges.remove(e);
	}

	/** Returns the target through a letter. */
	public DFAVertex target(Object letter) {
		IEdge edge = outgoingEdges.get(letter);
		if (edge == null) { return null; }
		IVertex vertex = edge.getTarget();
		if (vertex instanceof DFAVertex) {
			return (DFAVertex) vertex;
		} else {
			throw new IllegalStateException("DFAVertex expected.");
		}
	}

	/** Returns outgoing letters of this state. */
	public Collection<Object> getOutgoingLetters(IDirectedGraph graph) {
		return outgoingEdges.keySet();
	}
	
	/** Returns outgoing letters of this state. */
	public Collection<Object> getIncomingLetters(IDirectedGraph graph) {
		Set<Object> letters = new TreeSet<Object>();
		for (IEdge edge: getIncomingEdges(graph)) {
			letters.add(((DFAEdge)edge).letter()); 
		}
		return letters;
	}
	
}