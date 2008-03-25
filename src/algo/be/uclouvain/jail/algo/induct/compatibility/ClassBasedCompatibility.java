package be.uclouvain.jail.algo.induct.compatibility;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import be.uclouvain.jail.algo.commons.Avoid;
import be.uclouvain.jail.algo.fa.walk.PTADepthFirstWalker;
import be.uclouvain.jail.algo.fa.walk.PTADepthFirstWalker.VisitorAdapter;
import be.uclouvain.jail.algo.graph.utils.UnionFind;
import be.uclouvain.jail.algo.induct.internal.InductionAlgo;
import be.uclouvain.jail.algo.induct.internal.PTAEdge;
import be.uclouvain.jail.algo.induct.internal.PTAState;
import be.uclouvain.jail.algo.induct.internal.SLPair;
import be.uclouvain.jail.algo.induct.internal.Simulation;
import be.uclouvain.jail.algo.induct.listener.IInductionListener;
import be.uclouvain.jail.algo.induct.listener.InductionListenerHelper;
import be.uclouvain.jail.algo.induct.processor.ForwardLabelProcessor;
import be.uclouvain.jail.algo.induct.utils.StartTryD;
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
	
	/** Find all incompatibilities of a state label. */
	public Set<Integer> incompatibilities(int i) {
		return ufds.merge(i, mergeFunction);
	}
	
	/** Fins all incompatibilities of a set of labels. */
	public Set<Integer> incompatibilities(Set<Integer> s) {
		Set<Integer> inc = new HashSet<Integer>();
		for (Integer i: s) {
			inc.addAll(incompatibilities(i));
		}
		return inc;
	}
	
	/** Checks if two states are compatible. */
	public boolean isCompatible(int i, int j) {
		if (i == j || ufds.inSameBlock(i,j)) { return true; }

		Set<Integer> iSet = ufds.merge(i, mergeFunction);
		if (iSet.contains(j)) { return false; }
		
		Set<Integer> jSet = ufds.merge(j, mergeFunction);
		if (jSet.contains(i)) { return false; }
		
		return true;
	}
	
	/** Checks if two states are compatible. */
	public boolean isCompatible(Object s, Object t) {
		int i = ufds.findi(extractPTAClass(s));
		int j = ufds.findi(extractPTAClass(t));
		return isCompatible(i,j);
	}

	/** Returns true. */
	public boolean isExtensible() {
		return true;
	}

	/** Marks two classes as incompatible. */
	private void markAsIncompatible(int i, int j) {
		assert (i != j) : "Label never incompatible with itself.";
		//wSystem.out.println("Mark as incompatible (" + i + "," + j + ")");
		findIncompatibilities(i, true).add(j);
		findIncompatibilities(j, true).add(i);
	}
	
	/** Marks two states as incompatibles. */
	public void markAsIncompatible(Object s, Object t) {
		int i = ufds.findi(extractPTAClass(s));
		int j = ufds.findi(extractPTAClass(t));
		markAsIncompatible(i,j);
		assert (!isCompatible(s,t)) : "s and t are now incompatible";
	}

	/** Returns a string rep. */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int i=0; i<ufds.size(); i++) {
			if (i!=0) { sb.append("\n"); }
			sb.append(i + ":")
		      .append(ufds.find(i));
		}
		return sb.toString();
	}
	
	/** Tracks incompatibilities. */
	public class IncompatibilityTracker extends InductionListenerHelper {
		
		/** Consolidated classes. */
		private Set<Integer> consolidated;
		
		/** Reverse delta. */
		private Map<SLPair, Set<Integer>> reverse;
		
		/** Creates a tracker instance. */
		public IncompatibilityTracker() {
			super(classAttr);
			assert (classAttr != null) : "Representor attribute has been set.";
		}
		
		/** Initializes the tracker. */
		public void initialize(InductionAlgo algo) {
			this.consolidated = new HashSet<Integer>();
			this.reverse = new HashMap<SLPair,Set<Integer>>();
			buildReverse(algo);
		}
		
		/** Computes reverse delta. */
		private Set<Integer> reverse(Integer target, Object letter, boolean create) {
			SLPair pair = new SLPair(target,letter);
			Set<Integer> set = reverse.get(pair);
			if (set == null && create) {
				set = new HashSet<Integer>();
				reverse.put(pair, set);
			}
			return set;
		}
		
		/** Reverse the incompatibilities. */
		public Set<Integer> rDelta(Set<Integer> indexes, Object letter) {
			Set<Integer> rDelta = new HashSet<Integer>();
			for (Integer i: indexes) {
				Set<Integer> r = reverse(i, letter, false);
				if (r != null) {
					rDelta.addAll(r);
				}
			}
			return rDelta;
		}
		
		/** Builds delta-reverse map. */
		private void buildReverse(InductionAlgo algo) {
			final IDFA pta = algo.getPTA();
			new PTADepthFirstWalker(pta).execute(new VisitorAdapter() {

				/** Returns the index of a state. */
				private Integer indexOf(Object state) {
					return (Integer) ptag.getVertexInfo(state).getAttribute(classAttr);
				}
				
				/** When entering a state. */
				public boolean entering(Object target, Object edge) {
					if (edge == null) { return true; }
					Object source = ptag.getEdgeSource(edge);
					Integer sourceIndex = indexOf(source);
					Object letter = pta.getEdgeLetter(edge);
					Integer targetIndex = indexOf(target);
					reverse(targetIndex,letter,true).add(sourceIndex);
					return true;
				}
				
			});
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
			
			consolidated.add(clazz);
		}
		
		/** On new try ... */
		public void startTry(PTAEdge edge, Object kState) {
			// start transaction on UnionFind
			ufds.startTransaction();
		}

		/** On try commit ... */
		public void commit(Simulation simu) {
			// do nothing on consolidations
			if (!simu.isRealTry()) { return; }

			// check short back propagate
			StartTryD work = (StartTryD) simu.getStartTry().decorate();
			
			// find source kernel state index
			PTAEdge victimEdge = work.getVictimEdge();
			Object skState = work.getSourceKernelState();
			assert (skState != null) : "Source kernel state has been hooked";
			Object skStateIndex = ufds.findi(kStateIndex(skState));
			
			// find letter
			Object letter = victimEdge.letter();
			
			// find target index
			Integer targetIndex = ufds.findi(kStateIndex(work.getTargetState()));
			
			//System.out.println(targetIndex + " <- " + letter + " " + skStateIndex);
			
			// take incompatibilities of target
			Set<Integer> targetInc = incompatibilities(targetIndex);
			
			//System.out.println("Incompatibilities of " + targetIndex + " -> " + targetInc);
			
			// compute transitive closure of the back incompatibilities
			Set<Integer> rDelta = rDelta(targetInc, letter);
			Set<Integer> rInc = ufds.findi(rDelta);
			
			//System.out.println("Reverse delta -> " + rDelta);
			//System.out.println("Reverse incompatibilities -> " + rInc);
			
			// check that skState is not in the incompatibilities
			if (rInc.contains(skStateIndex)) {
				throw new Avoid();
			}
			
			// commit transaction on UnionFind
			ufds.commit();
		}

		/** On try rollback ... */
		public void rollback(Simulation simu, boolean incompatibility) {
			// rollback transaction on UnionFind
			if (simu.isRealTry()) { ufds.rollback(); }
		}

		/** Merges two states. */
		protected void merge(int i, int j) {
			assert (isCompatible(i,j)) : "Compatibility layer plays its role.";
			//System.out.println("\t\tMerging " + i + " " + j + " :: " + isCompatible(i,j));
			ufds.union(i,j);
		}
		
		/** On merge ... */
		public void merge(PTAState victim, Object target) {
			int iClass = ufds.findi(oStateIndex(victim));
			int jClass = ufds.findi(kStateIndex(target));
			
			// if victim index is consolidated then target index if that index
			if (consolidated.contains(iClass) && iClass != jClass) {
				throw new Avoid();
			}
			
			merge(iClass,jClass);
		}

		/** On merge ... */
		public void merge(PTAState victim, PTAState target) {
			int iClass = ufds.findi(oStateIndex(victim));
			int jClass = ufds.findi(oStateIndex(target));
			merge(iClass,jClass);
		}

	}
	
}
