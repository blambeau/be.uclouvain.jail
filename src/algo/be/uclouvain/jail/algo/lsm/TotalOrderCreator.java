package be.uclouvain.jail.algo.lsm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.utils.CopyTotalOrder;
import be.uclouvain.jail.graph.utils.ITotalOrder;

/**
 * Computes the natural total order of DFA states.
 * 
 * @author blambeau
 */
public class TotalOrderCreator {

	/** DFA under ordering compute. */
	private IDFA dfa;
	
	/** Graph. */
	private IDirectedGraph graph;
	
	/** States in order. */
	private List<Object> states;
	
	/** Affected states. */
	private Set<Object> affected;
	
	/** Affect a state in the list. */
	private void affect(Object state) {
		if (!affected.contains(state)) {
			states.add(state);
			affected.add(state);
		}
	}
	
	/** Computes the total order. */
	public ITotalOrder<Object> compute(IDFA dfa) {
		this.dfa = dfa;
		this.graph = dfa.getGraph();
		this.states = new ArrayList<Object>(graph.getVerticesTotalOrder().size());
		this.affected = new HashSet<Object>();
		
		// add init at start of the list
		Object init = dfa.getInitialState();
		affect(init);
		
		// parse states in order
		int index = 0;
		while (states.size() > index) {
			Object current = states.get(index);
			recurse(current);
			index++;
		}
		
		return new CopyTotalOrder<Object>(states);
	}

	/** Recurse on a state. */
	private void recurse(Object state) {
		// reachable states by letter, in natural order
		TreeMap<Object,Object> map = new TreeMap<Object,Object>(dfa.getAlphabet());
		for (Object edge: dfa.getGraph().getOutgoingEdges(state)) {
			Object letter = dfa.getEdgeLetter(edge);
			map.put(letter, graph.getEdgeTarget(edge));
		}
		
		// affect each
		for (Map.Entry<Object,Object> entry: map.entrySet()) {
			Object next = entry.getValue();
			affect(next);
		}
	}
	
}
