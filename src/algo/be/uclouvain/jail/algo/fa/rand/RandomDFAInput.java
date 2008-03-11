package be.uclouvain.jail.algo.fa.rand;

import be.uclouvain.jail.algo.graph.rand.DefaultRandomGraphInput;
import be.uclouvain.jail.algo.graph.rand.IRandomGraphInput;
import be.uclouvain.jail.fa.IAlphabet;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.constraints.DFAGraphConstraint;
import be.uclouvain.jail.fa.constraints.NoDeadlockGraphConstraint;
import be.uclouvain.jail.fa.impl.GraphDFA;
import be.uclouvain.jail.fa.utils.DFAQueryable;
import be.uclouvain.jail.fa.utils.IntegerAlphabet;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.IGraphPredicate;
import be.uclouvain.jail.graph.constraints.AbstractGraphPredicate;

/**
 * Provides an {@link IRandomGraphInput} implementation to generate
 * DFAs.
 * 
 * @author blambeau
 */
public class RandomDFAInput extends DefaultRandomGraphInput {

	/** State count multiplication factor. */
	private double stateMultFactor = 1.62;
	
	/** Accepting probability. */
	protected double accepting = 0.5;
	
	/** Maximal out degree. */
	protected int maxOutDegree = 4;
	
	/** Tolerance on stateCount. */
	protected double tolerance = 0.1;
	
	/** Make a depth control? */
	protected boolean depthControl = false;
	
	/** Alphabet size to generate. */
	protected IAlphabet<?> alphabet = new IntegerAlphabet(2);
	
	/** Installs the options. */
	@Override
	protected void installOptions() {
		super.installOptions();
		super.addOption("stateMultFactor", false, Double.class, null);
		super.addOption("accepting", false, Double.class, null);
		super.addOption("maxOutDegree", false, Integer.class, null);
		super.addOption("tolerance", false, Double.class, null);
		super.addOption("alphabetSize", false, Integer.class, null);
		super.addOption("depthControl", false, Boolean.class, null);
	}

	/** Sets state count multiplication factor. */
	public void setStateMultFactor(double stateMultFactor) {
		this.stateMultFactor = stateMultFactor;
	}
	
	/** Sets the probability to get an accepting state. */
	public void setAccepting(double accepting) {
		this.accepting = accepting;
	}

	/** Sets the tolerance on number of states. */
	public void setTolerance(double tolerance) {
		this.tolerance = tolerance;
	}

	/** Sets a user-defined alphabet size. */
	public void setAlphabetSize(int size) {
		this.alphabet = new IntegerAlphabet(size);
	}
	
	/** Sets maximal output degree of states. */
	public void setMaxOutDegree(int max) {
		this.maxOutDegree = max;
	}
	
	/** Sets depth control. */
	public void setDepthControl(boolean depthControl) {
		this.depthControl = depthControl;
	}

	/** Accepts the DFA if DFAGraphConstraint accepts it. */
	public IGraphPredicate getAcceptPredicate() {
		return new AbstractGraphPredicate() {
			@Override
			public boolean evaluate(IDirectedGraph graph) {
				// check that it's a DFA
				if (!new DFAGraphConstraint().isRespectedBy(graph)) {
					System.out.println("Not a DFA.");
					return false;
				}
				
				// check no deadlock
				if (!new NoDeadlockGraphConstraint().isRespectedBy(graph)) {
					return false;
				}
				
				// check tolerance if set
				if (tolerance != -1.0) {
					double wish = stateCount;
					double actual = graph.getVerticesTotalOrder().size();
					double diff = Math.abs(wish-actual)/wish;
					if (diff >= tolerance) { 
						return false; 
					}
				}
				
				// check depth if required
				if (depthControl) {
					IDFA dfa = new GraphDFA(graph);
					DFAQueryable queried = new DFAQueryable(dfa);
					int depth = queried.getDepth();
					long expected = Math.round((2*Math.log(stateCount)/Math.log(2)) - 2);
					if (depth != expected) {
						return false;
					}
				}
				
				// seems ok
				return true;
			}
		};
	}

	/** Returns true when created states reach stateCount. */
	public IGraphPredicate getVertexStopPredicate() {
		return new AbstractGraphPredicate() {
			public boolean evaluate(IDirectedGraph graph) {
				return graph.getVerticesTotalOrder().size() == 
				   new Double(stateCount*stateMultFactor).intValue();
			}
		};
	}

	/** Stops edge creation when... */
	public IGraphPredicate getEdgeStopPredicate() {
		return new AbstractGraphPredicate() {
			public boolean evaluate(IDirectedGraph g) {
				// actual state count
				int st = new Double(stateCount*stateMultFactor).intValue();
				// alphabet size
				int as = alphabet.getLetters().size();
				// maximal out degree
				int maxd = maxOutDegree;
				// current number of edges
				int ec = g.getEdgesTotalOrder().size();
				// maximal number of edges
				int maxe = st * Math.min(as, maxd);
				// at least one half of all possible edges reached?
				if (ec >= maxe/2) {
					return true;
				} else {
					return false;
				}
			}
		};
	}

}
