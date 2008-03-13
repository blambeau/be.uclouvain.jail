package be.uclouvain.jail.algo.induct.processor;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import be.uclouvain.jail.algo.induct.compatibility.ICompatibility;
import be.uclouvain.jail.algo.induct.internal.InductionAlgo;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.graph.IDirectedGraph;

/**
 * Listener that handles back propagation.
 * 
 * @author blambeau
 */
public class LabelBackPropagateProcessor implements IInductionProcessor {

	/** Label pair class. */
	static class Pair implements Comparable<Pair> {
		
		/** Two labels. */
		private Integer k, l;
		
		/** Creates a pair. */
		public Pair(Integer k, Integer l) {
			this.k = Math.min(k, l);
			this.l = Math.max(k, l);
		}

		/** Returns an hash code. */
		public int hashCode() {
			return 37*k + l;
		}
		
		/** Checks equality. */
		public boolean equals(Object o) {
			if (o instanceof Pair == false) { return false; }
			Pair other = (Pair) o;
			return k.equals(other.k) && l.equals(other.l);
		}

		/** Compare with another pair. */
		public int compareTo(Pair other) {
			if (k < other.k) { return -1; }
			if (k > other.k) { return 1; }
			if (l < other.l) { return -1; }
			if (l > other.l) { return 1; }
			return 0;
		}
		
		/** Returns a string representation. */
		public String toString() {
			return "(" + k + "," + l + ")";
		}
		
	}
	
	/** Algorithm. */
	private InductionAlgo algo;

	/** PTA. */
	private IDFA pta;
	
	/** PTA graph. */
	private IDirectedGraph ptag;
	
	/** Compatibility informer. */
	private ICompatibility compatibility;
	
	/** Class attribute. */
	private String classAttr;
	
	/** States by class. */
	private Map<Integer, Set<Object>> statesByClass;

	/** To-explore set (pairs of labels). */
	private TreeSet<Pair> toExplore;
	
	/** Explored set (pairs of labels). */
	private TreeSet<Pair> explored;
	
	/** Creates a processor instance. */
	public LabelBackPropagateProcessor(String classAttr) {
		this.classAttr = classAttr;
	}

	/** Returns all states by class. */
	private Set<Object> states(Integer clazz, boolean create) {
		Set<Object> states = statesByClass.get(clazz);
		if (states == null && create) {
			states = new HashSet<Object>();
			statesByClass.put(clazz, states);
		}
		return states;
	}
	
	/** Returns the class of a state. */
	private Integer classOf(Object state) {
		Object clazz = ptag.getVertexInfo(state).getAttribute(classAttr);
		assert (clazz instanceof Integer) : "Valid state class.";
		return (Integer) clazz;
	}
	
	/** Returns incoming edge of a state. */
	private Object inEdge(Object s) {
		Collection<Object> inEdges = ptag.getIncomingEdges(s);
		
		// initial state?
		if (inEdges.isEmpty()) { return null; }
		
		// only one incoming edge
		assert (inEdges.size() == 1) : "Valid PTA.";
			
		// return single incoming edge
		return inEdges.iterator().next();
	}
	
	/** Creates the letter/edges map. */
	private Map<Object,Set<Object>> createEdgeMap() {
		IDFA pta = algo.getPTA();

		// map to fill (letter -> set of edges)
		Map<Object,Set<Object>> edgesByLetter = new HashMap<Object,Set<Object>>();

		// take a look at each edge
		for (Object edge: pta.getGraph().getEdges()) {
			Object letter = pta.getEdgeLetter(edge);
			
			// find the set of edges or create it
			Set<Object> edges = edgesByLetter.get(letter);
			if (edges == null) {
				edges = new HashSet<Object>();
				edgesByLetter.put(letter, edges);
			}
			
			// add edge
			edges.add(edge);
		}
		
		return edgesByLetter;
	}
	
	/** Creates the letter/edges map. */
	private void initialize() {
		// create statesByClasses
		statesByClass = new HashMap<Integer,Set<Object>>();
		for (Object state: ptag.getVertices()) {
			Integer clazz = classOf(state);
			states(clazz,true).add(state);
		}
		
		// initialize data structures
		toExplore = new TreeSet<Pair>();
		explored = new TreeSet<Pair>();
		
		// for each letter
		Map<Object,Set<Object>> edgesByLetter = createEdgeMap();
		for (Object letter: edgesByLetter.keySet()) {
			Object[] edges = edgesByLetter.get(letter).toArray();
			
			// two by tow
			int size = edges.length;
			for (int i=0; i<size; i++) {
				for (int j=i+1; j<size; j++) {
					// take edges and target states
					Object iEdge = edges[i];
					Object jEdge = edges[j];
					Object iState = ptag.getEdgeTarget(iEdge);
					Object jState = ptag.getEdgeTarget(jEdge);
					
					// states are not compatible
					if (!compatibility.isCompatible(iState, jState)) {
						// mark the pair of labels as to explore
						Integer k = classOf(iState);
						Integer l = classOf(jState);
						toExplore.add(new Pair(k, l));
					}
				}
			}
			
		}
	}
	
	/** Installs initial sets of incompatibilities. */
	public void process(InductionAlgo algo) {
		this.algo = algo;
		this.compatibility = algo.getCompatibility();
		this.pta = algo.getPTA();
		this.ptag = pta.getGraph();

		// check that compatibility is extensible
		if (compatibility == null || !compatibility.isExtensible()) {
			throw new IllegalStateException("Compatibility must be extensible.");
		}

		initialize();
		while (!toExplore.isEmpty()) {
			//System.out.println("toExplore: " + toExplore);
			//System.out.println("explored: " + explored);
			
			// take a pair
			Pair pair = toExplore.first();
			
			//System.out.println("\nTaking " + pair + "----------------------------------");
			
			toExplore.remove(pair);
			explored.add(pair);
			
			explore(pair);
		}
	}
	
	/** Explores a pair. */
	private void explore(Pair pair) {
		Set<Object> kStates = states(pair.k, false);
		Set<Object> lStates = states(pair.l, false);
		assert (kStates != null && lStates != null) : "States exist.";
		for (Object kState: kStates) {
			for (Object lState: lStates) {
				backPropagate(kState, lState);
			}
		}
	}

	/** Mark two states as incompatible. */
	private void backPropagate(Object s1, Object s2) {
		if (compatibility.isCompatible(s1, s2)) {
			compatibility.markAsIncompatible(s1,s2);
		}
		
		// get incoming edges
		Object iEdge = inEdge(s1);
		Object jEdge = inEdge(s2);
		
		// initial state?
		if (iEdge == null || jEdge == null) { return; }

		// take letters
		Object iLetter = pta.getEdgeLetter(iEdge);
		Object jLetter = pta.getEdgeLetter(jEdge);
		
		// propagate?
		if (!iLetter.equals(jLetter)) {
			return;
		}
		
		// take source states
		Object iSource = ptag.getEdgeSource(iEdge);
		Object jSource = ptag.getEdgeSource(jEdge);
		
		Integer iClass = classOf(iSource);
		Integer jClass = classOf(jSource);
		Pair pair = new Pair(iClass, jClass);
		if (!explored.contains(pair) && !toExplore.contains(pair)) {
			toExplore.add(pair);
		}
	}
	
}
