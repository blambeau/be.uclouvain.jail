package be.uclouvain.jail.algo.induct.oracle;

import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.IFATrace;
import be.uclouvain.jail.fa.IString;
import be.uclouvain.jail.fa.IWalkInfo;
import be.uclouvain.jail.uinfo.IUserInfoHandler;

/**
 * Query tester which uses a DFA to answer the question.
 * 
 * @author blambeau
 */
public class AutoQueryTester implements IMembershipQueryTester {

	/** DFA to use to answer the queries. */
	private IDFA dfa;
	
	/** Creates a tester instance. */
	public AutoQueryTester(IDFA dfa) {
		if (dfa == null) { throw new IllegalArgumentException("DFA cannot be null"); }
		this.dfa = dfa;
	}

	/** Accepts the query? */
	@SuppressWarnings("unchecked")
	public <T> boolean accept(MembershipQuery<T> query) {
		// walk the query trace inside the known target
		IFATrace<T> trace = query.getTrace();
		IWalkInfo<T> info = trace.walk(dfa);
		
		// normalize the info
		IUserInfoHandler handler = query.getSample().getUserInfoHandler();
		IFATrace<T> normalized = info.normalize(handler);
		IString<T> answer = (IString<T>) normalized.adapt(IString.class);
		query.setAnswer(answer);
		return normalized.isAccepted();
	}

}
