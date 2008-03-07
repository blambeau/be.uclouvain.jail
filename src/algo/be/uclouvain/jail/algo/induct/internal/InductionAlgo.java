package be.uclouvain.jail.algo.induct.internal;

import be.uclouvain.jail.algo.commons.Avoid;
import be.uclouvain.jail.algo.commons.Restart;
import be.uclouvain.jail.algo.induct.compatibility.Compatibilities;
import be.uclouvain.jail.algo.induct.compatibility.ICompatibility;
import be.uclouvain.jail.algo.induct.extension.IInductionAlgoExtension;
import be.uclouvain.jail.algo.induct.listener.IInductionListener;
import be.uclouvain.jail.algo.induct.listener.InductionListeners;
import be.uclouvain.jail.algo.induct.oracle.IOracle;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.impl.GraphDFA;

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

	/** Compatibility information. */
	protected Compatibilities compatibility;

	/** Oracle to use. */
	protected IOracle oracle;
	
	/** Listener. */
	protected InductionListeners listener;
	
	/** Creates an algorithm instance. */
	public InductionAlgo() {
	}

	/** Executes the algorithm. */
	public IDFA execute(IInductionAlgoInput info) throws Avoid {
		this.input = info;

		// install extension if any
		IInductionAlgoExtension extension = input.getExtension();
		if (extension != null) {
			extension.install(this);
		}
		
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

		// create target DFA
		this.dfa = new GraphDFA(pta.getAlphabet());
		
		// initialize oracle if any
		oracle = input.getOracle();
		if (oracle != null) { oracle.initialize(this); }

		// create a real values handler
		valuesHandler = new RValuesHandler(this);
		
		// initialize extension if any
		IInductionAlgoExtension extension = input.getExtension();
		if (extension != null) {
			extension.initialize(this);
		}
		
		// create initial situation: root is red, fringe updated
		createInitialSituation();
	}

	/** Creates the initial situation, i.e. PTA root is consolidated
	 * and fringe is created with root neighbourns. */ 
	private void createInitialSituation() {
		// create fringe
		fringe = new Fringe(this);

		// creates the PTA decorator
		Object ptaInitial = pta.getInitialState();
		PTAState rootState = new PTAState(this, pta, ptaInitial, null);
		
		// consolidate pta initial state
		Simulation simu = new Simulation(this);
		simu.consolidate(rootState);
		simu.commit();
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

	/** Returns the compatibility informer. */
	public ICompatibility getCompatibility() {
		return compatibility;
	}
	
	/** Adds an incompatibility layer. */
	public void addCompatibility(ICompatibility...cs) {
		if (this.compatibility == null) { this.compatibility = new Compatibilities(); }
		for (ICompatibility c: cs) {
			this.compatibility.addCompatibility(c);
		}
	}
	
	/** Returns induction listener. */
	public IInductionListener getListener() {
		return listener;
	}

	/** Adds a listener. */
	public void addListener(IInductionListener...ls) {
		if (this.listener == null) { this.listener = new InductionListeners(); }
		for (IInductionListener l: ls) {
			this.listener.addListener(l);
		}
	}
	
	/** Returns induction listener. */
	public IOracle getOracle() {
		return oracle;
	}
	
	/** Debugs a pta state. */
	public String d(Object ptaState) {
		return pta.getGraph().getVertexInfo(ptaState).getAttribute("label").toString();
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
		if (oracle != null && !oracle.accept(simu)) {
			throw new Avoid();
		} else {
			return;
		}
	}

	/** To be implemented by subclasses. */
	protected abstract void mainLoop() throws Restart;

}
