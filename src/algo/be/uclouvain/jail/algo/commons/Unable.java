package be.uclouvain.jail.algo.commons;

/** 
 * Exception thrown by algorithms when they are unable to do the job. 
 *
 * <p>This exception is typically thrown by random algorithms when their
 * maxTry has been reached.</p>
 */
public class Unable extends RuntimeException {

	/** Serial Version UID. */
	private static final long serialVersionUID = -8229787452438358658L;

	/** Creates an exception instance. */
	public Unable() {
	}

	/** Creates an exception instance. */
	public Unable(String msg) {
		super(msg);
	}

}
