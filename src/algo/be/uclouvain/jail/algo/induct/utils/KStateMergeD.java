package be.uclouvain.jail.algo.induct.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import be.uclouvain.jail.algo.induct.internal.IWork;
import be.uclouvain.jail.algo.induct.internal.InductionAlgo;
import be.uclouvain.jail.algo.induct.internal.MappingUtils;
import be.uclouvain.jail.algo.induct.internal.PTAState;
import be.uclouvain.jail.algo.induct.internal.WorkType;
import be.uclouvain.jail.algo.induct.internal.WorkUtils;

/** Decorates a kernel state merge. */
public class KStateMergeD implements IMergeD {

	/** Decorated work. */
	private IWork work;

	/** Creates a decorator. */
	public KStateMergeD(IWork work) {
		if (!WorkType.KStateMerge.equals(work.type())) {
			throw new IllegalArgumentException("KStateMerge work expected.");
		} else {
			this.work = work;
			return;
		}
	}

	/** Returns the kernel state. */
	public Object kState() {
		return work.target();
	}

	/** Returns the target of the gained edge (aka gained state). */
	public PTAState stateGain() {
		return (PTAState) work.victim();
	}
	
	/** Returns the short prefix of the target state. */
	public Object[] shortPrefix() {
		InductionAlgo algo = WorkUtils.getRunningAlgo(work);
		Object rep = MappingUtils.sRepresentor(algo, kState());
		assert (rep != null) : "Kernel state has a representor.";
		return WorkUtils.shortPrefix(rep);
	}

	/** Returns the suffixes of the victim state. */
	public Iterator suffixes() {
		List<Suffix> suffixes = new ArrayList<Suffix>();
		stateGain().accept(new SuffixesExtractor(WorkUtils.getSourcePTA(work), suffixes));
		return suffixes.iterator();
	}

}
