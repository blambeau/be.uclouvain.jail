package be.uclouvain.jail.algo.fa.rand;

import be.uclouvain.jail.algo.commons.IAlgoInput;
import be.uclouvain.jail.common.IPredicate;
import be.uclouvain.jail.fa.IAlphabet;
import be.uclouvain.jail.fa.IString;

/**
 * Encapsulates input of the RandomStringsAlgo.
 * 
 * @author blambeau
 */
public interface IRandomStringsInput<L> extends IAlgoInput {

	/** Returns alphabet. */
	public IAlphabet<L> getAlphabet();

	/** Returns stop predicate. */
	public IPredicate<IRandomStringsResult<L>> getStopPredicate();

	/** Returns word stop predicate. */
	public IPredicate<IString<L>> getStringStopPredicate();
	
}
