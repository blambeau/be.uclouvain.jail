package be.uclouvain.jail.vm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Virtual machine command. */
public class JailVMCommand {

	/** Command argument. */
	public static interface VMCommandArgument {
		
		/** Returns the argument value. */
		public <T> T value();

	}
	
	/** Direct value argument. */ 
	public static class VMValueArgument<T> implements VMCommandArgument {
		
		/** Attache value. */
		private T value;
		
		/** Creates an argument. */
		public VMValueArgument(T value) {
			this.value = value;
		}

		/** Returns attache value. */
		@SuppressWarnings("unchecked")
		public T value() {
			return value;
		}
		
	}
	
	/** Command namespace. */
	private String namespace;
	
	/** Command name. */
	private String name;
	
	/** Command arguments. */
	private List<VMCommandArgument> arguments;
	
	/** Options. */
	private Map<String,VMCommandArgument> options;
	
	/** Creates a command. */
	public JailVMCommand(String namespace, String name) {
		if (name == null) { throw new IllegalArgumentException("Command name required"); }
		this.namespace = namespace;
		this.name = name;
		arguments = new ArrayList<VMCommandArgument>();
		options =  new HashMap<String,VMCommandArgument>();
	}
	
	/** Returns command namespace. */
	public String namespace() {
		return namespace;
	}
	
	/** Returns command name. */
	public String name() {
		return name;
	}
	
	/** Adds an argument. */
	public void addArgument(VMCommandArgument arg) {
		arguments.add(arg);
	}
	
	/** Returns the index-th argument. */
	public VMCommandArgument getArgument(int index) {
		if (index >= arguments.size()) {
			throw new ArrayIndexOutOfBoundsException("No such argument.");
		}
		return arguments.get(index);
	}
	
	/** Computes and returns index-th argument value. */
	@SuppressWarnings("unchecked")
	public <T> T getArgumentValue(int index, JailVM vm) throws JailVMException {
		try {
			VMCommandArgument arg = getArgument(index);
			T value = arg.value();
			return value;
		} catch (ArrayIndexOutOfBoundsException ex) {
			return (T) vm.top();
			//throw new JailVMException("no such argument " + index);
		} catch (ClassCastException ex) {
			throw new JailVMException("bad argument type " + index);
		}
	}
	
	/** Returns a command option. */
	public VMCommandArgument getOption(String name) {
		if (options.containsKey(name)) {
			return options.get(name);
		} else {
			return null;
		}
	}
	
}
