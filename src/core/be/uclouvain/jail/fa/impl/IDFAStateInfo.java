package be.uclouvain.jail.fa.impl;

/** 
 * Marker for vertex informations in DFAs.
 * 
 * @author blambeau
 */
public interface IDFAStateInfo {

	/** Returns true if the vertex is marked as initial. */
	public boolean isInitial();
	
	/** Returns true if the vertex is marked as accepted. */
	public boolean isAccepting();
	
	/** Returns true if the vertex is marked as error. */
	public boolean isError();
	
}
