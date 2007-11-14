package be.uclouvain.jail.algo.fa.tmoves;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.uinfo.IUserInfo;

/**
 * <p>Standard e-moves remover algorithm. This algorithm computes the equivalent NFA of a source
 * FA without any epsilon transition.</p>
 * 
 * <p>Let source denote the input NFA, this class works with two abstractions : a epsilon delta functions 
 * which respects the {@link EpsilonRemoverAlgorithmDeltaFunctions} contract and a result abstraction which 
 * respects the {@link ITauRemoverResult}. The first one informs about source transitions usage in 
 * case of epsilon transitions, while the second one abstract the construction of the resulting NFA.</p>
 * 
 * <p>Pseudo code viewed by the result abstraction can be described as follows :</p>
 * <pre>
 *   // initialization
 *   for each state s of source
 *     <b>result.createTargetState(s);</b>
 *   end for
 *   
 *   // computation
 *   for each state s of source
 *     for each letter l of the source alphabet
 *        ts = [...] // compute where we go from s through l, a collection of states
 *        <b>result.createTargetTransitions(s,l,ts);</b>
 *     end for
 *   end for
 * </pre>
 * 
 * @author LAMBEAU Bernard
 */
public class TauRemoverAlgo {

	/* ---------------------------------------------------------------------------------------------- Fields */
	/** Source DFA from which the tau-transitions must be removed. */
	private IDFA dfa;

	/** Underlying graph. */
	private IDirectedGraph graph;
	
	/** Epsilon informer. */
	private ITauInformer<Object> epsilon;

	/** Result abstraction. */
	private ITauRemoverResult result;

	/* ---------------------------------------------------------------------------------------------- Construction */
	/** Creates an algorithm instance. */
	public TauRemoverAlgo() {
	}

	/** Computes the closure of a single state. */
	private void eclosure(Object state, Set<Object> set) {
		set.add(state);
		
		// look at outgoing edges
		for (Object outEdge: graph.getOutgoingEdges(state)) {
			// bypass non epsilon letters
			Object letter = dfa.getEdgeLetter(outEdge);
			if (!epsilon.isEpsilon(letter)) {
				continue;
			}
			
			// get edge target
			Object target = graph.getEdgeTarget(outEdge);
			
			// add to the set of reachable states and recurse if 
			// required
			if (!set.contains(target)) {
				set.add(target);
				
				// recurse on that state
				eclosure(target,set);
			}
		}
	}
	
	/** Computes the closure of a set of states. */
	private Set<Object> eclosure(Set<Object> states) {
		Set<Object> closure = new HashSet<Object>(); 
		for (Object state: states) {
			if (!closure.contains(state)) {
				eclosure(state,closure);
			}
		}
		return closure;
	}
	
	/** Computes the closure of a single state. */
	private Set<Object> eclosure(Object state) {
		Set<Object> closure = new HashSet<Object>(); 
		eclosure(state,closure);
		return closure;
	}
	
	/** 
	 * Computes the delta function from some source states. 
	 * 
	 * <p>Returns a map (letter,[edges,targets]).</p>
	 */
	@SuppressWarnings("unchecked")
	private Map<Object,Set[]> delta(Set<Object> sources) {
		// create delta map
		Comparator c = dfa.getAlphabet();
		Map<Object,Set[]> delta = new TreeMap<Object,Set[]>(c);
		
		// for each source state
		for (Object source: sources) {
			// for each outgoing edge of this source
			for (Object edge: graph.getOutgoingEdges(source)) {
				Object letter = dfa.getEdgeLetter(edge);
				
				// get known target states or create it
				Set[] outEdgesAndTargets = delta.get(letter);
				if (outEdgesAndTargets == null) {
					outEdgesAndTargets = new Set[]{
						new HashSet<IUserInfo>(),
						new HashSet<Object>()
					};
					delta.put(letter, outEdgesAndTargets);
				}
				
				// append new target states
				outEdgesAndTargets[0].add(graph.getEdgeInfo(edge));
				outEdgesAndTargets[1].add(graph.getEdgeTarget(edge));
			}
		}
		
		return delta;
	}
	
	/* ---------------------------------------------------------------------------------------------- Main */
	/**
	 * Executes the e-moves algorithm on a given source and result.
	 * 
	 * @param source a NFA with epsilon moves transitions.
	 * @param algorithmResult a result to construct.
	 */
	@SuppressWarnings("unchecked")
	private void main() {
		/* started event to the result */
		result.started(dfa);

		// map of created states
		Map<Object,Object> createdStates = new HashMap<Object,Object>();
		
		/* creates each target state */
		for (Object s : graph.getVertices()) {
			IUserInfo info = graph.getVertexInfo(s);
			Object target = result.createTargetState(info);
			createdStates.put(s, target);
		}

		/* for each state */
		for (Object sourceState : graph.getVertices()) {

			// compute closure of the state
			Set<Object> sources = eclosure(sourceState);

			// compute delta of the source states
			Map<Object,Set[]> delta = delta(sources);
			
			/* iterate interresting alphabet letters */
			for (Object letter : delta.keySet()) {
				
				/* bypass epsilon */
				if (epsilon.isEpsilon(letter)) {
					continue;
				}

				// find reachable edges and states
				Set[] outEdgesAndTargets = delta.get(letter);  
				Set<IUserInfo> outEdges = outEdgesAndTargets[0];
				Set<Object> targets = eclosure(outEdgesAndTargets[1]);
				
				// get equivalent states in target
				Set<Object> cTargets = new HashSet<Object>();
				for (Object target: targets) {
					cTargets.add(createdStates.get(target));
				}
				
				/* create transitions */
				result.createTargetTransitions(createdStates.get(sourceState), cTargets, outEdges);
			}
		}

		result.ended();
	}

	/** Executes the algorithm. */
	@SuppressWarnings("unchecked")
	public void execute(ITauRemoverInput input, ITauRemoverResult result) {
		this.dfa = input.getDFA();
		this.graph = dfa.getGraph();
		this.epsilon = input.getTauInformer();
		this.result = result;
		main();
	}
	
}
