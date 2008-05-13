package be.uclouvain.jail.algo.lsm;

import be.uclouvain.jail.algo.utils.AbstractAlgoInput;
import be.uclouvain.jail.fa.IDFA;

/**
 * Algorithm input.
 * 
 * @author blambeau
 */
public class LSMAlgoInput extends AbstractAlgoInput {

	/** Source DFA. */
	private IDFA source;
	
	/** Creates an algorithm input. */
	public LSMAlgoInput(IDFA source) {
		this.source = source;
	}

	/** Returns the source. */
	public IDFA getSource() {
		return source;
	}
	
}
