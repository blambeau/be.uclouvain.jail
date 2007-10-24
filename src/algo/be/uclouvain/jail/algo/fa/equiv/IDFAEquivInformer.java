package be.uclouvain.jail.algo.fa.equiv;

import be.uclouvain.jail.uinfo.IUserInfo;

/**
 * Encapsulates the actual check of equivalence of states and edges.
 * 
 * <p>Basically, the automaton equivalence algorithm checks the language 
 * equivalence of the DFAs, not the fact that {@link IUserInfo}S attached
 * to equivalent states and edges are compatible in any way. You can extend
 * the algorithm by providing an external informer.</p>
 * 
 * <p>This interface can be implemented. EqualityEquivInformer may be used
 * to ensure that equivalent states and edges share equal IUserInfoS.</p>
 * 
 * @author blambeau
 */
public interface IDFAEquivInformer {

	/** 
	 * Checks equivalence of state user infos. 
	 * 
	 * @return true if two states can be considered equivalent according to
	 * their respective user informations, false otherwise (implies the non 
	 * equivalence of the two DFAs).
	 */
	public boolean isStateEquivalent(IUserInfo s1, IUserInfo s2);
	
	/** 
	 * Checks equivalence of edge user infos. 
	 * 
	 * @return true if two states can be considered equivalent according to
	 * their respective user informations, false otherwise (implies the non 
	 * equivalence of the two DFAs).
	 */
	public boolean isEdgeEquivalent(IUserInfo s1, IUserInfo s2);
	
}
