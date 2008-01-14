package be.uclouvain.jail.fa.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.chefbe.javautils.adapt.AdaptUtils;
import be.uclouvain.jail.fa.IFA;
import be.uclouvain.jail.fa.IFATrace;
import be.uclouvain.jail.graph.IDirectedGraphPath;

/**
 * Provides a default implementation of {@link IFATrace}.
 * 
 * @author blambeau
 */
public class DefaultFATrace<T> implements IFATrace<T> {

	/** FA from which this trace is extracted. */
	private IFA fa;
	
	/** Underlying graph path. */
	private IDirectedGraphPath path;
	
	/** Letters of the trace. */
	private List<T> letters; 
	
	/** Creates a trace instance. */
	public DefaultFATrace(IFA dfa, IDirectedGraphPath path) {
		this.fa = dfa;
		this.path = path;
	}

	/** Returns the dfa from which this trace has been 
	 * extracted. */
	public IFA getFA() {
		return fa;
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
				letters.add((T)fa.getEdgeLetter(edge));
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
