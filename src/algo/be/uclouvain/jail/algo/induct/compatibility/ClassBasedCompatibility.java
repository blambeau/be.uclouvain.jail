package be.uclouvain.jail.algo.induct.compatibility;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import be.uclouvain.jail.algo.commons.Avoid;
import be.uclouvain.jail.algo.graph.utils.UnionFind;
import be.uclouvain.jail.algo.induct.internal.InductionAlgo;
import be.uclouvain.jail.algo.induct.internal.PTAEdge;
import be.uclouvain.jail.algo.induct.internal.PTAState;
import be.uclouvain.jail.algo.induct.internal.Simulation;
import be.uclouvain.jail.algo.induct.listener.IInductionListener;
import be.uclouvain.jail.algo.induct.listener.InductionListenerHelper;
import be.uclouvain.jail.algo.induct.processor.ForwardLabelProcessor;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.functions.SetUnionFunction;

/**
 * Provides a compatibility informer based on state class.
 * 
 * @author blambeau
 */
public class ClassBasedCompatibility extends AbstractCompatibility {

	/** Graph underlying PTA. */
	private IDirectedGraph ptag;
	
	/** State class attribute. */
	private String classAttr;
	
	/** Union find used to keep inconsistencies. */
	private UnionFind<Set<Integer>> ufds;
	
	/** Merge function. */
	private SetUnionFunction<Integer> mergeFunction = new SetUnionFunction<Integer>() {

		/** Builds a TreeSet. */
		@Override
		protected Set<Integer> emptySet() {
			return new TreeSet<Integer>();
		}
		
	};
	
	/** Tracker. */
	private IncompatibilityTracker tracker; 
	
	/** Creates a compatibility layer instance. */
	public ClassBasedCompatibility(String classAttr) {
		assert (classAttr != null) : "Representor attribute has been set.";
		this.classAttr = classAttr;
		tracker = new IncompatibilityTracker();
	}
	
	/** Creates a compatibility layer instance. */
	public ClassBasedCompatibility() {
		this(ForwardLabelProcessor.DEFAULT_TARGET_ATTR);
	}

	/** Extracts clazz of a state in the pta. */
	private int extractPTAClass(Object state) {
		IUserInfo info = ptag.getVertexInfo(state);
		Object clazzO = info.getAttribute(classAttr);
		assert (clazzO instanceof Integer) : "Valid label pre-processor applied.";
		return (Integer) clazzO;
	}
	
	/** Returns the tracker. */
	public IInductionListener getTracker() {
		return tracker;
	}
	
	/** Initializes the layer. */
	public void initialize(InductionAlgo algo) {
		IDFA pta = algo.getPTA();
		this.ptag = pta.getGraph();
		
		// compute size
		int size = 0;
		for (Object state: pta.getGraph().getVertices()) {
			int clazz = extractPTAClass(state);
			if (clazz>size) { size = clazz; }
		}
		size += 1;
		
		// create union find
		ufds = new UnionFind<Set<Integer>>(size);
		tracker.initialize(algo);
	}

	/** Find incompatibilities of i. */
	private Set<Integer> findIncompatibilities(int i, boolean create) {
		Set<Integer> inc = ufds.find(i);
		if (inc == null && create) {
			inc = new HashSet<Integer>();
			ufds.setMember(i,inc);
		}
		return inc;
	}

	/** Checks disjointness of two sets. */
	protected boolean isDisjoint(Set<Integer> i, Set<Integer> j) {
		assert (i instanceof TreeSet) : "TreeSet hypothesis.";
		assert (j instanceof TreeSet) : "TreeSet hypothesis.";

		// take two iterators
		Iterator<Integer> it = i.iterator();
		Iterator<Integer> jt = j.iterator();
		
		// one empty => disjoint
		if (!it.hasNext() || !jt.hasNext()) { return true; }
		
		// current integers
		Integer ii = it.next();
		Integer jj = jt.next();
		if (ii.equals(jj)) { return false; }
		
		// iterate sets
		while (it.hasNext() && jt.hasNext()) {
			if (ii<jj) { ii = it.next(); } 
			else { jj = jt.next(); }
			if (ii.equals(jj)) { return false; }
		}
		return true;
	}
	
	/** Checks if incompatibilities of i and j are disjoint. */
	private boolean isDisjoint(int i, int j) {
		Set<Integer> iSet = ufds.merge(i, mergeFunction);
		Set<Integer> jSet = ufds.merge(j, mergeFunction);
		return isDisjoint(iSet,jSet);
	}
	
	/** Checks if two states are compatible. */
	public boolean isCompatible(int i, int j) {
		if (i == j || ufds.inSameBlock(i,j)) { return true; }
		Set<Integer> iSet = ufds.merge(Math.min(i, j), mergeFunction);
		return !iSet.contains(Math.max(i,j));
	}
	
	/** Checks if two states are compatible. */
	public boolean isCompatible(Object s, Object t) {
		int i = extractPTAClass(s);
		int j = extractPTAClass(t);
		if (ufds.inSameBlock(i,j)) { return true; }
		return isCompatible(i,j);
	}

	/** Returns true. */
	public boolean isExtensible() {
		return true;
	}

	/** Marks two classes as incompatible. */
	public void markAsIncompatible(int i, int j) {
		assert (i != j) : "Label never incompatible with itself.";
		System.out.println("Mark as incompatible (" + i + "," + j + ")");
		Set<Integer> inc = findIncompatibilities(Math.min(i, j), true);
		inc.add(Math.max(i,j));
	}
	
	/** Marks two states as incompatibles. */
	public void markAsIncompatible(Object s, Object t) {
		int i = extractPTAClass(s);
		int j = extractPTAClass(t);
		markAsIncompatible(i,j);
		assert (!isCompatible(s,t)) : "s and t are now incompatible";
	}

	/** Tracks incompatibilities. */
	public class IncompatibilityTracker extends InductionListenerHelper {
		
		/** Consolidated classes. */
		private Set<Integer> consolidated;
		
		/** Creates a tracker instance. */
		public IncompatibilityTracker() {
			super(classAttr);
			assert (classAttr != null) : "Representor attribute has been set.";
		}
		
		/** Initializes the tracker. */
		public void initialize(InductionAlgo algo) {
			this.consolidated = new HashSet<Integer>();
		}

		/** On state consolidation ... */
		public void consolidate(PTAState state) {
			int clazz = super.oStateIndex(state);
			
			// a state with same class has been consolidated
			// this should never happen by construction, the Avoid will not 
			// be catched by the induction algo 
			if (consolidated.contains(clazz)) {
				throw new Avoid();
			}
			
			// update incompatibilities of consolidated states
			for (int i: consolidated) {
				markAsIncompatible(i,clazz);
			}
			consolidated.add(clazz);
		}
		
		/** On new try ... */
		public void startTry(PTAEdge edge, Object kState) {
			// start transaction on UnionFind
			ufds.startTransaction();
		}

		/** On try commit ... */
		public void commit(Simulation simu) {
			// commit transaction on UnionFind
			if (simu.isRealTry()) { ufds.commit(); }
		}

		/** On try rollback ... */
		public void rollback(Simulation simu, boolean incompatibility) {
			// rollback transaction on UnionFind
			if (simu.isRealTry()) { ufds.rollback(); }
		}

		/** Merges two states. */
		protected void merge(int i, int j) {
			if (!isCompatible(i,j)) {
				throw new Avoid();
			}
			System.out.println("\t\tMerging " + i + " " + j + " :: " + isCompatible(i,j));
			ufds.union(i,j);
		}
		
		/** On merge ... */
		public void merge(PTAState victim, Object target) {
			int iClass = oStateIndex(victim);
			int jClass = kStateIndex(target);
			merge(iClass,jClass);
		}

		/** On merge ... */
		public void merge(PTAState victim, PTAState target) {
			int iClass = oStateIndex(victim);
			int jClass = oStateIndex(target);
			merge(iClass,jClass);
		}

	}
	
}
