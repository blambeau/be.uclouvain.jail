package be.uclouvain.jail.algo.induct.utils;

import java.util.Iterator;

import be.uclouvain.jail.algo.commons.Avoid;
import be.uclouvain.jail.algo.commons.Restart;
import be.uclouvain.jail.algo.induct.internal.InductionAlgo;
import be.uclouvain.jail.algo.induct.internal.PTAState;
import be.uclouvain.jail.algo.induct.internal.Simulation;
import be.uclouvain.jail.algo.induct.open.IMembershipQueryTester;
import be.uclouvain.jail.algo.induct.open.IOracle;
import be.uclouvain.jail.algo.induct.open.ISuffixExtractor;
import be.uclouvain.jail.fa.IFATrace;
import be.uclouvain.jail.fa.ISample;
import be.uclouvain.jail.fa.IString;

/**
 * Provides a base implementation for membership query oracles.
 *  
 * @author blambeau
 */
public abstract class AbstractMembershipOracle<T> implements IOracle {

	/** Input sample. */
	protected ISample<T> sample;
	
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
	
	/** Initializes the oracle. */
	@SuppressWarnings("unchecked")
	public void initialize(InductionAlgo algo) {
		this.sample = algo.getInfo().getInput();
	}
	
	/** Returns an iterator on state suffixes. */
	protected Iterator<IFATrace<T>> suffixesOf(PTAState state) {
		return sExtractor.extract(state);
	}
	
	/** Computes the short prefix of a state. */
	protected IFATrace<T> shortPrefixOf(PTAState state) {
		return state.getShortPrefix();
	}
	
	/** Send queries to the tester. */
	protected void sendQueries(PTAState target, PTAState gained) throws Avoid, Restart {
		IFATrace<T> prefix = shortPrefixOf(target);
		Iterator<IFATrace<T>> suffixes = suffixesOf(gained);
		while (suffixes.hasNext()) {
			IFATrace<T> suffix = suffixes.next();

			//System.out.println("With prefix " + prefix);
			//System.out.println("With suffix " + suffix);

			// merge prefix and suffix and send query
			IFATrace<T> trace = prefix.append(suffix);
			
			//System.out.println("Gives trace " + trace);
			sendQuery(trace);
		}
	}
	
	/** Sends a query. */
	protected void sendQuery(IFATrace<T> trace) throws Avoid, Restart {
		//System.out.println("Sending query " + trace);
		
		// bypass negative queries for now
		if (!trace.isAccepted()) { 
			//System.out.println("Bypassing negative query");
			return; 
		}

		// create query instance
		MembershipQuery<T> query = new MembershipQuery<T>(sample, trace);
		
		// bypass queries already accepted by the sample
		if (query.isFullyIncluded()) { 
			//System.out.println("Bypassing fully included query");
			return; 
		}
		
		// request the tester to check the query
		boolean ok = tester.accept(query);
		//System.out.println("Query is accepted? " + ok);
		if (ok) {
			queryAccepted(query);
		} else {
			queryRejected(query);
		}
	}
	
	/** Fired when a query is accepted. */
	protected void queryAccepted(MembershipQuery<T> query) throws Avoid, Restart {
		IString<T> answer = query.getAnswer();
		if (answer != null) {
			//System.out.println("Adding query to sample: " + answer);
			sample.addString(answer);
		} else {
			//System.out.println("No answer");
		}
	}
	
	/** Fired when a query is rejected. */
	protected void queryRejected(MembershipQuery<T> query) throws Avoid, Restart {
		IString<T> answer = query.getAnswer();
		if (answer != null) {
			//System.out.println("Adding query to sample and restarting: " + answer.toString());
			sample.addString(answer);
			throw new Restart();
		} else {
			//System.out.println("No answer ... just avoid");
			throw new Avoid();
		}
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
