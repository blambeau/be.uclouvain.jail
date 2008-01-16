package be.uclouvain.jail.fa.utils;

import java.util.Iterator;

import net.chefbe.javautils.adapt.AdaptUtils;
import be.uclouvain.jail.fa.IAlphabet;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.IExtensibleSample;
import be.uclouvain.jail.fa.IString;
import be.uclouvain.jail.fa.constraints.PTAGraphConstraint;
import be.uclouvain.jail.graph.IDirectedGraph;

/**
 * Decorates a MCA as a sample.
 * 
 * @author blambeau
 * @param <L>
 */
public class PTASample<L> implements IExtensibleSample<L> {

	/** Decorated dfa. */
	private IDFA pta;
	
	/** Creates a sample instance. */
	public PTASample(IDFA nfa) {
		assert (new PTAGraphConstraint().isRespectedBy(nfa.getGraph())) : "Valid PTA.";
		this.pta = nfa;
	}
	
	/** Returns the sample alphabet. */
	public IAlphabet<L> getAlphabet() {
		return pta.getAlphabet();
	}

	/** Returns true if the sample contains a given string. */
	public boolean contains(IString<L> s) {
		return FAWalkUtils.walk(pta,s) != null;
	}
	
	/** Returns number of strings. */
	public int size() {
		return -1; //return inits.size();
	}

	/** Returns an iterator on strings. */
	public Iterator<IString<L>> iterator() {
		return null;
	}

	/** Adds a sample string, by writing in the nfa. */
	public void addString(IString<L> s) {
	}

	/** Provides adaptations. */
	public <T> Object adapt(Class<T> c) {
		if (c.isAssignableFrom(getClass())) {
			return this;
		}
		
		// provide classical adaptations
		if (IDirectedGraph.class.equals(c)) {
			return pta.getGraph();
		} else if (IDFA.class.equals(c)) {
			return pta;
		}
		 
		// allow external adapters to do their work
		return AdaptUtils.externalAdapt(this,c);
	}

}
