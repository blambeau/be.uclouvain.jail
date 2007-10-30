package be.uclouvain.jail.vm;

/** Virtual machine exception. */
public class JailVMException extends Exception {

	/** Error type. */
	public static enum ERROR_TYPE {
		
		/** When a command is unknown. */
		UNKNOWN_COMMAND,
		
		/** When a dialect is unknown. */
		UNKNOWN_DIALECT,
		
		/** When a reference to a unexistant variable is made. */
		UNKNOWN_VAR,
		
		/** When a reference to a unexistant function is made. */
		UNKNOWN_FUNCTION,
		
		/** When a command is badly used. */
		BAD_COMMAND_USAGE,
		
		/** When some object cannot be adapted. */
		ADAPTABILITY_ERROR,
		
		/** When a parsing failed. */
		PARSE_ERROR,
		
		/** When a file access failed. */
		FILE_ACCESS_ERROR,
		
		/** When an internal error occurs. */
		INTERNAL_ERROR,
		
	}
	
	/** Serial version UID. */
	private static final long serialVersionUID = 5767962525314465505L;

	/** Error type. */
	private ERROR_TYPE type;
	
	/** Command which failed. */
	private IJailVMCommand command;
	
	/** Command name that failed. */
	private String commandName;
	
	/** Creates an empty exception. */
	public JailVMException(ERROR_TYPE type, IJailVMCommand command, String message, Throwable cause) {
		super(message,cause);
		this.type = type;
		this.command = command;
	}
	
	/** Creates an empty exception. */
	public JailVMException(ERROR_TYPE type, IJailVMCommand command, Throwable cause) {
		this(type,command,null,cause);
	}
	
	/** Creates an empty exception. */
	public JailVMException(ERROR_TYPE type, IJailVMCommand command, String message) {
		this(type,command,message,null);
	}
	
	/** Creates an empty exception. */
	public JailVMException(ERROR_TYPE type, IJailVMCommand command) {
		this(type,command,null,null);
	}
	
	/** Factors a unknown command exception. */
	public static JailVMException unknownCommand(String name) {
		JailVMException ex = new JailVMException(ERROR_TYPE.UNKNOWN_COMMAND,null,null,null);
		ex.commandName = name;
		return ex;
	}
	
	/** Factors a unknown dialect exception. */
	public static JailVMException unknownDialect(String name) {
		JailVMException ex = new JailVMException(ERROR_TYPE.UNKNOWN_DIALECT,null,null,null);
		ex.commandName = name;
		return ex;
	}
	
	/** Factors a unknown dialect exception. */
	public static JailVMException unknownVariable(String name) {
		JailVMException ex = new JailVMException(ERROR_TYPE.UNKNOWN_VAR,null,null,null);
		ex.commandName = name;
		return ex;
	}
	
	/** Factors a unknown dialect exception. */
	public static JailVMException unknownFunction(String name) {
		JailVMException ex = new JailVMException(ERROR_TYPE.UNKNOWN_FUNCTION,null,null,null);
		ex.commandName = name;
		return ex;
	}
	
	/** Sets the command that failed. */
	public void setCommand(IJailVMCommand command) {
		this.command = command;
	}

	/** Returns the message. */
	public String getMessage() {
		switch (type) {
			case UNKNOWN_DIALECT:
				return "Unknown dialect " + commandName;
			case UNKNOWN_COMMAND:
				return "Unknown command " + commandName;
			case UNKNOWN_VAR:
				return "Unknown variable reference " + commandName;
			case UNKNOWN_FUNCTION:
				return "Unknown function " + commandName;
			case BAD_COMMAND_USAGE:
				return "Usage: " + command.getSignature();
			case PARSE_ERROR:
				return "Parse error: " + super.getCause().getMessage();
			case INTERNAL_ERROR:
				return "Fatal error on " + command.getName() + "(please excuse): " + super.getMessage();
		}
		return super.getMessage();
	}

}
