package be.uclouvain.jail.algo.graph.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.uinfo.IUserInfo;

/**
 * Basic decorator of groups.
 * 
 * @author blambeau
 */
public abstract class GraphMemberGroupDecorator implements IGraphMemberGroup {

	/** Decorated group. */
	protected IGraphMemberGroup group;
	
	/** Creates an empty group instance. */
	public GraphMemberGroupDecorator(IGraphMemberGroup group) {
		this.group = group;
	}
	
	/** Delegated to the group. */
	public IDirectedGraph getGraph() {
		return group.getGraph();
	}

	/** Delegated to the group. */
	public void addMember(Object member) {
		group.addMember(member);
	}

	/** Delegated to the group. */
	public void addMembers(Collection<Object> members) {
		group.addMembers(members);
	}

	/** Delegated to the group. */
	public void addMembers(Object... members) {
		group.addMembers(members);
	}

	/** Delegated to the group. */
	public boolean contains(Object component) {
		return group.contains(component);
	}

	/** Delegated to the group. */
	public Set<Object> getMembers() {
		return group.getMembers();
	}
	
	/** Delegated to the group. */
	public List<IUserInfo> getUserInfos() {
		return group.getUserInfos();
	}

	/** Delegated to the group. */
	public Iterator<Object> iterator() {
		return group.iterator();
	}

	/** Delegated to the group. */
	public int size() {
		return group.size();
	}
	
	/** Delegaed to the group. */
	public int hashCode() {
		return group.hashCode();
	}
	
	/** Shows as a string. */
	public String toString() {
		return group.toString();
	}

	/** Checks equality. */
	public boolean equals(Object o) {
		if (o == this) { return true; }
		if (o instanceof GraphMemberGroupDecorator) {
			return group.equals(((GraphMemberGroupDecorator)o).group);
		} else if (o instanceof IGraphMemberGroup) {
			IGraphMemberGroup other = (IGraphMemberGroup) o;
			return group.equals(other);
		}
		return false;
	}
	
}
