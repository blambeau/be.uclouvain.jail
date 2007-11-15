package be.uclouvain.jail.vm;

import java.io.PrintWriter;

/**
 * Provides external environment to the Jail Virtual Machine. 
 * 
 * @author blambeau
 */
public interface IJailVMEnvironment {

	/** Message levels. */
	public static enum LEVEL {
		
		/** User request level. */
		USER(""),
		
		/** Virtual machine information. */
		INFO("INFO: "),
		
		/** Warning message. */
		WARNING("WARNING: "),
		
		/** Error message. */
		ERROR("ERROR: ");
		
		/** Prefix for output. */
		private String prefix;

		/** Creates a LEVEL instance. */
		LEVEL(String prefix) {
			this.prefix = prefix;
		}
		
		/** Returns output prefix. */ 
		public String prefix() {
			return prefix;
		}
		
	}
	
	/** Returns the console writer to use. */
	public PrintWriter getConsoleWriter();
	
	/** Prints a message on the console. */
	public void printConsole(String message, LEVEL level);

	/** Handles an error. */
	public void handleError(Throwable t);
	
}
