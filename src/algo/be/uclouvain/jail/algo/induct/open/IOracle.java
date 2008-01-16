package be.uclouvain.jail.algo.induct.open;

import be.uclouvain.jail.algo.commons.Avoid;
import be.uclouvain.jail.algo.commons.Restart;
import be.uclouvain.jail.algo.induct.internal.InductionAlgo;
import be.uclouvain.jail.algo.induct.internal.Simulation;

/**
 * Encapsulates oracle ability.
 *
 * <p>An oracle is able to check a merge simulations for acceptance
 * or rejection. This check is typically implemented by equivalence 
 * or membership queries.</p>
 * 
 * <p>The oracle may accept the simulation (aka 'commit'), or reject 
 * it by throwing an Avoid exception (aka 'rollback'). Moreover, an 
 * oracle may restart the whole induction process through a Restart 
 * exception. Please note however that restarting the whole induction 
 * process should never been made unless the initial sample has changed 
 * (to avoid non-termination of the induction).</p>
 * 
 * @author blambeau
 */
public interface IOracle {

	/** Initializes the oracle. */
	public void initialize(InductionAlgo algo);
	
	/** 
	 * Checks the simulation for acceptance. 
	 *
	 * @return true to accept the simulation, false otherwise.
	 * @throws Avoid to reject the simulation (same as returning false).
	 * @throws Restart to restart the whole induction process on a refined
	 * sample.
	 */
	public boolean accept(Simulation simulation) throws Avoid, Restart;

}
