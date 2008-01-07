package be.uclouvain.jail.algo.commons;

/** 
 * Exception thrown by algorithms when some merge (for example) must 
 * be avoided. 
 * 
 * <p>This exception is typically thrown to avoid the current state
 * merging tried by the induction algorithm (that is, to rollback the 
 * current simulation). It is typically thrown by IUserInfo functions 
 * as well as oracles.</p>
 */
public final class Avoid extends RuntimeException {

	/** Serial Version UID. */
	private static final long serialVersionUID = -4328376166950513958L;

	/** Creates an avoid instance. */
	public Avoid() {
	}
	
}
