package be.uclouvain.jail.algo.fa.minimize;

import java.util.HashSet;
import java.util.Set;

import be.uclouvain.jail.fa.IDFA;

/**
 * Provides a default implementation of {@link IDFAMinimizerResult}.
 * 
 * @author blambeau
 */
public class DefaultDFAMinimizerResult implements IDFAMinimizerResult {

	/** The block structure. */
	private SetBlockStructure<Object> blocks;
	
	/** 
	 * Algorithm started event.
	 * 
	 * <p>Initial partition is the classical one: accepting, non
	 * accepting and error states.</p>
	 */
	@SuppressWarnings("unchecked")
	public IBlockStructure<Object> started(IDFAMinimizerInput input) {
		IDFA dfa = input.getDFA();

		Set[] blocks = new Set[]{new HashSet(), new HashSet(), new HashSet()};
		for (Object state: dfa.getGraph().getVertices()) {
			if (dfa.isError(state)) {
				blocks[2].add(state);
			} else if (dfa.isAccepting(state)) {
				blocks[0].add(state);
			} else {
				blocks[1].add(state);
			}
		}
		
		this.blocks = new SetBlockStructure<Object>(blocks);
		return this.blocks;
	}

	/** Updates the structure to reflect the change. */
	public int refined(Set<Object> block, Set<Object> unreachable) {
		return blocks.refine(unreachable);
	}
	
	/** Returns the computed partition. */
	public IBlockStructure<Object> getStatePartition() {
		return blocks;
	}

}
