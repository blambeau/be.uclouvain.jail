package be.uclouvain.jail.algo.graph.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.utils.ITotalOrder;

/**
 * Default partition.
 * 
 * @author blambeau
 */
public class DefaultGraphPartition implements IGraphPartition {

	/** Partitionner. */
	private IGraphPartitionner<Object> partitionner;
	
	/** Groups. */
	private Map<Object, IGraphMemberGroup> groups;
	
	/** Creates a partition instance. */
	public DefaultGraphPartition(IDirectedGraph graph, ITotalOrder<Object> members, IGraphPartitionner<Object> p) {
		this.partitionner = p;
		this.groups = new HashMap<Object, IGraphMemberGroup>();

		// create the groups
		for (Object member: members) {
			// retrieve part
			Object part = p.getPartitionOf(member);
			
			// find associated group, creating it if required
			IGraphMemberGroup group = groups.get(part);
			if (group == null) {
				group = new GraphMemberGroup(graph,members);
				groups.put(part, group);
			}
			
			// add member to the group
			group.addMember(member);
		}
	}
	
	/** Returns the group of a member. */
	public IGraphMemberGroup getGroupOf(Object member) {
		return groups.get(partitionner.getPartitionOf(member));
	}

	/** Returns number of partitions. */
	public int size() {
		return groups.size();
	}

	/** Iterates the groups. */
	public Iterator<IGraphMemberGroup> iterator() {
		return groups.values().iterator();
	}

	/** Returns the partition of a member. */
	public Object getPartitionOf(Object member) {
		return partitionner.getPartitionOf(member);
	}

}
