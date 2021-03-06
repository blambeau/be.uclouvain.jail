package be.uclouvain.jail.algo.fa.equiv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import be.uclouvain.jail.algo.fa.equiv.IDFAEquivOutput.CounterExampleKind;
import be.uclouvain.jail.fa.FAStateKind;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.IFATrace;
import be.uclouvain.jail.fa.utils.DefaultFATrace;
import be.uclouvain.jail.graph.GraphConstraintViolationException;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.IDirectedGraphPath;
import be.uclouvain.jail.graph.utils.DefaultDirectedGraphPath;
import be.uclouvain.jail.graph.utils.ITotalOrder;
import be.uclouvain.jail.uinfo.IUserInfo;

/** 
 * Checks equivalence of two (minimal) DFAs. 
 *
 * <p>This algorithm implements a test for checking structural equivalence of two 
 * (minimal) DFAs. By structural equivalence we mean the equivalence of the regular 
 * languages represented by the two DFAs. In other words, this algorithm (using tries 
 * to compute a one-to-one mapping between states of the DFAs in terms of accepted 
 * language (infinite) suffixes only.</p>
 * 
 * <p>Please note however that this algorithm makes the assumption that the provided DFAs
 * are minimal. In other words, if at least one of the provided DFAs is non minimal, this
 * algorithm will probably fail to detect the language equivalence even if it's the case.</p> 
 * 
 * <p>This algorithm may be extended or adapted for special needs by providing a 
 * user-defined {@link IDFAEquivInformer}. This could be interresting for example 
 * if {@link IUserInfo}S attached to edges and states must be checked against 
 * user-defined attributes (which can be interpreted as testing the equality of the 
 * automata instead of the language equivalence).</p>  
 */
public class DFAEquivAlgo {

	/** Thrown to detect non equivalence. */
	@SuppressWarnings("serial")
	static class NonEquivException extends Exception{
		
		/** Kind of counter example. */
		public CounterExampleKind kind;
		
		/** Counter example trace. */
		public IFATrace trace;

		/** Creates a non-equivalence exception. */
		public NonEquivException(CounterExampleKind kind, IFATrace trace) {
			this.kind = kind;
			this.trace = trace;
		}
		
		
	};
	
	/** Input of the algorithm. */
	private IDFAEquivInput input;
	
	/** Output of the algorithm. */
	private IDFAEquivOutput output;
	
	/** DFA to test. */
	private IDFA tested;
	
	/** Reference DFA. */
	private IDFA reference;

	/** Equivalence map. */
	private Map<Object,Object> equivalence = new HashMap<Object,Object>();
	
	/** Stack for counter-example construction. */
	private LinkedList<Object> stack;
	
	/** Pushes an edge on the trace stack. */
	private void pushTrace(Object edge) {
		if (input.isCounterExampleEnabled()) {
			stack.addLast(edge);
		}
	}

	/** Pops the last edge from the trace stack. */
	private void popTrace() {
		if (input.isCounterExampleEnabled()) {
			stack.removeLast();
		}
	}
	
	/** Returns current trace. */
	private IFATrace getTestedTrace() {
		if (input.isCounterExampleEnabled()) {
			IDirectedGraphPath path = new DefaultDirectedGraphPath(tested.getGraph(),stack);
			return new DefaultFATrace(tested,path);
		} else {
			return null;
		}
	}
	
	/** Returns a reference equivalent trace extended with one rejected edge. */
	private IFATrace getReferenceTrace(Object rejEdge) {
		if (!input.isCounterExampleEnabled()) {
			return null;
		}
		
		// transform tested trace to reference trace
		List<Object> edges = new ArrayList<Object>(stack.size()+1);
		Object current = reference.getInitialState();
		for (Object edge: stack) {
			Object letter = tested.getEdgeLetter(edge);
			Object equiv = reference.getOutgoingEdge(current, letter);
			edges.add(equiv);
			current = reference.getGraph().getEdgeTarget(equiv);
		}
		
		// add rejected edge
		if (rejEdge != null) {
			edges.add(rejEdge);
		}
		
		// create and return trace
		IDirectedGraphPath path = new DefaultDirectedGraphPath(reference.getGraph(),edges);
		return new DefaultFATrace(reference,path);
	}
	
	/** Factors a NonEquivException for STATE kind. */
	private NonEquivException stateNonEquivException(Object s1, Object s2) {
		return new NonEquivException(CounterExampleKind.STATE,getTestedTrace());
	}
	
	/** Factors a NonEquivException for EDGE kind. */
	private NonEquivException edgeNonEquivException(Object e1, Object s2) {
		pushTrace(e1);
		return new NonEquivException(CounterExampleKind.EDGE,getTestedTrace());
	}
	
	/** Factors a NonEquivException for EDGE kind. */
	private NonEquivException rejectedNonEquivException(Object s1, Object s2) {
		if (!input.isCounterExampleEnabled()) {
			return new NonEquivException(null,null);
		}
		
		// find unexistant edge
		for (Object letter: reference.getOutgoingLetters(s2)) {
			if (tested.getOutgoingEdge(s1, letter) != null) {
				Object rejEdge = reference.getOutgoingEdge(s2, letter);
				return new NonEquivException(CounterExampleKind.REJECTED,getReferenceTrace(rejEdge));
			}
		}
		
		throw new AssertionError("Reference contains at least one rejected letter");
	}
	
	/** Factors a NonEquivException for ACCEPTED kind. */
	private NonEquivException acceptedNonEquivException() {
		return new NonEquivException(CounterExampleKind.ACCEPTED,getTestedTrace());
	}
	
	/** Checks that graph are basically equivalent (count number of vertices 
	 * and edges). */
	protected boolean testGraphsOK() {
		IDirectedGraph g1 = tested.getGraph();
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
	
	/** Tests equivalence of two states according to their flags. */
	protected void testStateKindEquiv(Object s1, Object s2) throws NonEquivException {
		if (tested.isInitial(s1) != reference.isInitial(s2)) {
			throw new GraphConstraintViolationException(null,"Not DFAs, multiple initial states");
		}

		// get state kinds
		FAStateKind s1Kind = tested.getStateKind(s1);
		FAStateKind s2Kind = reference.getStateKind(s2);
		
		if (s1Kind.equals(s2Kind)) {
			// equals? OK
			return;
		} else {
			// not equal? check path equivalent
			boolean s1MayStop = FAStateKind.ACCEPTING.equals(s1Kind);
			boolean s2MayStop = FAStateKind.ACCEPTING.equals(s2Kind);
			if ((s1MayStop || s2MayStop) && !(s1MayStop && s2MayStop)) {
				// one accepting but not both
				throw new NonEquivException(CounterExampleKind.UNEXPLAINED,getReferenceTrace(null));
			}
			
			boolean s1IsAvoid = FAStateKind.AVOID.equals(s1Kind);
			boolean s2IsAvoid = FAStateKind.AVOID.equals(s2Kind);
			if ((s1IsAvoid || s2IsAvoid) && !(s1IsAvoid && s2IsAvoid)) {
				// one avoid, but not both
				throw new NonEquivException(CounterExampleKind.UNEXPLAINED,getReferenceTrace(null));
			}
		}
	}
	
	/** Tests equivalence of two states according to their flags 
	 * and attributes. */
	protected void testStateInfoEquiv(Object s1, Object s2) throws NonEquivException {
		// same flags?
		testStateKindEquiv(s1,s2);

		// check IUserInfo equivalence if required
		IDFAEquivInformer informer = input.getInformer();
		if (informer != null) {
			IUserInfo s1Info = tested.getGraph().getVertexInfo(s1);
			IUserInfo s2Info = reference.getGraph().getVertexInfo(s2);
			if (!informer.isStateEquivalent(s1Info, s2Info)) {
				throw stateNonEquivException(s1,s2);
			}
		}
	}
	
	/** Tests equivalence of two edges according to their attributes. */
	protected void testEdgeInfoEquiv(Object e1, Object e2) throws NonEquivException {
		// check IUserInfo equivalence if required
		IDFAEquivInformer informer = input.getInformer();
		if (informer != null) {
			IUserInfo e1Info = tested.getGraph().getEdgeInfo(e1);
			IUserInfo e2Info = reference.getGraph().getEdgeInfo(e2);
			if (!informer.isEdgeEquivalent(e1Info, e2Info)) {
				throw edgeNonEquivException(e1,e2);
			}
		}
	}
	
	/** Marks two states as being equivalent. */
	protected boolean mark(Object s1, Object s2) throws NonEquivException {
		if (equivalence.containsKey(s1)) {
			// s1 has already been visited
			if (!s2.equals(equivalence.get(s1))) {
				// and is not equivalent to s2 because equivalent
				// to another state
				throw new NonEquivException(CounterExampleKind.UNEXPLAINED,null);
			} else {
				// and is equivalent to s2 !
				return true;
			}
		} else {
			// s1 has not been previously visited
			// check states flags and attributes equivalence
			testStateInfoEquiv(s1,s2);
			
			// mark equivalence
			equivalence.put(s1, s2);
			return false;
		}
	}
	
	/** Recursively checks state equivalence. */
	protected void testStateEquiv(Object s1, Object s2) throws NonEquivException {
		// mark states as equivalent, ... 
		// ... returning if already known (end of recursion)
		boolean alreadyKnown = mark(s1,s2);
		if (alreadyKnown) { return; }

		// same number of outgoing and incoming letters?
		int s1OutCount = tested.getOutgoingLetters(s1).size();
		int s2OutCount = reference.getOutgoingLetters(s2).size();
		if (s1OutCount != s2OutCount && !input.isCounterExampleEnabled()) {
			throw new NonEquivException(null,null);
		}
		
		// check target states for each letter
		for (Object letter: tested.getOutgoingLetters(s1)) {
			
			// retrieve edges
			Object s1e = tested.getOutgoingEdge(s1, letter);
			Object s2e = reference.getOutgoingEdge(s2, letter);
			
			// push on stack
			pushTrace(s1e);
			
			if (s2e == null) {
				// no such letter, not equivalent
				throw acceptedNonEquivException();
			} else {
				// letter ok, edge are equivalent?
				testEdgeInfoEquiv(s1e,s2e);
			}
			
			// retrieve edge targets
			Object s1t = tested.getGraph().getEdgeTarget(s1e);
			Object s2t = reference.getGraph().getEdgeTarget(s2e);
			
			// recurse
			testStateEquiv(s1t,s2t);
			
			// pop from stack
			popTrace();
		}
		
		// create counter example if not same state due 
		// to the number of out edges
		if (s1OutCount != s2OutCount) {
			// all letters of tested found, find the converse difference
			throw rejectedNonEquivException(s1,s2);
		}
	}
	
	/** Test DFA equivalence. */
	protected void testDFAEquiv() throws NonEquivException {
		// launch recursion on initial states
		Object s1 = tested.getInitialState();
		Object s2 = reference.getInitialState();
		testStateEquiv(s1,s2);
	}
	
	/** Tests equivalence. */
	protected void execute(IDFAEquivInput input, IDFAEquivOutput output) {
		this.input = input;
		this.output = output;
		this.tested = input.getTested();
		this.reference = input.getReference();
		
		// create stack if counter example required
		if (input.isCounterExampleEnabled()) {
			this.stack = new LinkedList<Object>();
		}
		
		// check vertices and edges count
		if (!input.isCounterExampleEnabled() && !testGraphsOK()) { 
			this.output.counterExampleFound(null, null);
		}
		
		// check real DFA equivalence
		try {
			testDFAEquiv();
			this.output.equivProved();
		} catch (NonEquivException ex) {
			this.output.counterExampleFound(ex.kind, ex.trace);
		}
	}
	
}
