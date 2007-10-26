package be.uclouvain.jail.vm;

/** Virtual machine exception. */
public class JailVMException extends Exception {

	/** Serial version UID. */
	private static final long serialVersionUID = 5767962525314465505L;

	/** Creates an empty exception. */
	public JailVMException() {
	}

	/** Creates an exception with message. */
	public JailVMException(String msg) {
		super(msg);
	}

	/** Creates an exception with cause. */
	public JailVMException(Throwable cause) {
		super(cause);
	}

	/** Creates an exception with message and cause. */
	public JailVMException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
