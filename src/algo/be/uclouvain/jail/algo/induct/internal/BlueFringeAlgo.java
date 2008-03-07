package be.uclouvain.jail.algo.induct.internal;

import java.util.Iterator;
import java.util.TreeSet;

import be.uclouvain.jail.algo.commons.Avoid;
import be.uclouvain.jail.algo.commons.Restart;
import be.uclouvain.jail.algo.commons.Unable;
import be.uclouvain.jail.algo.induct.utils.IEvaluator;
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
public final class BlueFringeAlgo extends InductionAlgo {
	
	/** Evaluation of a merge simulation. */ 
	class Evaluation implements Comparable<Evaluation> {

		/** Fringe edge. */
		protected PTAEdge fEdge;
		
		/** Target merge state. */
		protected Object kState;
		
		/** Evaluation value. */ 
		int evaluation;

		/** Creates an evaluation instance. */
		public Evaluation(PTAEdge fEdge, Object kState, int eval) {
			this.fEdge = fEdge;
			this.kState = kState;
			evaluation = eval;
		}
		
		/** Evaluations comparison. */ 
		public int compareTo(Evaluation arg0) {
			Evaluation other = (Evaluation) arg0;
			return other.evaluation - evaluation;
		}
		
	}

	/** Creates a BlueFringe instance. */
	public BlueFringeAlgo() {
	}

	/** BlueFringe main loop. */
	protected void mainLoop() throws Restart {
		// work 
		Simulation simu = null;
		
		// current evaluations, order by preference. 
		TreeSet<Evaluation> evaluations = new TreeSet<Evaluation>();
		
		// continue until no state on the fringe
		IDirectedGraph dfag = dfa.getGraph();
		IEvaluator evaluator = input.getEvaluator();
		int cThreshold = input.getConsolidationThreshold();
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
							simu = new Simulation(this);
							simu.startTry(fEdge, kState);
							
							// evaluate simulation and keep result if required
							int eval = evaluator.evaluate(simu);
							if (eval > cThreshold) {
								evaluations.add(new Evaluation(fEdge, kState, eval));
								hasEval = true;
							}
							
							// rollback work in all cases
							simu.rollback();
						} catch (Avoid avoid) {
							simu.rollback();
						}
					}
				}

				// no evaluation -> edge consilidation and break
				if (!hasEval) {
					try {
						simu = new Simulation(this);
						simu.consolidate(fEdge);
						simu.commit();
						break;
					} catch (Avoid ex) {
						throw new Unable("Unexcpected avoid exception on consolidation.",ex);
					}
				}
			}

			// when at least an evaluation
			boolean commited = false;
			if (hasEval) {
				// find the best ones in order an try them
				for (Evaluation eval : evaluations) {
					// replay simulation
					try {
						simu = new Simulation(this);
						simu.startTry(eval.fEdge, eval.kState);
					} catch (Avoid ex) {
						throw new Unable("Unexcpected avoid exception on replay.",ex);
					}
					
					try { 
						// last chance to reject it
						checkWithOracle(simu);
						
						// all ok, take it as current merge
						simu.commit();
						commited = true;
						break;
					} catch (Avoid avoid) {
						simu.rollback();
					}
				}

			}
			
			// when no evaluation or all rejected by the oracle
			if (hasEval && !commited) {
				// consolidate first one ... a bit arbitrary actually
				try {
					PTAEdge fEdge = fringe.iterator().next(); 
					simu = new Simulation(this);
					simu.consolidate(fEdge);
					simu.commit();
					break;
				} catch (Avoid ex) {
					throw new Unable("Unexcpected avoid exception on consolidation.",ex);
				}
			}
		}
	}
}
