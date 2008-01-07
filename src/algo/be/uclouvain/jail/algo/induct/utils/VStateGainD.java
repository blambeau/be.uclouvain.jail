package be.uclouvain.jail.algo.induct.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import be.uclouvain.jail.algo.induct.internal.IWork;
import be.uclouvain.jail.algo.induct.internal.PTAState;
import be.uclouvain.jail.algo.induct.internal.WorkType;
import be.uclouvain.jail.algo.induct.internal.WorkUtils;
import be.uclouvain.jail.fa.IDFA;

/** Decorates a VStateGain with utilities. */
public class VStateGainD implements IGainD {

	/** Decorated work. */
	private IWork work;

	/** Source PTA. */
	private IDFA pta;
	
	public VStateGainD(IWork work) {
		if (!WorkType.VStateGain.equals(work.type())) {
			throw new IllegalArgumentException("VStateGain work expected.");
		}
		this.work = work;
		this.pta = WorkUtils.getSourcePTA(work);
	}

	/** Returns the letter of the gained edge. */
	public Object letter() {
		// victim is gained PTA edge 
		Object ptaEdge = work.victim();
		Object letter = pta.getEdgeLetter(ptaEdge);
		return letter;
	}

	/** Returns the short prefix of the target other state. */
	public Object[] shortPrefix() {
		PTAState state = (PTAState) work.target();
		Object ptaState = state.representor();
		return WorkUtils.shortPrefix(ptaState);
	}

	/** Extracts the suffixes of the merged state. */
	public Iterator<Suffix> suffixes() {
		// create suffixes and extractor
		List<Suffix> suffixes = new ArrayList<Suffix>();
		/*
		SuffixesExtractor extractor = new SuffixesExtractor(pta, suffixes);
		
		// retrieve first state
		Object ptaEdge = work.victim();
		Object target = pta.getEdgeTarget(ptaEdge);
		
		// make visit
		PrefixTreeAcceptorVisit.infixVisit(pta,extractor,target);
		*/
		
		// return suffixes
		return suffixes.iterator();
	}

}
