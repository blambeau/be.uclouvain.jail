package be.uclouvain.jail.algo.induct.internal;

import java.util.Iterator;
import java.util.TreeSet;

import be.uclouvain.jail.algo.commons.Avoid;
import be.uclouvain.jail.algo.commons.Restart;
import be.uclouvain.jail.algo.induct.open.IEvaluator;
import be.uclouvain.jail.graph.IDirectedGraph;

/** 
 * Provides an implementation of the well known blue-fringe induction 
 * algorithm.
 *
 * <p>This class is a specialization of {@link InductionAlgo} which installs
 * the blue-fringe main loop. When using this main-loop, user is required
 * to provide an simulation evaluator {@link IEvaluator} in the algorithm
 * input.</p>
 */
public final class BlueFringe extends InductionAlgo {
	
	/** Evaluation of a merge simulation. */ 
	class Evaluation implements Comparable<Evaluation> {

		/** Evaluated simulation. */
		Simulation simu;

		/** Evaluation value. */ 
		int evaluation;

		/** Creates an evaluation instance. */
		public Evaluation(Simulation work, int eval) {
			simu = work;
			evaluation = eval;
		}
		
		/** Evaluations comparison. */ 
		public int compareTo(Evaluation arg0) {
			Evaluation other = (Evaluation) arg0;
			return other.evaluation - evaluation;
		}
		
	}

	/** Creates a BlueFringe instance. */
	public BlueFringe() {
	}

	/** BlueFringe main loop. */
	protected void mainLoop() throws Restart {
		// work 
		Simulation work = null;
		
		// current evaluations, order by preference. 
		TreeSet<Evaluation> evaluations = new TreeSet<Evaluation>();
		
		// continue until no state on the fringe
		IDirectedGraph dfag = dfa.getGraph();
		IEvaluator evaluator = info.getEvaluator();
		int cThreshold = info.getConsolidationThreshold();
		while (!fringe.isEmpty()) {
			
			// no evaluation at all
			evaluations.clear();
			boolean hasEval = false;
			
			// for each state on the fringe
			for (Iterator iterator = fringe.iterator(); iterator.hasNext();) {
				PTAEdge fEdge = (PTAEdge) iterator.next();
				hasEval = false;
				
				// for each kernel state
				for (Object kState: dfag.getVertices()) {
					
					// check compatibility
					if (isCompatible(kState, fEdge)) {
						try {
							// create new simulation
							work = new Simulation(this, fEdge, kState);
							fEdge.simulate(this, work);
							
							// evaluate simulation and keep result if required
							int eval = evaluator.evaluate(work);
							if (eval > cThreshold) {
								evaluations.add(new Evaluation(work, eval));
								hasEval = true;
							}
							
							// rollback work in all cases
							work.rollback();
						} catch (Avoid avoid) {
							work.rollback();
						}
					}
				}

				// no evaluation -> edge consilidation and break
				if (!hasEval) {
					fEdge.consolidate(this);
					break;
				}
			}

			// when at least an evaluation
			boolean commited = false;
			if (hasEval) {
				// find the best ones in order an try them
				for (Evaluation eval : evaluations) {
					try {
						// last chance to reject it
						checkWithOracle(eval.simu);
						
						// all ok, take it as current merge
						eval.simu.commit();
						commited = true;
						break;
					} catch (Avoid avoid1) {}
				}

			}
			
			// when no evaluation or all rejected by the oracle
			if (hasEval && !commited) {
				// consolidate first one ... a bit arbitrary actually
				Simulation simu = ((Evaluation) evaluations.first()).simu;
				simu.getFringeEdge().consolidate(this);
			}
		}
	}
}
