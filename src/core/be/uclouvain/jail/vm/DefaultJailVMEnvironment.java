package be.uclouvain.jail.vm;

import java.io.File;
import java.io.PrintWriter;

/** Provides a default implementation of IJailVMEnvironment. */
public class DefaultJailVMEnvironment implements IJailVMEnvironment {

	/** Returns the console writer to use. */
	public PrintWriter getConsoleWriter() {
		return new PrintWriter(System.out);
	}
	
	/** Prints the message and stack trace. */
	public void handleError(Throwable t) {
		System.out.println(t.getMessage());
		t.printStackTrace();
	}

	/** Prints to output stream. */
	public void printConsole(String message, LEVEL level) {
		System.out.println(level.prefix() + message);
	}

	/** Resolves the path according to execution dir. */
	public String resolvePath(String path) {
		return new File(path).getAbsolutePath();
	}

}
