package be.uclouvain.jail.algo.induct.utils;

import java.util.Iterator;

import be.uclouvain.jail.algo.commons.Avoid;
import be.uclouvain.jail.algo.commons.Restart;
import be.uclouvain.jail.algo.induct.internal.PTAState;
import be.uclouvain.jail.algo.induct.internal.Simulation;
import be.uclouvain.jail.algo.induct.open.IMembershipQueryTester;
import be.uclouvain.jail.algo.induct.open.IOracle;
import be.uclouvain.jail.algo.induct.open.ISuffixExtractor;
import be.uclouvain.jail.fa.IDFATrace;

/**
 * Provides a base implementation for membership query oracles.
 *  
 * @author blambeau
 */
public abstract class AbstractMembershipOracle implements IOracle {

	/** Simulation. */
	protected Simulation simulation;
	
	/** Suffixes extractor to use. */
	protected ISuffixExtractor sExtractor;
	
	/** Tester to use. */
	protected IMembershipQueryTester tester;
	
	/** Creates an oracle with a given extractor. */
	public AbstractMembershipOracle() {
	}
	
	/** Sets the extractor to use. */
	public void setExtractor(ISuffixExtractor extractor) {
		this.sExtractor = extractor;
	}
	
	/** Sets the tester to use. */
	public void setTester(IMembershipQueryTester tester) {
		this.tester = tester;
	}
	
	/** Returns an iterator on state suffixes. */
	protected <T> Iterator<IDFATrace<T>> suffixesOf(PTAState state) {
		return sExtractor.extract(state);
	}
	
	/** Computes the short prefix of a state. */
	protected <T> IDFATrace<T> shortPrefixOf(PTAState state) {
		return state.getShortPrefix();
	}
	
	/** Send queries to the tester. */
	protected <T> void sendQueries(PTAState target, PTAState gained) throws Avoid, Restart {
		IDFATrace<T> prefix = shortPrefixOf(target);
		Iterator<IDFATrace<T>> suffixes = suffixesOf(gained);
		while (suffixes.hasNext()) {
			IDFATrace<T> suffix = suffixes.next();
			sendQuery(prefix,suffix);
		}
	}
	
	/** Sends a query. */
	protected <T> void sendQuery(IDFATrace<T> prefix, IDFATrace<T> suffix) throws Avoid, Restart {
		MembershipQuery<T> query = new MembershipQuery<T>(prefix,suffix);
		if (tester.accept(query)) {
			queryAccepted(query);
		} else {
			queryRejected(query);
		}
	}
	
	/** Fired when a query is accepted. */
	protected void queryAccepted(MembershipQuery query) throws Avoid, Restart {
	}
	
	/** Fired when a query is rejected. */
	protected void queryRejected(MembershipQuery query) throws Avoid, Restart {
		throw new Avoid();
	}
	
	/** Accepts the simulation? */
	public final boolean accept(Simulation simulation) throws Avoid, Restart {
		if (tester == null || sExtractor == null) {
			throw new IllegalStateException("Tester and extractor must have been set.");
		}
		this.simulation = simulation;
		return doAccept(simulation);
	}

	/** To be implemented. */
	protected abstract boolean doAccept(Simulation simu);

}
