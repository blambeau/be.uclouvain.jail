package be.uclouvain.jail.algo.fa.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import be.uclouvain.jail.fa.IFA;
import be.uclouvain.jail.graph.utils.ITotalOrder;
import be.uclouvain.jail.uinfo.IUserInfo;

/** 
 * Provides an implementation of components group. 
 * 
 * @author blambeau
 */
public class FAAbstractGroup implements Iterable<Object> {

	/** Automaton. */
	protected IFA fa;
	
	/** Group as an array of components. */
	protected Set<Object> group; 
	
	/** Creates an empty group instance. */
	public FAAbstractGroup(IFA fa, ITotalOrder<Object> order) {
		this.fa = fa;
		this.group = new TreeSet<Object>(order);
	}
	
	/** Adds a component in the group. */
	public void addComponent(Object component) {
		if (component == null) {
			throw new IllegalArgumentException("Component cannot be null");
		}
		group.add(component);
	}
	
	/** Adds some components in the group. */
	public void addComponents(Collection<Object> components) {
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
	public void addComponents(Object...components) {
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
	
	/** Returns an iterator on states. */
	public Iterator<Object> iterator() {
		return group.iterator();
	}

	/** Returns the list of user infos. */
	public List<IUserInfo> getUserInfos() {
		List<IUserInfo> infos = new ArrayList<IUserInfo>();
		for (Object component: group) {
			infos.add(fa.getGraph().getUserInfoOf(component));
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
		if (o instanceof FAAbstractGroup == false) { return false;	}
		return group.equals(((FAAbstractGroup)o).group);
	}
	
}
