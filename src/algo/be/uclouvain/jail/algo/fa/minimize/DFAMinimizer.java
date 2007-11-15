package be.uclouvain.jail.algo.fa.minimize;

import be.uclouvain.jail.algo.graph.utils.IGraphPartition;
import be.uclouvain.jail.fa.IDFA;

/**
 * Provides a user-friendly API on top of the minimizer algorithm.
 * 
 * @author blambeau
 */
public class DFAMinimizer {

	/** Algorithm input. */ 
	private IDFAMinimizerInput input;
	
	/** Algorithm result. */ 
	private IDFAMinimizerResult result;
	
	/** Algorithm has been executed? */
	private boolean executed = false;
	
	/** Creates an algorithm instance. */
	public DFAMinimizer(IDFA dfa) {
		this.input = new DefaultDFAMinimizerInput(dfa);
		this.result = new DefaultDFAMinimizerResult();
	}
	
	/** Returns computed partition. */
	public IGraphPartition getStatePartition() {
		if (!executed) {execute();}
		return (IGraphPartition) result.adapt(IGraphPartition.class);
	}
	
	/** Computed the minimal DFA. */
	public IDFA getMinimalDFA() {
		if (!executed) {execute();}
		return (IDFA) result.adapt(IDFA.class);
	}
	
	/** Executes the algorithm. */
	private void execute() {
		new DFAMinimizerAlgo().execute(input,result);
		executed = true;
	}
	
}
