package be.uclouvain.jail.algo.induct.utils;

import java.util.List;
import java.util.Stack;

import be.uclouvain.jail.algo.induct.internal.PTAEdge;
import be.uclouvain.jail.algo.induct.internal.PTAState;
import be.uclouvain.jail.algo.induct.open.IWalker;
import be.uclouvain.jail.fa.IDFA;

/** Provide a utility to extract suffixes of states. */
public class SuffixesExtractor implements IWalker {

	/** Encountered letters. */
	private Stack<Object> letters;

	/** Extracted suffixes. */
	private List<Suffix> suffixes;

	/** Source PTA. */
	private IDFA pta;

	/** 
	 * Creates an extractor instance. 
	 * 
	 * @param PTA prefix tree acceptor to extract suffixes from. 
	 * @param suffixes list of suffixes to fill. 
	 */
	public SuffixesExtractor(IDFA pta, List<Suffix> suffixes) {
		letters = new Stack<Object>();
		this.pta = pta;
		this.suffixes = suffixes;
	}

	/** When entering a state at left. */
	public boolean walksLeftOf(PTAEdge edge, PTAState state) {
		// bypass root state (no incoming letter)
		if (edge != null) {
			letters.push(edge.letter());
		}
		return true;
	}

	/** When leaving a state at right. */
	public boolean walksRightOf(PTAEdge edge, PTAState state, boolean cVisited) {
		// flush suffix, if any.
		if (state.letters().isEmpty()) {
			Object rep = state.representor();
			flushSuffix(pta.isError(rep));
		}
		if (edge == null) {
			return true;
		} else {
			letters.pop();
			return false;
		}
	}

	private boolean first = true;
	
	/** When entering a state at left. */
	public void entering(IDFA pta, Object state) {
		if (!first) {
			Object letter = pta.getIncomingLetters(state).toArray()[0];
			letters.push(letter);
		}
		first = false;
	}

	/** When leaving a state. */
	public void leaving(IDFA pta, Object state) {
		if (pta.getGraph().getOutgoingEdges(state).isEmpty()) {
			flushSuffix(pta.isError(state));
		}
	}

	/** Flushes a suffix. */
	private void flushSuffix(boolean negative) {
		suffixes.add(new Suffix(letters.toArray(), negative));
	}

}
