package be.uclouvain.jail.algo.induct.open;

import be.uclouvain.jail.algo.induct.utils.MembershipQuery;

/**
 * Send a query to the user.
 * 
 * @author blambeau
 */
public interface IMembershipQueryTester {

	/** Accepts a given query? */
	public <T> boolean accept(MembershipQuery<T> query);
	
}
