package be.uclouvain.jail.algo.induct.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import be.uclouvain.jail.algo.induct.internal.IWork;
import be.uclouvain.jail.algo.induct.internal.InductionAlgo;
import be.uclouvain.jail.algo.induct.internal.MappingUtils;
import be.uclouvain.jail.algo.induct.internal.PTAEdge;
import be.uclouvain.jail.algo.induct.internal.PTAState;
import be.uclouvain.jail.algo.induct.internal.WorkType;
import be.uclouvain.jail.algo.induct.internal.WorkUtils;

/** Decorates a KStateGain with utilities. */
public class KStateGainD implements IGainD {

	/** Decorated work. */
	private IWork work;

	public KStateGainD(IWork work) {
		if (!WorkType.KStateGain.equals(work.type())) {
			throw new IllegalArgumentException("KStateGain work expected.");
		}
		this.work = work;
	}

	/** Returns the kernel state. */
	public Object kState() {
		return work.target();
	}

	/** Returns the letter of the gained edge. */
	public Object letter() {
		return edgeGain().letter();
	}

	/** Returns the gained edge. */
	public PTAEdge edgeGain() {
		Object victim = work.victim();
		assert (victim instanceof PTAEdge) : "Victim of a KStateGain is a PTAEdge.";
		return (PTAEdge) victim;
	}

	/** Returns the target of the gained edge (aka gained state). */
	public PTAState stateGain() {
		return edgeGain().target();
	}

	/** Returns the short prefix of the target kernel state. */
	public Object[] shortPrefix() {
		InductionAlgo algo = WorkUtils.getRunningAlgo(work);
		Object rep = MappingUtils.sRepresentor(algo, kState());
		assert (rep != null) : "Kernel state has a representor.";
		return WorkUtils.shortPrefix(rep);
	}

	/** Extracts the suffixes of the merge state. */
	public Iterator<Suffix> suffixes() {
		List<Suffix> suffixes = new ArrayList<Suffix>();
		stateGain().accept(new SuffixesExtractor(WorkUtils.getSourcePTA(work), suffixes));
		return suffixes.iterator();
	}

}
