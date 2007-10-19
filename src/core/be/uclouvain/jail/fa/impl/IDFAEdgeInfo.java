package be.uclouvain.jail.fa.impl;

/** 
 * Marker for edge informations in DFAs.
 *
 * @author blambeau
 */ 
public interface IDFAEdgeInfo {

	/** Letter attached to the edge. */
	public Object letter();
	
}
