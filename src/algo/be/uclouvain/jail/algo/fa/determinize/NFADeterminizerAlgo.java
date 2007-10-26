package be.uclouvain.jail.algo.fa.determinize;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import be.uclouvain.jail.fa.INFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.uinfo.IUserInfo;

/**
 * <p>Standard NDA determinization algorithm.</p>
 * 
 * <p>This algorithm computes a DFA which is equivalent to a source NFA.</p>
 * 
 * <p>Pseudo code viewed by the result abstraction can be described as below.</p> 
 * <pre>
 *   // TargetDef below is a definition of a target state, i.e. a set of source states. 
 * 
 *   // initialization
 *   TargetDef initial = [...];
 *   toExplore.putOne(initial);
 *   <b>result.createTargetState(initial);</b>
 *   
 *   // computation ... we will create transitions, and target states on the fly
 *   while !toExplore.isEmpty()
 *     // transition source
 *     TargetDef source = toExplore.removeOne();
 *     
 *     // iterate outgoing transitions of the source states in the target definition
 *     for each outgoing letter l in delta.letters(source)
 *       // transition target 
 *       TargetDef target = delta.compute(targets,l);
 *       
 *       // empty target (not used letter), do nothing
 *       if targets is empty continue; 
 *       
 *       // create target state on the fly is not already done 
 *       // <b>the result is ensured that target states always exist when creating transitions.</b> 
 *       if !alreadyCreated(targets)
 *         <b>result.createTargetState(targets)</b>
 *       fi
 *       
 *       // create the transition !
 *       <b>result.createTargetTransition(current,l,targets);</b>
 *       
 *     end for
 *   end for
 * </pre>
 * 
 * TODO: toExplore implementation may be quite expensive, no ?  
 * 
 * @author LAMBEAU Bernard
 */
public class NFADeterminizerAlgo {

	/* ---------------------------------------------------------------------------------------------- Fields */
	/** Target states to explore, as a lookup table (def -> identifier). */
	private Map<Set<Object>, Object> toExplore;

	/** Explored states, as a lookup table (def -> identifier). */
	private Map<Set<Object>, Object> explored;

	/** Result abstraction. */
	private INFADeterminizerResult result;
	
	/** Non deterministic automaton to determinize. */
	private INFA nfa;
	
	/* ---------------------------------------------------------------------------------------------- Construction */
	/** <p>Creates an algorithm instance.</p> */
	public NFADeterminizerAlgo() {
	}

	/* ---------------------------------------------------------------------------------------------- Initialization */
	/** Algorithm initialization. */
	protected void init() {
		toExplore = new HashMap<Set<Object>, Object>();
		explored = new HashMap<Set<Object>, Object>();
	}

	/* ---------------------------------------------------------------------------------------------- Algorithm methods */
	/** Returns true when there is target state defnitions to explore. */
	protected boolean hasNext() {
		return !toExplore.isEmpty();
	}

	/**
	 * Returns a target state definition to explore.
	 * 
	 * <p>This method removes a target state definition of the toExplore list and mark it 
	 * as explored.</p>
	 */
	private Set<Object> getOne() {
		/* get one target definition */
		Set<Object> sources = toExplore.keySet().iterator().next();
		Object identifier = toExplore.remove(sources);

		/* mark as explored */
		explored.put(sources, identifier);

		return sources;
	}

	/** Checks if a target state definition has been explored. */
	private boolean isAlreadyFound(Set<Object> def) {
		return explored.containsKey(def) || toExplore.containsKey(def);
	}

	/** 
	 * Creates a new target state definition.
	 * 
	 * @param def the definition of the target states (a set of source states).
	 * @return the identifier of the target state created by the result.
	 */
	private Object createOne(Set<Object> def) {
		IDirectedGraph g = nfa.getGraph();
		Set<IUserInfo> infos = new HashSet<IUserInfo>();
		for (Object state: def) {
			infos.add(g.getVertexInfo(state));
		}
		
		Object identifier = result.createTargetState(infos);
		toExplore.put(def, identifier);
		return identifier;
	}
	
	/** 
	 * Computes the delta function from some source states. 
	 * 
	 * <p>Returns a map (letter,[edges,targets]).</p>
	 */
	@SuppressWarnings("unchecked")
	private Map<Object,Set[]> delta(Set<Object> sources) {
		// extract graph
		IDirectedGraph g = nfa.getGraph();
		
		// create delta map
		Comparator c = nfa.getAlphabet();
		Map<Object,Set[]> delta = new TreeMap<Object,Set[]>(c);
		
		// for each source state
		for (Object source: sources) {
			// for each outgoing edge of this source
			for (Object edge: g.getOutgoingEdges(source)) {
				Object letter = nfa.getEdgeLetter(edge);
				
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
				outEdgesAndTargets[0].add(g.getEdgeInfo(edge));
				outEdgesAndTargets[1].add(g.getEdgeTarget(edge));
			}
		}
		
		return delta;
	}

	/** Main method of the algo */
	@SuppressWarnings("unchecked")
	protected void main() {
		// initialize algorithm
		init();
		
		/* algo started event */
		result.started(nfa);

		/* create one for initial states of the NFA */
		Set<Object> initStates = new HashSet<Object>();
		for (Object init: nfa.getInitialStates()) {
			initStates.add(init);
		}
		createOne(initStates);

		/* main loop ... while target state created and not explored */
		while (hasNext()) {

			/* get the definition of the target state (a set of source states) */
			Set<Object> sources = getOne();

			/* iterate interresting alphabet letters */
			Map<Object,Set[]> delta = delta(sources);
			for (Object letter : delta.keySet()) {
				
				// get delta function
				Set[] outEdgesAndTargets = delta.get(letter);  
				
				// find reachable edges and states
				Set<IUserInfo> outEdges = outEdgesAndTargets[0];
				Set<Object> targets = outEdgesAndTargets[1];

				// create state
				Object targetIdentifier = null;
				if (!isAlreadyFound(targets)) {
					targetIdentifier = createOne(targets);
				} else {
					/* explored ? */
					targetIdentifier = explored.get(targets);
					/* no, to be explored ! */
					if (targetIdentifier == null) {
						targetIdentifier = toExplore.get(targets);
					}
				}

				/* create transitions */
				result.createTargetTransitions(explored.get(sources), targetIdentifier, outEdges);
			}
		}

		result.ended();
	}

	/** Executes the determinization. */
	public void execute(INFADeterminizerInput input, INFADeterminizerResult result) {
		this.nfa = input.getNFA();
		this.result = result;
		main();
	}
	
}
