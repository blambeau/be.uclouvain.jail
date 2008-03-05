package be.uclouvain.jail.algo.induct.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.uclouvain.jail.algo.commons.Avoid;
import be.uclouvain.jail.algo.induct.listener.IInductionListener;
import be.uclouvain.jail.algo.induct.utils.ISimuVisitor;
import be.uclouvain.jail.algo.induct.utils.KStateGainD;
import be.uclouvain.jail.algo.induct.utils.KStateMergeD;
import be.uclouvain.jail.algo.induct.utils.OStateGainD;
import be.uclouvain.jail.algo.induct.utils.OStateMergeD;
import be.uclouvain.jail.algo.induct.utils.WorkDecorator;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.uinfo.IUserInfo;

/** Encapsulates all steps to be done if a merge is selected. */
public class Simulation {

	/** Running algorithm. */
	private InductionAlgo algo;

	/** Target DFA (under construction). */
	private IDFA dfa;

	/** Underlying graph. */
	private IDirectedGraph dfag;
	
	/** Commitable values handler. */
	private CValuesHandler handler;

	/** List of works in the simulation. */
	private List<AbstractSubWork> subWorks;

	/** Gained trannsitions on kernel states. */
	private Map<SLPair,Object> kStateGains;

	/** Gained transitions on other states. */
	private Map<SLPair,Object> oStateGains;

	/** Simulation has been commited ? */
	private boolean commited;

	/** Listener. */
	protected IInductionListener listener;
	
	/** Helper to create works. */
	abstract class AbstractSubWork implements IWork {

		/** Returns the work type. */
		public WorkType type() {
			WorkType type = WorkType.valueOf(getClass().getSimpleName());
			assert (type != null) : "Work classes have the correct name.";
			return type;
		}

		/** Returns the parent simulation. */
		public Simulation simulation() {
			return Simulation.this;
		}

		/** Commits the work. */
		protected void commit() {
		}

		/** Rollbacks the work. */
		protected void rollback() {
		}

	}

	/** Work for initial merge choice. */
	class StartTry extends AbstractSubWork {
		
		/** Initial PTA edge. */
		private PTAEdge fEdge;
		
		/** Created kernel edge. */
		private Object kEdge;
		
		/** Kernel edge info. */
		private IUserInfo kEdgeInfo;
		
		/** Kernel state to go to. */
		private Object kState;
		
		/** Creates a startTry work instance. */
		public StartTry(PTAEdge fEdge, Object kState) {
			this.fEdge = fEdge;
			this.kState = kState;

			// create the edge
			kEdgeInfo = fEdge.getUserInfo();
			ensureKEdge();
		}
		
		/** Ensures creation of the kernel edge. */
		private void ensureKEdge() {
			if (kEdge == null) {
				kEdge = dfag.createEdge(fEdge.getSourceKernelState(), kState, kEdgeInfo);
				handler.updateKEdge(kEdge, kEdgeInfo);
			}
		}

		/** Commits the work. */
		public void commit() {
			// check that the new pair does not appear on the fringe now
			Object source = fEdge.getSourceKernelState();
			Object letter = fEdge.letter();
			SLPair pair = new SLPair(source, letter);
			assert (!kStateGains.containsKey(pair)) : "Commited edge does not appear on new fringe.";
			
			// ensure that kernel edge is created
			ensureKEdge();
			
			// remove pair from the fringe
			algo.getFringe().remove(source, letter);
		}
		
		/** Rollbacks the work. */
		public void rollback() {
			// remove initial created edge
			// save last version of its info in kEdgeInfo
			// for late commit which happens in blue-fringe efficient case
			kEdgeInfo = handler.remKEdgeInfo(kEdge);
			dfag.removeEdge(kEdge);
			kEdge = null;
		}
		
		/** Returns target kernel state. */
		public Object target() {
			return kState;
		}

		/** Returns the PTA edge. */
		public Object victim() {
			return fEdge;
		}
		
		/** Throws UnsupportedException. */
		public WorkDecorator decorate() {
			throw new UnsupportedOperationException("StartTry work does not have a decorator yet.");
		}

	}
	
	/** Consolidation work. */
	class StateConsolidate extends AbstractSubWork {
		
		/** Initial PTA state. */
		private PTAState victim;
		
		/** Consolidates an edge. */
		public StateConsolidate(PTAState victim) {
			this.victim = victim;
		}

		/** Returns target kernel state. */
		public Object target() {
			return null;
		}

		/** Returns the PTA edge. */
		public Object victim() {
			return victim;
		}
		
		/** Throws UnsupportedException. */
		public WorkDecorator decorate() {
			throw new UnsupportedOperationException("StartTry work does not have a decorator yet.");
		}

	}
	
	/** Consolidation work. */
	class EdgeConsolidate extends AbstractSubWork {
		
		/** Initial PTA edge. */
		private PTAEdge victim;
		
		/** Consolidates an edge. */
		public EdgeConsolidate(PTAEdge edge) {
			this.victim = edge;
		}

		/** Returns target kernel state. */
		public Object target() {
			return null;
		}

		/** Returns the PTA edge. */
		public Object victim() {
			return victim;
		}
		
		/** Throws UnsupportedException. */
		public WorkDecorator decorate() {
			throw new UnsupportedOperationException("StartTry work does not have a decorator yet.");
		}

	}
	
	/** Merge of another state with a kernel one. */
	class KStateMerge extends AbstractSubWork {

		/** Merge victim. */
		protected PTAState state;

		/** Target kernel state. */
		protected Object tkState;
		
		/** Returns the victim. */
		public Object victim() {
			return state;
		}

		/** Returns the target kernel state. */
		public Object target() {
			return tkState;
		}

		/** Creates a work instance. */
		public KStateMerge(PTAState state, Object tkState) throws Avoid {
			this.state = state;
			this.tkState = tkState;
			
			// update the kernel state values
			handler.updateKState(tkState, handler.mergeStateUserInfo(state,tkState));
		}

		/** Factors a KStateMergeD. */
		public WorkDecorator decorate() {
			return new KStateMergeD(this);
		}
		
	}

	/** Merge of two another states. */
	class OStateMerge extends AbstractSubWork {

		/** Merge victim. */
		protected PTAState victim;

		/** Merge target. */
		protected PTAState target;

		/** Returns the victim. */
		public Object victim() {
			return victim;
		}

		/** Returns the target. */
		public Object target() {
			return target;
		}

		/** Creates a work instance. */
		public OStateMerge(PTAState victim, PTAState target) throws Avoid {
			this.victim = victim;
			this.target = target;
			
			// update the kernel state values
			handler.updateOState(target, handler.mergeStateUserInfo(victim,target));
		}
		
		/** Factors a OStateMergeD. */
		public WorkDecorator decorate() {
			return new OStateMergeD(this);
		}
		
	}

	/** Merge of another edge with a kernel one. */
	class KEdgeMerge extends AbstractSubWork {

		/** Merge victim. */
		protected PTAEdge edge;

		/** Target kernel edge. */
		protected Object tkEdge;

		/** Returns the victim. */
		public Object victim() {
			return edge;
		}

		/** Returns the target kernel edge. */
		public Object target() {
			return tkEdge;
		}

		/** Creates a work instance. */
		public KEdgeMerge(PTAEdge edge, Object tkEdge) throws Avoid {
			this.edge = edge;
			this.tkEdge = tkEdge;
			
			// update the kernel edge values
			handler.updateKEdge(tkEdge, handler.mergeEdgeUserInfo(edge, tkEdge));
		}

		/** Factors a KEdgeMergeD. */
		public WorkDecorator decorate() {
			throw new UnsupportedOperationException("No such decorator (KEdgeMergeD)");
		}
		
	}

	/** Merge of two another edges. */
	class OEdgeMerge extends AbstractSubWork {

		/** Merge victim. */
		protected PTAEdge victim;

		/** Target kernel edge. */
		protected PTAEdge target;

		/** Returns the victim. */
		public Object victim() {
			return victim;
		}

		/** Returns the kernel edge. */
		public Object target() {
			return target;
		}

		/** Creates a work instance. */
		public OEdgeMerge(PTAEdge victim, PTAEdge target) throws Avoid {
			this.victim = victim;
			this.target = target;
			
			// update the kernel edge values.
			handler.updateOEdge(target, handler.mergeEdgeUserInfo(victim, target));
		}
		
		/** Factors a OEdgeMergeD. */
		public WorkDecorator decorate() {
			throw new UnsupportedOperationException("No such decorator (OEdgeMergeD)");
		}
		
	}

	/** Kernel state outgoing transition gain. */
	class KStateGain extends AbstractSubWork {

		/** Target kernel state. */
		protected Object tkState;

		/** Gained edge. */
		protected PTAEdge ptaEdge;

		/** Commits the gain. */
		protected void commit() {
			// add new edge on the fringe
			algo.getFringe().add(tkState, ptaEdge);
		}

		/** Returns the merge victim, i.e. the gained edge. */ 
		public Object victim() {
			return ptaEdge;
		}

		/** Returns the kernel state. */
		public Object target() {
			return tkState;
		}

		/** Creates a work instance. */
		public KStateGain(Object tkState, PTAEdge ptaEdge) {
			this.tkState = tkState;
			this.ptaEdge = ptaEdge;
		}
		
		/** Factors a KStateGainD. */
		public WorkDecorator decorate() {
			return new KStateGainD(this);
		}
		
	}

	/** Other state outgoing transition gain. */
	class OStateGain extends AbstractSubWork {

		/** Other state. */
		protected PTAState state;

		/** Gained edge. */
		protected PTAEdge edge;

		/** Commits the work. */
		protected void commit() {
			// add gained edge on target state
			state.addOutEdge(edge);
		}

		/** Returns the victim, i.e. gained edge. */
		public Object victim() {
			return edge;
		}

		/** Returns the target state. */
		public Object target() {
			return state;
		}

		/** Creates a work instance. */
		public OStateGain(PTAState state, PTAEdge edge) {
			this.state = state;
			this.edge = edge;
		}
		
		/** Factors a OStateGainD. */
		public WorkDecorator decorate() {
			return new OStateGainD(this);
		}
		
	}

	/** Creates a simulation instance with initial merge info. */
	protected Simulation(InductionAlgo algo) {
		// initialize instance variables
		this.algo = algo;
		this.commited = false;
		this.dfa = algo.getDFA();
		this.dfag = dfa.getGraph();
		this.listener = algo.getListener();
		
		// decorate real values handler with commit support
		handler = new CValuesHandler(algo, algo.getValuesHandler());
		
		// create data structures
		kStateGains = new HashMap<SLPair,Object>();
		oStateGains = new HashMap<SLPair,Object>();
		subWorks = new ArrayList<AbstractSubWork>();
		
		if (listener != null) { listener.newStep(this); }
	}

	/** Returns the parent algorithm. */
	public InductionAlgo getRunningAlgo() {
		return algo;
	}
	
	/** Returns the values handler. */
	public IValuesHandler getValuesHandler() {
		return handler;
	}
	
	/** Returns the fringe. */
	public Fringe getFringe() {
		return algo.getFringe();
	}

	/** Returns the kernel DFA (under construction). */
	public IDFA getKernelDFA() {
		return dfa;
	}

	/** Returns the source PTA. */
	public IDFA getSourcePTA() {
		return algo.getPTA();
	}

	/** Starts a merge try. */
	protected void startTry(PTAEdge fEdge, Object kState) {
		// check
		assert (subWorks.isEmpty()) : "No previous work.";

		// create work
		addSubWork(new StartTry(fEdge,kState));
		
		// let listener follow
		if (listener != null) { listener.startTry(fEdge, kState); }

		// continue
		fEdge.target().merge(Simulation.this,kState);
	}
	
	/** Consolidates an edge. */
	protected Object consolidate(PTAEdge fEdge) {
		// check
		assert (subWorks.isEmpty()) : "No previous work.";
		
		// create work
		addSubWork(new EdgeConsolidate(fEdge));
		
		// let listener follow
		if (listener != null) { listener.consolidate(fEdge); }
		
		// continue
		return fEdge.consolidate(this);
	}
	
	/** Consolidates an edge. */
	protected Object consolidate(PTAState state) {
		// check
		assert (subWorks.isEmpty()) : "No previous work.";
		
		// create work
		addSubWork(new StateConsolidate(state));
		
		// let listener follow
		if (listener != null) { listener.consolidate(state); }
		
		// continue
		return state.consolidate(this);
	}
	
	/** Add a kernel state merge. */
	protected void merge(PTAState state, Object tkState) throws Avoid {
		// create work
		addSubWork(new KStateMerge(state, tkState));
		
		// let listener follow
		if (listener != null) { listener.merge(state, tkState); }
	}

	/** Add a new kernel edge merge. */
	protected void merge(PTAEdge edge, Object tkEdge) throws Avoid {
		// create work
		addSubWork(new KEdgeMerge(edge, tkEdge));
		
		// let listener follow
		if (listener != null) { listener.merge(edge, tkEdge); }
	}

	/** Adds another edge merge. */
	protected void merge(PTAEdge victim, PTAEdge target) throws Avoid {
		// create work
		addSubWork(new OEdgeMerge(victim, target));
		
		// let listener follow
		if (listener != null) { listener.merge(victim, target); }
	}

	/** Adds an other state merge. */
	protected void merge(PTAState victim, PTAState target) throws Avoid {
		// create work
		addSubWork(new OStateMerge(victim, target));
		
		// let listener follow
		if (listener != null) { listener.merge(victim, target); }
	}

	/** Adds a kernel state gain. */
	protected void gain(Object tkState, PTAEdge ptaEdge) throws Avoid {
		// check
		SLPair pair = new SLPair(tkState, ptaEdge.letter());
		assert (!kStateGains.containsKey(pair)) : "Gain => not already gained.";

		// create work
		addSubWork(new KStateGain(tkState, ptaEdge));
		kStateGains.put(pair, ptaEdge);
		
		// let listener follow
		if (listener != null) { listener.gain(tkState, ptaEdge); }
	}

	/** Adds another state gain. */
	protected void gain(PTAState state, PTAEdge edge) {
		// check
		SLPair pair = new SLPair(state, edge.letter());
		assert (!oStateGains.containsKey(pair)) : "Gain => not already gained.";

		// create work
		addSubWork(new OStateGain(state, edge));
		oStateGains.put(pair, edge);
		
		// let listener follow
		if (listener != null) { listener.gain(state, edge); }
	}

	/** Commits the simulation. */
	protected void commit() {
		assert (!commited) : "Already commited.";
		
		// commit all works
		for (AbstractSubWork work : subWorks) {
			work.commit();
		}

		// commit handler
		handler.commit();
		if (listener != null) { listener.commit(this); }
		
		// mark as commited
		commited = true;
	}

	/** Rollbacks the simulation. */
	protected void rollback() {
		assert (!commited) : "Cannot rollback a commited work.";
		
		// commit all works
		for (AbstractSubWork work : subWorks) {
			work.rollback();
		}

		// commit handler
		handler.rollback();
		if (listener != null) { listener.rollback(this); }
	}

	/** Adds a subwork. */
	private void addSubWork(AbstractSubWork subWork) {
		subWorks.add(subWork);
	}

	/** Returns a kernel state gain. */
	protected PTAEdge getKStateGain(Object tkState, Object letter) {
		SLPair pair = new SLPair(tkState, letter);
		return (PTAEdge) kStateGains.get(pair);
	}

	/** Returns another state gain. */
	protected PTAEdge getOStateGain(PTAState oState, Object letter) {
		SLPair pair = new SLPair(oState, letter);
		return (PTAEdge) oStateGains.get(pair);
	}

	/** Checks if the job is a real try. */
	public boolean isRealTry() {
		return WorkType.StartTry.equals(subWorks.get(0).type());
	}

	/** Returns the StartTry work. */
	public IWork getStartTry() {
		IWork work = subWorks.get(0);
		if (WorkType.StartTry.equals(work.type())) {
			return work;
		} else {
			return null;
		}
	}
	
	/** Accepts a visitor. */
	public void accept(ISimuVisitor visitor) {
		for (AbstractSubWork subWork: subWorks) { 
			visitor.onWork(this, subWork);
		}
	}

}
