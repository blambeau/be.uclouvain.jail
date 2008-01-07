package be.uclouvain.jail.algo.induct.internal;

import java.util.ArrayList;
import java.util.List;

import net.chefbe.javautils.comparisons.HashCodeUtils;
import be.uclouvain.jail.algo.induct.open.ICompatibility;
import be.uclouvain.jail.algo.induct.open.IPartitionner;
import be.uclouvain.jail.algo.induct.utils.AbstractCompatibility;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.utils.ITotalOrder;

/** 
 * Implements an ICompatibility for equivalence relations of the PTA
 * states. 
 * 
 * <p>The equivalence relation on the PTA states is computed accordingly 
 * to installed partitioners. Each of them computes a local partition 
 * of the state, following an attribute value (for example). Two states
 * are equal according to this equivalence relation if they belong to 
 * exactly the same partitions.</p>
 */
public class EquivRelations extends AbstractCompatibility {
	
	/** A state partition. */
	class StatePartition {

		/** Partitions of the state (one by partitioner). */
		protected int pids[];
 
		/** Creates a state partition. */
		public StatePartition(int pids[]) {
			super();
			this.pids = pids;
		}
		
		/** Computes an hash-code. */
		public int hashCode() {
			int hashCode = 23;
			int ai[] = pids;
			int i = 0;
			for (int j = ai.length; i < j; i++) {
				int pid = ai[i];
				hashCode = HashCodeUtils.hash(hashCode, pid);
			}
			return hashCode;
		}

		/** Checks equality with another partition. */ 
		public boolean equals(Object arg0) {
			if (arg0 instanceof StatePartition == false) {
				throw new AssertionError("Compared with another partition.");
			}
			StatePartition other = (StatePartition) arg0;
			assert (other.pids.length == pids.length) : "Same partition size.";
			int size = pids.length;
			for (int i = 0; i < size; i++) {
				if (pids[i] != other.pids[i]) {
					return false;
				}
			}

			return true;
		}

	}

	/** Partitioners. */
	private List<IPartitionner> partitioners;

	/** Partition for each state. */
	private StatePartition partition[];

	/** Prefix tree acceptor to partition. */
	private IDFA pta;

	/** Creates a equivalence relation instance. */
	public EquivRelations(ICompatibility parent) {
		super(parent);
		partitioners = new ArrayList<IPartitionner>();
	}

	/** Adds a partitioner. */
	public void addRelation(IPartitionner partitionner) {
		partitioners.add(partitionner);
	}

	/** Computes the partitioning. */
	public void initialize(InductionAlgo algo) {
		pta = algo.getPTA();
		int sCount = pta.getGraph().getVerticesTotalOrder().size();
		int size = partitioners.size();
		if (size == 0) {
			return;
		}
		
		// compute partitions
		partition = new StatePartition[sCount];
		int computed[][] = new int[size][];
		for (int i = 0; i < size; i++) {
			IPartitionner p = (IPartitionner) partitioners.get(i);
			computed[i] = p.partition(pta);
			assert (computed[i].length == sCount) : "Partition has same size as the PTA.";
		}

		// create equivalence relation
		for (int i = 0; i < sCount; i++) {
			int pids[] = new int[size];
			for (int j = 0; j < size; j++) {
				pids[j] = computed[j][i];
			}
			partition[i] = new StatePartition(pids);
		}

	}

	/** Checks compatibility of two states. */
	protected boolean doIsCompatible(Object p, Object q) {
		if (partition == null) {
			return true;
		}
		IDirectedGraph g = pta.getGraph();
		ITotalOrder<Object> states = g.getVerticesTotalOrder();
		int pIndex = states.indexOf(p);
		int qIndex = states.indexOf(q);
		assert !(pIndex < 0 || qIndex < 0 || pIndex >= partition.length || qIndex >= partition.length) :
			   "Valid request (recognized states)";
		return partition[pIndex].equals(partition[qIndex]);
	}

}
