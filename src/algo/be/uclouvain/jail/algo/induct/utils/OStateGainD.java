package be.uclouvain.jail.algo.induct.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import be.uclouvain.jail.algo.induct.internal.IWork;
import be.uclouvain.jail.algo.induct.internal.PTAEdge;
import be.uclouvain.jail.algo.induct.internal.PTAState;
import be.uclouvain.jail.algo.induct.internal.WorkType;
import be.uclouvain.jail.algo.induct.internal.WorkUtils;

/** Decorates another state gain. */
public class OStateGainD implements IGainD {

	/** Decorated work. */
	private IWork work;

	/** Creates a decorator. */
	public OStateGainD(IWork work) {
		if (!WorkType.OStateGain.equals(work.type())) {
			throw new IllegalArgumentException("OStateGain work expected.");
		} else {
			this.work = work;
			return;
		}
	}

	/** Returns the target state. */
	public PTAState targetState() {
		return (PTAState) work.target();
	}

	/** Returns gained letter. */
	public Object letter() {
		return edgeGain().letter();
	}

	/** Returns gained edge. */
	public PTAEdge edgeGain() {
		Object victim = work.victim();
		assert (victim instanceof PTAEdge) : "Victim of a KStateGain is a PTAEdge.";
		return (PTAEdge) victim;
	}

	/** Returns the gained state (target of the gained edge). */
	public PTAState stateGain() {
		return edgeGain().target();
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
