package be.uclouvain.jail.algo.graph.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.uinfo.IUserInfo;

/** 
 * Common implementation of graph vertex and edge groups. 
 * 
 * @author blambeau
 */
public class GraphMemberGroup implements IGraphMemberGroup {

	/** Graph. */
	protected IDirectedGraph graph;
	
	/** Group as an array of components. */
	protected Set<Object> group; 
	
	/** Creates an empty group instance. */
	public GraphMemberGroup(IDirectedGraph graph, Comparator<Object> order) {
		this.graph = graph;
		this.group = (order == null) ? new HashSet<Object>() : new TreeSet<Object>(order);
	}
	
	/** Returns the graph. */
	public IDirectedGraph getGraph() {
		return graph;
	}

	/** Adds a component in the group. */
	public void addMember(Object component) {
		if (component == null) {
			throw new IllegalArgumentException("Component cannot be null");
		}
		group.add(component);
	}
	
	/** Adds some components in the group. */
	public void addMembers(Collection<Object> components) {
		if (components == null) {
			throw new IllegalArgumentException("Components cannot be null");
		}
		for (Object component: components) {
			if (component == null) { 
				throw new IllegalArgumentException("Component cannot be null"); 
			}
			group.add(component);
		}
	}
	
	/** Adds some components in the group. */
	public void addMembers(Object...components) {
		if (components == null) {
			throw new IllegalArgumentException("Components cannot be null");
		}
		for (Object component: components) {
			if (component == null) { 
				throw new IllegalArgumentException("Component cannot be null"); 
			}
			group.add(component);
		}
	}
	
	/** Checks if the group contains some component. */
	public boolean contains(Object component) {
		return group.contains(component);
	}
	
	/** Returns the members of the group as a set. */
	public Set<Object> getMembers() {
		return group;
	}
	
	/** Returns an iterator on states. */
	public Iterator<Object> iterator() {
		return group.iterator();
	}

	/** Returns the list of user infos. */
	public List<IUserInfo> getUserInfos() {
		List<IUserInfo> infos = new ArrayList<IUserInfo>();
		for (Object component: group) {
			infos.add(graph.getUserInfoOf(component));
		}
		return infos;
	}
	
	/** Returns group size. */
	public int size() {
		return group.size();
	}
	
	/** Computes an returns an hash code. */
	public int hashCode() {
		return group.hashCode();
	}
	
	/** Checks equality with another group. */
	public boolean equals(Object o) {
		if (o == this) { return true; }
		if (o instanceof GraphMemberGroup) { 
			GraphMemberGroup other = (GraphMemberGroup) o;
			return graph.equals(other.graph) && group.equals(other.group);
		} else if (o instanceof IGraphMemberGroup) {
			IGraphMemberGroup other = (IGraphMemberGroup) o;
			return graph.equals(other.getGraph()) && group.equals(other.getMembers());
		}
		return false;	
	}
	
}
