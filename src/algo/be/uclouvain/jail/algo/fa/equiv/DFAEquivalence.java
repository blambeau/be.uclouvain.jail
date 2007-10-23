package be.uclouvain.jail.algo.fa.equiv;

import java.util.HashMap;
import java.util.Map;

import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.orders.ITotalOrder;

/** Checks equivalence of two DFAs. */
public class DFAEquivalence {

	/** Thrown to detect non equivalence. */
	@SuppressWarnings("serial")
	static class NonEquivException extends Exception{};
	
	/** DFA to test. */
	private IDFA dfa;
	
	/** Reference DFA. */
	private IDFA reference;
	
	/** Equivalence map. */
	private Map<Object,Object> equivalence = new HashMap<Object,Object>();
	
	/** Tests equivalence. */
	protected boolean testGraphsOK() {
		IDirectedGraph g1 = dfa.getGraph();
		IDirectedGraph g2 = reference.getGraph();
		
		// same number of vertices?
		ITotalOrder order1 = g1.getVerticesTotalOrder();
		ITotalOrder order2 = g2.getVerticesTotalOrder();
		if (order1.size() != order2.size()) {
			return false;
		}

		// same number of edges?
		order1 = g1.getEdgesTotalOrder();
		order2 = g2.getEdgesTotalOrder();
		if (order1.size() != order2.size()) {
			return false;
		}
		
		return true;
	}
	
	/** Marks two states as being equivalent. */
	protected boolean mark(Object s1, Object s2) throws NonEquivException {
		if (equivalence.containsKey(s1)) {
			if (!s2.equals(equivalence.get(s1))) {
				throw new NonEquivException();
			} else {
				return true;
			}
		} else {
			equivalence.put(s1, s2);
			return false;
		}
	}
	
	/** Tests state equivalence. */
	protected void testStateEquiv(Object s1, Object s2) throws NonEquivException {
		boolean alreadyKnown = mark(s1,s2);
		if (alreadyKnown) { return; }

		// same attributes?
		if (dfa.isInitial(s1) != reference.isInitial(s2)) {
			throw new NonEquivException();
		}
		if (dfa.isAccepting(s1) != reference.isAccepting(s2)) {
			throw new NonEquivException();
		}
		if (dfa.isError(s1) != reference.isError(s2)) {
			throw new NonEquivException();
		}

		// same number of outgoing and incoming letters?
		if (dfa.getOutgoingLetters(s1).size() != reference.getOutgoingLetters(s2).size()) {
			throw new NonEquivException();
		}
		
		// check target states for each letter
		for (Object letter: dfa.getOutgoingLetters(s1)) {
			
			// retrieve edges
			Object s1e = dfa.getOutgoingEdge(s1, letter);
			Object s2e = reference.getOutgoingEdge(s2, letter);
			
			// no such letter, not equivalent
			if (s2e == null) {
				throw new NonEquivException();
			}
			
			// retrieve edge targets
			Object s1t = dfa.getGraph().getEdgeTarget(s1e);
			Object s2t = reference.getGraph().getEdgeTarget(s2e);
			
			// recurse
			testStateEquiv(s1t,s2t);
		}
	}
	
	/** Test DFA equivalence. */
	protected void testDFAEquiv() throws NonEquivException {
		testStateEquiv(dfa.initialState(),reference.initialState());
	}
	
	/** Tests equivalence. */
	protected boolean execute() {
		if (!testGraphsOK()) {
			return false;
		}
		try {
			testDFAEquiv();
			return true;
		} catch (NonEquivException ex) {
			return false;
		}
	}
	
	/** Checks if dfa is equivalent to the reference DFA. */
	public static boolean isEquivalent(IDFA dfa, IDFA reference) {
		DFAEquivalence equiv = new DFAEquivalence();
		equiv.dfa = dfa;
		equiv.reference = reference;
		return equiv.execute();
	}
	
}
