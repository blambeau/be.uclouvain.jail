package be.uclouvain.jail.algo.induct.utils;

import be.uclouvain.jail.algo.induct.open.IMembershipQueryTester;
import be.uclouvain.jail.fa.FAStateKind;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.graph.IDirectedGraph;

/**
 * Query tester which uses a DFA to answer the question.
 * 
 * @author blambeau
 */
public class AutoQueryTester implements IMembershipQueryTester {

	/** DFA to use to answer the queries. */
	private IDFA dfa;
	
	/** Underlying graph. */
	private IDirectedGraph graph;
	
	/** Creates a tester instance. */
	public AutoQueryTester(IDFA dfa) {
		if (dfa == null) { throw new IllegalArgumentException("DFA cannot be null"); }
		this.dfa = dfa;
		this.graph = dfa.getGraph();
	}

	/** Accepts the query? */
	public boolean accept(MembershipQuery query) {
		boolean positive = query.isPositive();
		int size = query.size();
		
		// walk the dfa
		Object current = dfa.getInitialState();
		int found = 0;
		for (Object letter: query) {
			Object edge = dfa.getOutgoingEdge(current, letter);
			if (edge == null) {
				break;
			} else {
				current = graph.getEdgeTarget(edge); 
				found++;
			}
		}
		
		// check situations
		if (found==size) {
			// 1) all edges found, current is the DFA corresponding 
			//    state of the last query state
			if (positive) {
				// when query is positive, current.kind must be ACCEPTING
				return FAStateKind.ACCEPTING.equals(dfa.getStateKind(current));
			} else {
				// when query is negative, current.kind may not be an ACCEPTING
				// state
				return !FAStateKind.ACCEPTING.equals(dfa.getStateKind(current));
			}
		} else {
			// 2) not all edges found, current is the last state found
			if (positive) {
				// when query is positive, all edges should have been matched.
				// it must then be rejected !
				return false;
			} else {
				// when query is negative, it's ok if we are at one edge (DFA is
				// not complemented by default)
				return (found == size-1);
			}
		}
	}

}
