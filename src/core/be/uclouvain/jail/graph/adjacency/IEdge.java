package be.uclouvain.jail.graph.adjacency;

import be.uclouvain.jail.uinfo.IUserInfo;

/**
 * Contract to be a valid edge of a DirectedGraph implemented with Adjacency List Structure.
 * 
 * @author LAMBEAU Bernard
 */
public interface IEdge {

	/** Returns the edge id. */
	public int getId();

	/** Sets the edge id. */
	public void setId(int id);

	/** Returns attached user information. */
	public IUserInfo getUserInfo();

	/** Sets attached user information. */
	public void setUserInfo(IUserInfo info);

	/** Returns edge source. */
	public IVertex getSource();

	/** Sets edge source. */
	public void setSource(IVertex source);

	/**
	 * <p>Checks is a vertex is the source of the edge.</p>
	 * 
	 * <p>This method must ensure that the result is coherent with standard equals() method result ;
	 * for example : (e.isSource(v) == true) <=> (e.getSource().equals(v) == true).
	 * 
	 * @param vertex a graph vertex.
	 * @return true if the parameter vertex is the source of the edge. false otherwise.
	 */
	public boolean isSource(IVertex vertex);

	/** Returns edge target. */
	public IVertex getTarget();

	/** Sets edge target. */
	public void setTarget(IVertex target);

	/**
	 * <p>Checks is a vertex is the target of the edge.</p>
	 * 
	 * <p>This method must ensure that the result is coherent with standard equals() method result ;
	 * for example : (e.isTarget(v) == true) <=> (e.getTarget().equals(v) == true).
	 * 
	 * @param vertex a graph vertex.
	 * @return true if the parameter vertex is the target of the edge. false otherwise.
	 */
	public boolean isTarget(IVertex vertex);

}