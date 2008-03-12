package be.uclouvain.jail.fa.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.chefbe.javautils.adapt.AdaptUtils;
import be.uclouvain.jail.fa.FAStateKind;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.IFA;
import be.uclouvain.jail.fa.IFATrace;
import be.uclouvain.jail.fa.IString;
import be.uclouvain.jail.fa.IWalkInfo;
import be.uclouvain.jail.graph.IDirectedGraphPath;
import be.uclouvain.jail.graph.IDirectedGraphWriter;
import be.uclouvain.jail.graph.IDirectedGraphPath.IVisitor;

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
		assert (dfa != null) : "Underlying FA is not null.";
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

	/** Returns root state of the path. */
	public Object getRootState() {
		return path.getRootVertex();
	}
	
	/** Returns the last state. */
	public Object getLastState() {
		return path.getLastVertex();
	}
	
	/** Returns true if this trace is accepted by the DFA. Acceptation is
	 * defined as the fact that the trace ends in an accepting state. */
	public boolean isAccepted() {
		Object last = getLastState();
		return FAStateKind.ACCEPTING.equals(fa.getStateKind(last));
	}
	
	/** Returns true if this trace ends in an passage state. */
	public boolean isPassage() {
		Object last = getLastState();
		return FAStateKind.PASSAGE.equals(fa.getStateKind(last));
	}
	
	/** Returns true if this trace ends in an error state. */
	public boolean isError() {
		Object last = getLastState();
		return FAStateKind.ERROR.equals(fa.getStateKind(last));
	}
	
	/** Returns true if this trace ends in an avoid state. */
	public boolean isAvoid() {
		Object last = getLastState();
		return FAStateKind.AVOID.equals(fa.getStateKind(last));
	}
	
	/** Returns the size of the path, defined as the number of used edges in 
	 * the path. The number of visited vertices is equal to getPathSize()+1
	 * by definition. */
	public int size() {
		return path.size();
	}

	/** Returns an iterator on path edges. */
	public List<Object> edges() {
		return path.edges();
	}

	/** Returns an iterator on path states. */
	public List<Object> states() {
		return path.vertices();
	}

	/** Appends an edge in the trace. */
	public void append(Object edge) {
		path.append(edge);
	}
	
	/** Appends with another trace. */
	public IFATrace<T> append(IFATrace<T> trace) {
		return new DefaultFATrace<T>(fa,path.append(trace.getGraphPath()));
	}
	
	/** Accepts a visitor. */
	public void accept(IVisitor visitor) {
		path.accept(visitor);
	}

	/** Walks a DFA. */
	public IWalkInfo<T> walk(IDFA fa) {
		return FAWalkUtils.stringWalk(fa, asString());
	}

	/** Creates a subtrace. */
	public IFATrace<T> subTrace(int start, int length) {
		IDirectedGraphPath path = this.path.subPath(start,length);
		return new DefaultFATrace<T>(fa,path);
	}
	
	/** Flushes in a writer and return the equivalent trace. */
	public IFATrace<T> flush(IDirectedGraphWriter writer) {
		IDirectedGraphPath copy = path.flush(writer);
		IFA fa = (IFA) writer.adapt(IFA.class);
		assert (fa != null) : "Writer " + writer + " is able to convert to a FA.";
		return new DefaultFATrace<T>(fa,copy);
	}

	/** Adapts as a string. */
	private IString<T> asString() {
		return new FATraceString<T>(this);
	}
	
	/** Returns a string representation. */
	public String toString() {
		return asString().toString();
	}
	
	/** Adapts this trace to another type. */
	public <S> Object adapt(Class<S> c) {
		if (c.isAssignableFrom(this.getClass())) {
			return this;
		}

		// adapt to a word
		if (IString.class.equals(c)) {
			return asString();
		}
		
		return AdaptUtils.externalAdapt(this,c);
	}

}
