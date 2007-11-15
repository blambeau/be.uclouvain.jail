package be.uclouvain.jail.algo.graph.utils;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.uinfo.IUserInfo;

/**
 * Provides common API of all groups.
 * 
 * @author blambeau
 */
public interface IGraphMemberGroup extends Iterable<Object> {

	/** Returns the graph. */
	public IDirectedGraph getGraph();
	
	/** Adds a member. */
	public void addMember(Object member);
	
	/** Adds some components. */
	public void addMembers(Object...members);
	
	/** Adds some components. */
	public void addMembers(Collection<Object> members);
	
	/** Returns group size. */
	public int size();
	
	/** Returns true if the component is contained in thte group,
	 * false otherwise. */
	public boolean contains(Object component);
	
	/** Returns the members of the group as a set. */
	public Set<Object> getMembers();
	
	/** Returns the list of IUserInfo. */
	public List<IUserInfo> getUserInfos();
	
}
