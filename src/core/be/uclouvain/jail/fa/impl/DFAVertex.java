package be.uclouvain.jail.fa.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import net.chefbe.javautils.robust.exceptions.IllegalUsageException;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.adjacency.IEdge;
import be.uclouvain.jail.graph.adjacency.IVertex;
import be.uclouvain.jail.graph.deco.GraphConstraintViolationException;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.UserInfoCapable;

/**
 * Efficient vertex implementation for DFAs.
 * 
 * @author LAMBEAU Bernard
 */
public class DFAVertex extends UserInfoCapable implements IVertex {

	/** List of incoming edges. */
	protected List<IEdge> incomingEdges = new ArrayList<IEdge>();
	
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
	
	/* incoming edges COLLECTION PROPERTY SECTION ------------------------------------------------ */
	/** Returns the letter associated to an edge. */
	protected Object edgeLetter(IDirectedGraph graph, IEdge e) {
		IGraphFAInformer informer = (IGraphFAInformer) graph.adapt(IGraphFAInformer.class);
		if (informer == null) {
			throw new IllegalUsageException("Provided graph must be IGraphFAInformer adaptable.");
		}
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
		return outgoingEdges.values();
	}

	/** Adds an outgoing edge. */
	public void addOutgoingEdge(IDirectedGraph graph, IEdge e) {
		Object letter = edgeLetter(graph,e);
		if (outgoingEdges.containsKey(letter)) {
			throw new GraphConstraintViolationException(null,"DFA constraint violated.");
		}
		outgoingEdges.put(letter,e);
	}

	/** Removes an outgoing edge. */
	public void removeOutgoingEdge(IDirectedGraph graph, IEdge e) {
		Object letter = edgeLetter(graph,e);
		outgoingEdges.remove(letter);
	}

	/** Returns the outgoing edge labeled with a given letter. */
	public IEdge getOutgoingEdge(Object letter) {
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