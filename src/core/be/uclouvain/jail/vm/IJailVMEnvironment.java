package be.uclouvain.jail.vm;

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
	
	/** Prints a message on the console. */
	public void printConsole(String message, LEVEL level);

	/** Handles an error. */
	public void handleError(Throwable t);
	
}
