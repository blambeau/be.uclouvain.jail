package be.uclouvain.jail.algo.graph.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import be.uclouvain.jail.graph.IDirectedGraph;

/**
 * Allows partitioning graph members according to some partitioners.
 * 
 * @author blambeau
 */
public class GraphPartition implements IGraphPartition {

	/** Partitionned graph. */
	private IDirectedGraph graph;
	
	/** Partitionner. */
	private MultiPartitionner<Object> partitionner;

	/** Groups. */
	private List<IGraphMemberGroup> groups;
	
	/** Creates an empty partition. */
	public GraphPartition(IDirectedGraph graph, Iterable<Object> members) {
		this.graph = graph;
		partitionner = new MultiPartitionner<Object>();
		groups = new ArrayList<IGraphMemberGroup>();
		
		// creates initial partition
		IGraphMemberGroup initGroup = factorGroup();
		for (Object o: members) {
			initGroup.addMember(o);
		}
		if (initGroup.size()==0) {
			throw new IllegalArgumentException("Members cannot be empty.");
		}
		
		// add initial group
		groups.add(initGroup);
	}
	
	/** Asserts that this partition is correct. */
	private void assertCorrectPartition() {
		for (IGraphMemberGroup group: groups) {
			if (group.size()==0) {
				throw new AssertionError("Correct partition (no empty group).");
			}
		}
	}
	
	/** Returns the partitionned graph. */
	public IDirectedGraph getGraph() {
		return graph;
	}
	
	/** Factors an empty group. */
	protected IGraphMemberGroup factorGroup() {
		return new GraphMemberGroup(getGraph(),null);
	}
	
	/** Returns the index-th group. */
	public IGraphMemberGroup getGroup(int index) {
		return groups.get(index);
	}
	
	/** Refines this partition according to a partitionner. */
	public void refine(IGraphPartitionner<Object> partitionner) {
		int size = groups.size();
		for (int i=0; i<size; i++) {
			refine(i,partitionner);
		}
		this.partitionner.addPartitionner(partitionner);
		assertCorrectPartition();
	}
	
	/** Refines a single group, according to a partitionner. */
	public void refine(int i, IGraphPartitionner<Object> p) {
		IGraphMemberGroup group = this.groups.get(i);
		
		// subgroups by partition key
		Map<Object,IGraphMemberGroup> subgroups = new HashMap<Object,IGraphMemberGroup>();

		// for each member of the group
		for (Object o: group) {
			Object key = p.getPartitionOf(o);
			
			// find its subgroup
			IGraphMemberGroup subgroup = subgroups.get(key);
			
			// create it when new one
			if (subgroup == null) {
				subgroup = factorGroup();
				subgroups.put(key, subgroup);
			}
			
			subgroup.addMember(o);
		}
		
		// add each sub group in the groups
		int j=0;
		for (IGraphMemberGroup subgroup: subgroups.values()) {
			if (subgroup.size() == 0) {
				throw new AssertionError("Does not lead to empty groups.");
			}
			if (j++ == 0) {
				groups.set(i, subgroup);
			} else {
				groups.add(subgroup);
			}
		}
	}
	
	/** Returns the group of a member. */
	public IGraphMemberGroup getGroupOf(Object member) {
		for (IGraphMemberGroup group: groups) {
			if (group.contains(member)) {
				return group;
			}
		}
		return null;
	}

	/** Returns the group of the member. */
	public Object getPartitionOf(Object member) {
		return getGroupOf(member);
	}

	/** Returns number of groups. */
	public int size() {
		return groups.size();
	}

	/** Returns an iterator on groups. */
	public Iterator<IGraphMemberGroup> iterator() {
		return groups.iterator();
	}

}
