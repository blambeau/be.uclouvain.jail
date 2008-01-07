package be.uclouvain.jail.algo.induct.utils;

import be.uclouvain.jail.algo.induct.internal.IWork;
import be.uclouvain.jail.algo.induct.internal.Simulation;
import be.uclouvain.jail.algo.induct.internal.WorkType;
import be.uclouvain.jail.algo.induct.open.IEvaluator;

/** Classical Blue-Fringe evaluator. 
 * 
 * <p>This evaluator counts the state merges and use that count as 
 * the evaluation answer.</p>
 */ 
public class ClassicEvaluator implements IEvaluator {

	/** Creates an evaluator instance. */
	public ClassicEvaluator() {
	}

	/** Counts state merging. */
	public int evaluate(Simulation simu) {
		// just a hack (final array for single value)
		final int count[] = new int[1];
		
		// visit KState and OState merging
		simu.accept(new FilteredSimuVisitor(WorkType.KStateMerge, WorkType.OStateMerge) {
			public void doOnWork(Simulation simu, IWork work, WorkType type) {
				count[0]++;
			}
		});
		
		// return number of merge
		return count[0];
	}
}
