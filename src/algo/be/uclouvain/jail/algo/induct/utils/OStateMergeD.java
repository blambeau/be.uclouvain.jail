package be.uclouvain.jail.algo.induct.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import be.uclouvain.jail.algo.induct.internal.IWork;
import be.uclouvain.jail.algo.induct.internal.PTAState;
import be.uclouvain.jail.algo.induct.internal.WorkType;
import be.uclouvain.jail.algo.induct.internal.WorkUtils;

/** Decorates a other state merge. */
public class OStateMergeD implements IMergeD {

	/** Decorated work. */
	private IWork work;

	/** Creates a decorator. */
	public OStateMergeD(IWork work) {
		if (!WorkType.OStateMerge.equals(work.type())) {
			throw new IllegalArgumentException("OStateMerge work expected.");
		} else {
			this.work = work;
			return;
		}
	}

	/** Returns the target state. */
	public PTAState targetState() {
		return (PTAState) work.target();
	}

	/** Returns the gained state (target of the gained edge). */
	public PTAState stateGain() {
		return (PTAState) work.victim();
	}

	/** Returns the short prefix of the target state. */
	public Object[] shortPrefix() {
		Object rep = targetState().representor();
		assert (rep != null) : "Target state has a representor.";
		return WorkUtils.shortPrefix(rep);
	}

	/** Extracts the suffixes of the target state. */
	public Iterator<Suffix> suffixes() {
		List<Suffix> suffixes = new ArrayList<Suffix>();
		stateGain().accept(new SuffixesExtractor(WorkUtils.getSourcePTA(work), suffixes));
		return suffixes.iterator();
	}

}
