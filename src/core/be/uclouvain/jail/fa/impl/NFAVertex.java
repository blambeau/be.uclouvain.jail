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
 * <p>Efficient vertex implementation for DFAs.</p>
 * 
 * @author LAMBEAU Bernard
 */
public class NFAVertex extends UserInfoCapable implements IVertex {

	/** List of incoming edges. */
	protected List<IEdge> incomingEdges = new ArrayList<IEdge>();
	
	/** The adjacent list for outgoing edges. */
	protected Map<Object,List<IEdge>> outgoingEdges = new TreeMap<Object,List<IEdge>>();

	/* CONSTRUCTORS SECTION ---------------------------------------------------------------------- */
	/** Empty constructor. Installs a MapUserInfo. */
	public NFAVertex() {
		this(null);
	}

	/** Constructor with specific user info. */
	public NFAVertex(IUserInfo info) {
		super(info);
	}
	
	/* incoming edges COLLECTION PROPERTY SECTION ------------------------------------------------ */
	/** Returns the letter associated to an edge. */
	protected Object edgeLetter(IDirectedGraph graph, IEdge e) {
		IGraphFAInformer informer = (IGraphFAInformer) graph.adapt(IGraphFAInformer.class);
		return informer.edgeLetter(e.getUserInfo());
	}
	
	/** Returns incoming edges of the vertex. */
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

	/** Returns outgoing edges. */
	public Collection<IEdge> getOutgoingEdges(IDirectedGraph graph) {
		List<IEdge> edges = new ArrayList<IEdge>();
		for (List<IEdge> oEdges: outgoingEdges.values()) {
			edges.addAll(oEdges);
		}
		return edges;
	}

	/** Adds an outgoing edge. */
	public void addOutgoingEdge(IDirectedGraph graph, IEdge e) {
		Object letter = edgeLetter(graph,e);
		List<IEdge> edges = outgoingEdges.get(letter);
		if (edges == null) {
			edges = new ArrayList<IEdge>();
			outgoingEdges.put(letter, edges);
		}
		edges.add(e);
	}

	/** Removes an outgoing edge. */
	public void removeOutgoingEdge(IDirectedGraph graph, IEdge e) {
		Object letter = edgeLetter(graph,e);
		List<IEdge> edges = outgoingEdges.get(letter);
		if (edges != null) {
			edges.remove(e);
		}
	}

	/** Returns the outgoing edge labeled with a given letter. */
	public Collection<IEdge> getOutgoingEdges(Object letter) {
		return outgoingEdges.get(letter);
	}

	/** Returns outgoing letters of this state. */
	public Collection<Object> getOutgoingLetters(IDirectedGraph graph) {
		return outgoingEdges.keySet();
	}
	
	/** Returns outgoing letters of this state. */
	public Collection<Object> getIncomingLetters(IDirectedGraph graph) {
		Set<Object> letters = new TreeSet<Object>();
		for (IEdge edge: getIncomingEdges(graph)) {
			letters.add(edgeLetter(graph,edge)); 
		}
		return letters;
	}
	
}