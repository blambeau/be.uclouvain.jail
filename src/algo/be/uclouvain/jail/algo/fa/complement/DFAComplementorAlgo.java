package be.uclouvain.jail.algo.fa.complement;

import java.util.Set;
import java.util.TreeSet;

import net.chefbe.javautils.collections.arrays.ArrayUtils;
import be.uclouvain.jail.fa.IAlphabet;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.graph.IDirectedGraph;

/**
 * Complements a DFA by adding new transitions on missing alphabet 
 * letters.
 * 
 * @author blambeau
 */
public class DFAComplementorAlgo {

	/** Executes the algorithm. */
	public void execute(IDFAComplementorInput input, IDFAComplementorResult result) {
		IDFA dfa = input.getDFA();
		IAlphabet<Object> alphabet = dfa.getAlphabet();
		IDirectedGraph graph = dfa.getGraph();
		
		result.started(input);
		
		// copy each state with outgoing letters
		for (Object state: graph.getVertices()) {
			Set<Object> letters = new TreeSet<Object>(alphabet);
			
			// copy outgoing edges
			for (Object edge: graph.getOutgoingEdges(state)) {
				Object letter = dfa.getEdgeLetter(edge);
				result.copyEdge(edge);
				letters.add(letter);
			}
			
			/*
			System.out.println("On state " + state + " " + letters + " " + 
					ArrayUtils.toString(alphabet.getLetters().getTotalOrder(),","));
			*/
			// add missing letters
			Set<Object> missing = new TreeSet<Object>();
			for (Object letter: alphabet) {
				if (!letters.contains(letter)) {
					missing.add(letter);
				}
			}
			if (!missing.isEmpty()) {
				result.onMissing(state,missing);
			}
		}
		
		result.ended();
	}

}
