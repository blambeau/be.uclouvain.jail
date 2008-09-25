package be.uclouvain.jail.algo.induct.oracle;

import be.uclouvain.jail.fa.IFATrace;
import be.uclouvain.jail.fa.ISample;
import be.uclouvain.jail.fa.IString;
import be.uclouvain.jail.fa.IWalkInfo;

/**
 * Provides a membership query.
 * 
 * @author blambeau
 */
public class MembershipQuery<T> {

	/** Returns underlying sample. */
	private ISample<T> sample;
	
	/** Query trace. */
	private IFATrace<T> trace;

	/** Walk info in the sample. */
	private IWalkInfo<T> info;
	
	/** Answer to the query. */
	private IString<T> answer;
	
	/** Creates a query instance. */
	@SuppressWarnings("unchecked")
	public MembershipQuery(ISample<T> sample, IFATrace<T> trace) {
		this.sample = sample;
		this.trace = trace;
		IString<T> string = (IString<T>) trace.adapt(IString.class);
		info = sample.walk(string);
	}

	/** Returns the underlying sample. */
	public ISample<T> getSample() {
		return sample;
	}
	
	/** Returns query trace. */
	public IFATrace<T> getTrace() {
		return trace;
	}
	
	/** Query is fully included in the sample? */
	public boolean isFullyIncluded() {
		return info.isFullyIncluded();
	}

	/** Query is fully excluded from the sample? */
	public boolean isFullyExcluded() {
		return info.isFullyExcluded();
	}

	/** Returns the part of the query which is already included in
	 * the sample. */
	public IFATrace<T> getIncludedPart() {
		return info.getIncludedPart();
	}
	
	/** Returns the part of the query which is already included in
	 * the sample. */
	public IString<T> getExcludedString() {
		return info.getExcludedString();
	}
	
	/** Returns user answer. */
	public IString<T> getAnswer() {
		return this.answer;
	}
	
	/** Sets user answer. */
	public void setAnswer(IString<T> answer) {
		this.answer = answer;
	}
	
	/** Returns a string representation. */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		int i=0;
		for (T t: getIncludedPart()) {
			if (i++ != 0) sb.append(" ");
			sb.append(t);
		}
		if (i++ != 0) sb.append(" ");
		sb.append("#");
		for (T t: getExcludedString()) {
			if (i++ != 0) sb.append(" ");
			sb.append(t);
		}
		return sb.toString();
	}
	
}
