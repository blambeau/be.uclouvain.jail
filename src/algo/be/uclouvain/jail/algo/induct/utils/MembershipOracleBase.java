package be.uclouvain.jail.algo.induct.utils;

import be.uclouvain.jail.algo.commons.Avoid;
import be.uclouvain.jail.algo.commons.Restart;
import be.uclouvain.jail.algo.induct.internal.Simulation;
import be.uclouvain.jail.algo.induct.open.IOracle;

/**
 * Provides a base implementation for membership query 
 * oracles.
 *  
 * @author blambeau
 */
public class MembershipOracleBase implements IOracle {

	/** Accepts the simulation? */
	public boolean accept(Simulation simulation) throws Avoid, Restart {
		return false;
	}

}
