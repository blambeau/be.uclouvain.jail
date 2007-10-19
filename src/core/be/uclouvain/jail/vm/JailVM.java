package be.uclouvain.jail.vm;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import net.chefbe.autogram.ast2.IASTNode;
import net.chefbe.autogram.ast2.ILocation;
import net.chefbe.autogram.ast2.parsing.ParseException;
import net.chefbe.autogram.ast2.parsing.active.ASTLoader;
import net.chefbe.autogram.ast2.parsing.active.ASTLoader.EnumTypeResolver;
import net.chefbe.autogram.ast2.parsing.peg.Input;
import net.chefbe.autogram.ast2.parsing.peg.Pos;
import net.chefbe.autogram.ast2.utils.BaseLocation;
import be.uclouvain.jail.vm.JailVMCommand.VMValueArgument;
import be.uclouvain.jail.vm.autogram.JailCallback;
import be.uclouvain.jail.vm.autogram.JailNodes;
import be.uclouvain.jail.vm.autogram.JailParser;

/**
 * Provides JAIL virtual machine, which interprets .jail files.
 * 
 * @author blambeau
 */
public class JailVM {

	/** VM memory. */
	private Map<String,Object> memory;
	
	/** VM stack. */
	private Stack<Object> stack;
	
	/** Installed plugins. */
	private Map<String,IJailVMPlugin> plugins;
	
	/** Creates a VM instance. */
	public JailVM() {
		memory = new HashMap<String,Object>();
		stack = new Stack<Object>();
		plugins = new HashMap<String,IJailVMPlugin>();
	}

	/** Executes some commands taken on some source. */
	public void execute(Object source) throws JailVMException {
		try {
			// create location and pos
			ILocation loc = new BaseLocation(source);
			Pos pos = new Pos(Input.input(loc),0);
			
			// create parser and parse
			JailParser parser = new JailParser();
			parser.setActiveLoader(new ASTLoader(new EnumTypeResolver<JailNodes>(JailNodes.class)));
			IASTNode root = (IASTNode) parser.pUnit(pos);
			
			// execute on callbacl
			try {
				root.accept(new JailVMCallback());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (IOException ex) {
			throw new JailVMException("Unable to parse jail file.",ex);
		} catch (ParseException ex) {
			throw new JailVMException("Jail parsing failed.",ex);
		}
	}
	
	/** Executes a command. */
	protected void executeCommand(JailVMCommand command) throws JailVMException {
		// retrieve plugin from namespace
		String namespace = command.namespace();
		IJailVMPlugin plugin = plugins.get(namespace);
		
		// some check
		if (plugin == null) {
			throw new JailVMException("Unknown namespace " + namespace);
		}
		
		// execute 
		Object result = plugin.execute(command, this);
		if (result != null) {
			push(result);
		}
	}
	
	/** Registers some plugin. */
	public void register(IJailVMPlugin plugin) {
		plugins.put(plugin.namespace(), plugin);
	}
	
	/** Returns a plugin mapped to some namespace. */
	public IJailVMPlugin getPlugin(String namespace) {
		return plugins.get(namespace);
	}
	
	/** Affects some value to a variable. */
	public void affect(String varName, Object value) {
		memory.put(varName, value);
	}
	
	/** Destroys a variable. */
	public void destroy(String varName) {
		memory.remove(varName);
	}
	
	/** Pushes a value on the stack. */
	public void push(Object value) {
		stack.push(value);
	}
	
	/** Pops and return top value from/of the stack. */
	public Object pop() {
		return stack.pop();
	}
	
	/** Returns top value of the stack. */
	public Object top() {
		return stack.peek();
	}
	
	/** Execution callback. */
	class JailVMCallback extends JailCallback<Object> {

		/** Current constructed command. */
		private JailVMCommand command;
		
		/** Callback method for UNIT nodes. */
		public Object UNIT(IASTNode node) throws Exception {
			super.recurseOnChildren(node);
			return null;
		}

		/** Callback method for PLUGIN_COMMAND nodes. */
		public Object PLUGIN_COMMAND(IASTNode node) throws Exception {
			command = new JailVMCommand(node.getAttrString("namespace"),node.getAttrString("name"));
			super.recurseOnChildren(node);
			executeCommand(command);
			return command;
		}

		/** Callback method for ARGUMENT nodes. */
		public Object ARGUMENT(IASTNode node) throws Exception {
			Object value = node.getAttr("value");
			VMValueArgument<Object> arg = new VMValueArgument<Object>(value);
			command.addArgument(arg);
			return arg;
		}
		
	}
	
}
