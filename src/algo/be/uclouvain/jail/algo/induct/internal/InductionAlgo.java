package be.uclouvain.jail.algo.induct.internal;

import be.uclouvain.jail.algo.commons.Avoid;
import be.uclouvain.jail.algo.commons.Restart;
import be.uclouvain.jail.algo.induct.open.ICompatibility;
import be.uclouvain.jail.algo.induct.open.IOracle;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.constraints.PTAGraphConstraint;
import be.uclouvain.jail.fa.impl.GraphDFA;
import be.uclouvain.jail.uinfo.UserInfoAggregator;

/** Base implementation of RPNI-like induction algorithms. */
public abstract class InductionAlgo {

	/** Algorithm informations. */
	protected IInductionAlgoInput input;

	/** Source PTA. */
	protected IDFA pta;

	/** Target DFA under construction. */
	protected IDFA dfa;

	/** Fringe information. */
	protected Fringe fringe;

	/** Values handler. */
	protected IValuesHandler valuesHandler;

	/** Compatibility information (chain). */
	protected ICompatibility compatibility;

	/** Creates an algorithm instance. */
	public InductionAlgo() {
	}

	/** Executes the algorithm. */
	public IDFA execute(IInductionAlgoInput info) throws Avoid {
		this.input = info;
		do {
			try {
				initialize();
				mainLoop();
			} catch (Restart restart) {
				continue;
			}
			return dfa;
		} while (true);
	}

	/** Returns the input info. */
	public IInductionAlgoInput getInfo() {
		return input;
	}
	
	/** Initializes the algorithm. */
	private void initialize() throws Avoid {
		// create PTA
		this.pta = (IDFA) input.getInput().adapt(IDFA.class);
		assert new PTAGraphConstraint().isRespectedBy(this.pta.getGraph()) : "Valid input PTA.";
		
		// create target DFA
		this.dfa = new GraphDFA(pta.getAlphabet());
		
		// compute and initialize compatibility information
		compatibility = input.getCompatibility();
		if (compatibility != null) {
			compatibility.initialize(this);
		}
		
		// create a real values handler
		valuesHandler = new RValuesHandler(this);
		
		// create initial situation: root is red, fringe updated
		createInitialSituation();
	}

	/** Creates the initial situation, i.e. PTA root is consolidated
	 * and fringe is created with root neighbourns. */ 
	private void createInitialSituation() {
		// create fringe
		fringe = new Fringe(this);

		// consolidate pta initial state (creation of PTA rootState 
		// creates the whole PTA decorator as a side effect)
		Object ptaInitial = pta.getInitialState();
		PTAState rootState = new PTAState(this, pta, ptaInitial, null);
		rootState.consolidate(this);
	}

	/** Returns current solution. */
	public IDFA getDFA() {
		return dfa;
	}

	/** Returns source PTA. */
	public IDFA getPTA() {
		return pta;
	}

	/** Returns fringe instance. */
	protected Fringe getFringe() {
		return fringe;
	}

	/** Returns the values handler. */
	protected IValuesHandler getValuesHandler() {
		return valuesHandler;
	}

	/** Returns used state functions. */
	protected UserInfoAggregator getStateAggregator() {
		return input.getStateAggregator();
	}

	/** Returns used edge functions. */
	protected UserInfoAggregator getEdgeAggregator() {
		return input.getEdgeAggregator();
	}

	/** Checks if two DFA states are compatible. */
	protected boolean isCompatible(Object pta1, Object pta2) {
		if (compatibility == null) { return true; }
		return compatibility.isCompatible(pta1, pta2);
	}

	/** Checks if two PTA states are compatible. */
	protected boolean isCompatible(PTAState pta1, PTAState pta2) {
		if (compatibility == null) { return true; }
		return isCompatible(pta1.representor(), pta2.representor());
	}

	/** Checks if a kernel and a fringe state are compatible. */
	protected boolean isCompatible(Object kState, PTAState fState) {
		if (compatibility == null) { return true; }
		Object kStateRep = MappingUtils.sRepresentor(this, kState);
		Object fStateRep = fState.representor();
		assert (kStateRep != null && fStateRep != null) : "Both nodes have representors.";
		return isCompatible(kStateRep, fStateRep);
	}

	/** Checks if a kernel state is compatible with the target of a fringe edge. */ 
	protected boolean isCompatible(Object kState, PTAEdge fEdge) {
		if (compatibility == null) { return true; }
		return isCompatible(kState, fEdge.target());
	}

	/** Checks a simulation with the installed oracle. */
	public void checkWithOracle(Simulation simu) throws Avoid, Restart {
		IOracle oracle = input.getOracle();
		if (oracle != null && !oracle.accept(simu)) {
			throw new Avoid();
		} else {
			return;
		}
	}

	/** To be implemented by subclasses. */
	protected abstract void mainLoop() throws Restart;

}
