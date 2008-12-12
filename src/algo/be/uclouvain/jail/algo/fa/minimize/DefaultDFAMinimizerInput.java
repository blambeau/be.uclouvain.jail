package be.uclouvain.jail.algo.fa.minimize;

import be.uclouvain.jail.algo.fa.utils.FAStateKindPartitionner;
import be.uclouvain.jail.algo.graph.utils.IGraphPartitionner;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.constraints.DFAGraphConstraint;

/**
 * Provides a default implementation of {@link IDFAMinimizerInput}.
 * 
 * @author blambeau
 */
public class DefaultDFAMinimizerInput implements IDFAMinimizerInput {

	/** DFA to minimize. */
	private IDFA dfa;

	/** Ensures connex component from the initial state? */
	private boolean connex = false;
	
	/** Creates an input instance. */
	public DefaultDFAMinimizerInput(IDFA dfa) {
		if (! new DFAGraphConstraint().isRespectedBy(dfa.getGraph())) {
			throw new IllegalArgumentException("Not a DFA.");
		}
		this.dfa = dfa;
	}

	/** Returns the DFA to minimize. */
	public IDFA getDFA() {
		return dfa;
	}

	/** Sets connex. */
	public void setConnex(boolean connex) {
		this.connex = connex;
	}
	
	/** {@inheritDoc} */
	public boolean connex() {
		return connex;
	}

	/** Returns the initial partitionner. */
	public IGraphPartitionner<Object> getInitPartitionner() {
		return new FAStateKindPartitionner(dfa);
	}

	
}
