package be.uclouvain.jail.algo.commons;

/** 
 * Exception thrown by algorithms to restart the computation. 
 *
 * <p>This exception is typically thrown by induction oracles that
 * modify the induction sample.</p>
 */
public class Restart extends RuntimeException {

	/** Serial Version UID. */
	private static final long serialVersionUID = -8229787452438358658L;

	/** Creates an exception instance. */
	public Restart() {
	}

}
