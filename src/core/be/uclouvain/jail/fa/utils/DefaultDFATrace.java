package be.uclouvain.jail.fa.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.chefbe.javautils.adapt.AdaptUtils;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.IDFATrace;
import be.uclouvain.jail.graph.IDirectedGraphPath;

/**
 * Provides a default implementation of {@link IDFATrace}.
 * 
 * @author blambeau
 */
public class DefaultDFATrace<T> implements IDFATrace<T> {

	/** DFA from which this trace is extracted. */
	private IDFA dfa;
	
	/** Underlying graph path. */
	private IDirectedGraphPath path;
	
	/** Letters of the trace. */
	private List<T> letters; 
	
	/** Creates a trace instance. */
	public DefaultDFATrace(IDFA dfa, IDirectedGraphPath path) {
		this.dfa = dfa;
		this.path = path;
	}

	/** Returns the dfa from which this trace has been 
	 * extracted. */
	public IDFA getDFA() {
		return dfa;
	}

	/** Returns the underlying graph path. */
	public IDirectedGraphPath getGraphPath() {
		return path;
	}

	/** Returns an iterator on letters. */
	@SuppressWarnings("unchecked")
	public Iterator<T> iterator() {
		if (letters == null) {
			letters = new ArrayList<T>(path.size());
			for (Object edge: path.edges()) {
				letters.add((T)dfa.getEdgeLetter(edge));
			}
		}
		return letters.iterator();
	}

	/** Adapts this trace to another type. */
	public <S> Object adapt(Class<S> c) {
		if (c.isAssignableFrom(this.getClass())) {
			return this;
		}
		return AdaptUtils.externalAdapt(this,c);
	}

}
