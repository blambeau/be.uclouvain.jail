package be.uclouvain.jail.algo.fa.rand;

import be.uclouvain.jail.algo.graph.rand.DefaultRandomGraphInput;
import be.uclouvain.jail.algo.graph.rand.IRandomGraphInput;
import be.uclouvain.jail.fa.IAlphabet;
import be.uclouvain.jail.fa.constraints.DFAGraphConstraint;
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
	
	/** Accepts the DFA if DFAGraphConstraint accepts it. */
	public IGraphPredicate getAcceptPredicate() {
		return new AbstractGraphPredicate() {
			@Override
			public boolean evaluate(IDirectedGraph graph) {
				// check that it's a DFA
				if (!new DFAGraphConstraint().isRespectedBy(graph)) {
					return false;
				}
				
				// check tolerance if set
				if (tolerance != -1.0) {
					double wish = stateCount;
					double actual = graph.getVerticesTotalOrder().size();
					double diff = Math.abs(wish-actual)/wish;
					return diff<tolerance;
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
				int st = new Double(stateCount*stateMultFactor).intValue();
				int as = alphabet.getLetters().size();
				int maxd = maxOutDegree;
				int ec = g.getEdgesTotalOrder().size();
				int maxe = st * Math.min(as, maxd);
				if (ec >= maxe/2) {
					return true;
				} else {
					return false;
				}
			}
		};
	}

}
