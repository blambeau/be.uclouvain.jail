package be.uclouvain.jail.algo.fa.walk;

import java.util.Random;

import be.uclouvain.jail.algo.graph.walk.DefaultRandomWalkInput;
import be.uclouvain.jail.common.IPredicate;
import be.uclouvain.jail.fa.FAStateKind;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.IDirectedGraphPath;
import be.uclouvain.jail.graph.utils.DefaultDirectedGraphPath;

/**
 * Specialization of {@link DefaultRandomWalkInput} for DFA cases.
 * 
 * @author blambeau
 */
public class DFARandomWalkInput extends DefaultRandomWalkInput {

	/** Input dfa; */
	private IDFA dfa;
	
	/** Extend strings? */
	protected boolean extend = true;
	
	/** Probability to get a positive path. */
	protected double posProba = 0.5;
	
	/** Tolerance on path lengths. */
	protected double tolerance = -1;
	
	/** Maximal admitted tries on extension. */
	protected int maxTry = 100;
	
	/** Creates an input instance. */
	public DFARandomWalkInput(IDFA dfa) {
		super(dfa.getGraph());
		this.dfa = dfa;
	}

	/** Install options. */
	@Override
	protected void installOptions() {
		super.installOptions();
		super.addOption("extend",false,Boolean.class,null);
		super.addOption("posProba",false,Double.class,null);
		super.addOption("tolerance", false, Double.class, null);
		super.addOption("maxTry", false, Integer.class, null);
	}

	/** Extend the strings? */
	public void setExtend(boolean extend) {
		this.extend = extend;
	}
	
	/** Sets the positive probability. */
	public void setPosProba(double d) {
		this.posProba = d;
	}
	
	/** Sets the tolerance on number of states. */
	public void setTolerance(double tolerance) {
		this.tolerance = tolerance;
	}

	/** Sets the maximum tries before failure. */
	public void setMaxTry(int maxTry) {
		this.maxTry = maxTry;
	}

	/** Overrided to return the initial state. */
	@Override
	public Object chooseRootVertex(IDirectedGraph g, Random r) {
		return dfa.getInitialState();
	}

	/** Returns path stop predicate. */
	public IPredicate<IDirectedGraphPath> getPathStopPredicate(final Random r) {
		if (!extend) { return super.getPathStopPredicate(r); } 
		
		return new IPredicate<IDirectedGraphPath>() {
			
			/** Should stop on current string? */
			private boolean shouldStop = false;
			
			/** Generate a positive string? */
			private Boolean positive = null;
			
			/** Tries. */
			private int tries = 0;
			
			/** Returns true if it time to stop according to parent
			 *  heuristic. */
			private boolean shouldStop(IDirectedGraphPath path) {
				if (!shouldStop) {
					if (pathStopProba != -1) {
						shouldStop = r.nextDouble() <= pathStopProba;
					} else {
						shouldStop = path.size()-1 >= pathLength;
					}
				}
				return shouldStop;
			}
			
			/** Evaluates. */
			public boolean evaluate(IDirectedGraphPath path) {
				// should stop now ?
				boolean should = shouldStop(path);
				
				// return when should not or no extend strings
				if (!should || !extend) { return should; }
				
				// here: should stop and extend strings
				if (positive == null) { positive = r.nextDouble()<=posProba; }
				Object last = path.getLastVertex();
				
				// extract last state kind and compute ok
				FAStateKind kind = dfa.getStateKind(last);
				boolean isOk = positive ?
						       FAStateKind.ACCEPTING.equals(kind) :
						       !FAStateKind.ACCEPTING.equals(kind);

				if (!isOk) {
					isOk = complete(path, last, positive);
				}
						       
				// stop generation when ok or failed by tries
                if (isOk || (++tries > maxTry)) { 
                	shouldStop = false;
                	positive = null;
                	tries = 0;
                	return true; 
                }
                return false;
			}
			
			/** Completes the path. */
			private boolean complete(IDirectedGraphPath path, Object last, boolean positive) {
				if (path instanceof DefaultDirectedGraphPath == false) {
					throw new AssertionError("DefaultDirectedGraphPath expected.");
				}
				DefaultDirectedGraphPath dpath = (DefaultDirectedGraphPath) path;
				
				// complete positively by finding an (non) accepting
				// state in neighbourhood
				for (Object edge: graph.getOutgoingEdges(last)) {
					Object target = graph.getEdgeTarget(edge);
					FAStateKind targetKind = dfa.getStateKind(target);
					boolean accepting = FAStateKind.ACCEPTING.equals(targetKind);
					if (positive == accepting) {
						dpath.addEdge(edge);
						return true;
					} 
				}

				// reaching here => no such neighbour found.
				return false;
			}
			
		};
	}

}
