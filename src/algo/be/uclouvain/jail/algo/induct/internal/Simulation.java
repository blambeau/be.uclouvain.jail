package be.uclouvain.jail.algo.induct.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.uclouvain.jail.algo.commons.Avoid;
import be.uclouvain.jail.algo.induct.open.ISimuVisitor;
import be.uclouvain.jail.algo.induct.utils.KStateGainD;
import be.uclouvain.jail.algo.induct.utils.KStateMergeD;
import be.uclouvain.jail.algo.induct.utils.OStateGainD;
import be.uclouvain.jail.algo.induct.utils.OStateMergeD;
import be.uclouvain.jail.algo.induct.utils.WorkDecorator;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.utils.ITotalOrder;
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

	/** Starting fringe edge. */
	private PTAEdge fringeEdge;

	/** Starting kernel state. */
	private Object targetKState;

	/** Kernel DFA version of the fringe edge (initial added edge). */
	private Object kEdge;

	/** Saved user info for rollbacked kEdge. */
	private IUserInfo kEdgeInfo;
	
	/** List of works in the simulation. */
	private List<AbstractSubWork> subWorks;

	/** Gained trannsitions on kernel states. */
	private Map<SLPair,Object> kStateGains;

	/** Gained transitions on other states. */
	private Map<SLPair,Object> oStateGains;

	/** Simulation has been commited ? */
	private boolean commited;

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
	protected Simulation(InductionAlgo algo, PTAEdge edge, Object tkState) {
		// initialize instance variables
		this.algo = algo;
		this.commited = false;
		this.dfa = algo.getDFA();
		this.dfag = dfa.getGraph();
		this.fringeEdge = edge;
		this.targetKState = tkState;
		
		// decorate real values handler with commit support
		handler = new CValuesHandler(algo, algo.getValuesHandler());
		
		// create data structures
		kStateGains = new HashMap<SLPair,Object>();
		oStateGains = new HashMap<SLPair,Object>();
		subWorks = new ArrayList<AbstractSubWork>();
	}

	/** Returns the parent algorithm. */
	public InductionAlgo getRunningAlgo() {
		return algo;
	}

	/** Returns the kernel DFA (under construction). */
	public IDFA getKernelDFA() {
		return dfa;
	}

	/** Returns the source PTA. */
	public IDFA getSourcePTA() {
		return algo.getPTA();
	}

	/** Returns the target kernel state. */
	public Object getTargetKState() {
		return targetKState;
	}

	/** Initializes the simulation. */
	protected void initialize() {
		Object letter = fringeEdge.letter();
		Object skState = fringeEdge.getSourceKernelState();
		
		// some checks
		assert (skState != null) : "Edge has been hooked (fringe edge!).";
		assert (algo.getFringe().fringeEdge(skState, letter) == fringeEdge) : "Works on a fringe edge.";
		assert (dfa.getOutgoingEdge(skState, letter) == null) : "No such edge on the source kernel state.";
		
		if (kEdgeInfo == null) {
			// kEdgeInfo is not null when work has been committed after a rollback
			// otherwise, we take the real edge info on decorated PTA
			kEdgeInfo = fringeEdge.getUserInfo(); 
		}

		// connect source kernel state with target one
		kEdge = dfag.createEdge(skState, targetKState, kEdgeInfo);
		handler.updateKEdge(kEdge, kEdgeInfo);
	}

	/** Commits the simulation. */
	protected void commit() {
		if (commited) {
			throw new IllegalStateException("Already commited.");
		}
		if (kEdge == null) {
			// happens when work has been rollbacked
			// and commited later (efficient blue-fringe case) 
			initialize();
		}
		
		// check that the new pair does not appear on the fringe now
		Object source = fringeEdge.getSourceKernelState();
		Object letter = fringeEdge.letter();
		SLPair pair = new SLPair(source, letter);
		assert (!kStateGains.containsKey(pair)) : "Commited edge does not appear on new fringe.";
		
		// remove pair from the fringe
		algo.getFringe().remove(source, letter);
		
		// commit all works
		for (AbstractSubWork work : subWorks) {
			work.commit();
		}

		// commit handler (flush all values)
		handler.commit(false);
		
		// mark as commited
		commited = true;
	}

	/** Rollbacks the simulation. */
	protected void rollback() {
		if (commited) {
			throw new IllegalStateException("Cannot rollback a commited work.");
		}
		
		// remove initial created edge
		// save last version of its info in kEdgeInfo
		// for late commit which happens in blue-fringe efficient case
		if (kEdge != null) {
			kEdgeInfo = handler.remKEdgeInfo(kEdge);
			dfag.removeEdge(kEdge);
			kEdge = null;
		}
	}

	/** Adds a subwork. */
	private void addSubWork(AbstractSubWork subWork) {
		subWorks.add(subWork);
	}

	/** Add a kernel state merge. */
	protected void addKStateMerge(PTAState state, Object tkState) throws Avoid {
		if (!algo.isCompatible(tkState, state)) {
			throw new Avoid();
		} else {
			addSubWork(new KStateMerge(state, tkState));
		}
	}

	/** Add a new kernel edge merge. */
	protected void addKEdgeMerge(PTAEdge edge, Object tkEdge) throws Avoid {
		addSubWork(new KEdgeMerge(edge, tkEdge));
	}

	/** Adds a kernel state gain. */
	protected void addKStateGain(Object tkState, PTAEdge ptaEdge) throws Avoid {
		SLPair pair = new SLPair(tkState, ptaEdge.letter());
		assert (!kStateGains.containsKey(pair)) : "Gain => not already gained.";
		addSubWork(new KStateGain(tkState, ptaEdge));
		kStateGains.put(pair, ptaEdge);
	}

	/** Adds an other state merge. */
	protected void addOStateMerge(PTAState victim, PTAState target) throws Avoid {
		if (!algo.isCompatible(victim, target)) {
			throw new Avoid();
		} else {
			addSubWork(new OStateMerge(victim, target));
		}
	}

	/** Adds another edge merge. */
	protected void addOEdgeMerge(PTAEdge victim, PTAEdge target) throws Avoid {
		addSubWork(new OEdgeMerge(victim, target));
	}

	/** Adds another state gain. */
	protected void addOStateGain(PTAState state, PTAEdge edge) {
		SLPair pair = new SLPair(state, edge.letter());
		assert (!oStateGains.containsKey(pair)) : "Gain => not already gained.";
		addSubWork(new OStateGain(state, edge));
		oStateGains.put(pair, edge);
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

	/** Accepts a visitor. */
	public void accept(ISimuVisitor visitor) {
		for (AbstractSubWork subWork: subWorks) { 
			visitor.onWork(this, subWork);
		}
	}

	/** Debugs the simulation. */
	public String debug() {
		final StringBuffer sb = new StringBuffer();
		final ITotalOrder<Object> order = dfag.getVerticesTotalOrder();
		sb.append("Simulation: edge is '")
		  .append(" -> ").append(fringeEdge.letter()).append(" -> ").append(fringeEdge.target())
		  .append("' on dfa '").append(order.indexOf(fringeEdge.getSourceKernelState()))
		  .append(" -> ").append(order.indexOf(targetKState))
		  .append("'\n");
		
		accept(new ISimuVisitor() {
			public void onWork(Simulation simu, IWork work) {
				sb.append("  ").append(WorkUtils.toString(work)).append("\n");
			}
		});
		return sb.toString();
	}

}
