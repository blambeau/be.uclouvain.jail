package be.uclouvain.jail.algo.induct.processor;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import be.uclouvain.jail.algo.induct.compatibility.ICompatibility;
import be.uclouvain.jail.algo.induct.internal.InductionAlgo;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.graph.IDirectedGraph;

/**
 * Listener that handles back propagation.
 * 
 * @author blambeau
 */
public class BackPropagateProcessor implements IInductionProcessor {

	/** Algorithm. */
	private InductionAlgo algo;

	/** PTA. */
	private IDFA pta;
	
	/** PTA graph. */
	private IDirectedGraph ptag;
	
	/** Compatibility informer. */
	private ICompatibility compatibility;
	
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
		
		// create edgesByLetter map
		Map<Object,Set<Object>> edgesByLetter = createEdgeMap();
		
		// for each letter
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
						// get source states
						Object iSource = ptag.getEdgeSource(iEdge);
						Object jSource = ptag.getEdgeSource(jEdge);
						
						// if they are compatible, they are to be explored
						if (compatibility.isCompatible(iSource, jSource)) {
							backPropagate(iSource,jSource);
						}
					}
				}
			}
			
		}
	}

	/** Debugs index of a pair. */
	@SuppressWarnings("unused")
	private String debugPair(Object s, Object t) {
		return "(" + ptag.getVerticesTotalOrder().indexOf(s) + "," + ptag.getVerticesTotalOrder().indexOf(t) + ")";
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
	
	/** Mark two states as incompatible. */
	private void backPropagate(Object s1, Object s2) {
		compatibility.markAsIncompatible(s1,s2);
		
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
		
		// no need to go far if not compatible 
		// (already taken into account in main loop)
		if (compatibility.isCompatible(iSource, jSource)) {
			backPropagate(iSource,jSource);
		}
	}
	
}
