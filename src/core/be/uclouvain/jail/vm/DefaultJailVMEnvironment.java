package be.uclouvain.jail.vm;

/** Provides a default implementation of IJailVMEnvironment. */
public class DefaultJailVMEnvironment implements IJailVMEnvironment {

	/** Prints the message and stack trace. */
	public void handleError(Throwable t) {
		System.out.println(t.getMessage());
		t.printStackTrace();
	}

	/** Prints to output stream. */
	public void printConsole(String message, LEVEL level) {
		System.out.println(level.prefix() + message);
	}

}
