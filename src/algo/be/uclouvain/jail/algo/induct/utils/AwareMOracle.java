package be.uclouvain.jail.algo.induct.utils;

import net.chefbe.javautils.collections.arrays.ArrayUtils;
import be.uclouvain.jail.algo.commons.Avoid;
import be.uclouvain.jail.algo.commons.Restart;
import be.uclouvain.jail.fa.IDFA;

/** Automatic oracle which knows the target. 
 * <p>
 * This oracle is introduced for academic automated tests. It is 
 * initialized with the known target. Merbership queries are check
 * for acceptance by a target walk. 
 * </p>
 * */
public class AwareMOracle extends MQueryOracle {

	/** Known target DFA. */
	protected IDFA target;

	/** Initial state. */
	protected Object initState;

	/** Creates an oracle instance. */
	public AwareMOracle(IDFA dfa) {
		target = dfa;
		initState = dfa.getInitialState();
	}

	/** Answers the query by a walk of the target. */
	protected boolean query(MembershipQuery query) throws Avoid, Restart {
		@SuppressWarnings("unused")
		Object letters[] = ArrayUtils.merge(query.prefix, query.letter, query.suffix);
		if (query.negative) {
			//return WalkUtils.isRejected(target, initState, letters);
		} else {
			//return !WalkUtils.isRejected(target, initState, letters);
		}
		return true;
	}

}