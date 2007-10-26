package be.uclouvain.jail.vm;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.chefbe.autogram.ast2.IASTNode;
import net.chefbe.autogram.ast2.ILocation;
import net.chefbe.autogram.ast2.parsing.ParseException;
import net.chefbe.autogram.ast2.parsing.active.ASTLoader;
import net.chefbe.autogram.ast2.parsing.active.ASTLoader.EnumTypeResolver;
import net.chefbe.autogram.ast2.parsing.peg.Input;
import net.chefbe.autogram.ast2.parsing.peg.Pos;
import net.chefbe.autogram.ast2.utils.BaseLocation;
import net.chefbe.autogram.ast2.utils.DebugVisitor;
import be.uclouvain.jail.adapt.AdaptUtils;
import be.uclouvain.jail.adapt.IAdapter;
import be.uclouvain.jail.dialect.IPrintable;
import be.uclouvain.jail.dialect.dot.DOTDirectedGraphLoader;
import be.uclouvain.jail.vm.autogram.JailNodes;
import be.uclouvain.jail.vm.autogram.JailParser;
import be.uclouvain.jail.vm.toolkits.AutomatonToolkit;
import be.uclouvain.jail.vm.toolkits.GraphToolkit;

/**
 * Provides JAIL virtual machine, which interprets .jail files.
 * 
 * @author blambeau
 */
public class JailVM {

	/** VM memory. */
	private Map<String,Object> memory;
	
	/** Installed plugins. */
	private Map<String,IJailVMToolkit> toolkits;
	
	/** Creates a VM instance. */
	public JailVM() {
		memory = new HashMap<String,Object>();
		toolkits = new HashMap<String,IJailVMToolkit>();
		register("fa",new AutomatonToolkit());
		register("graph",new GraphToolkit());
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
			
			// execute on callback
			try {
				// debug
				root.accept(new DebugVisitor());
				root.accept(new JailVMCallback(this));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (IOException ex) {
			throw new JailVMException("Unable to parse jail file.",ex);
		} catch (ParseException ex) {
			throw new JailVMException("Jail parsing failed.",ex);
		}
	}
	
	/** Registers some plugin. */
	public void register(String namespace, IJailVMToolkit toolkit) {
		toolkits.put(namespace, toolkit);
		toolkit.install(this);
	}
	
	/** Returns a plugin mapped to some namespace. */
	public IJailVMToolkit getToolkit(String namespace) {
		return toolkits.get(namespace);
	}
	
	/** Affects some value to a variable. */
	public void affect(String varName, Object value) {
		if (varName == null || value == null) {
			throw new IllegalArgumentException("varName as well as value are mandatory.");
		}
		memory.put(varName, value);
	}
	
	/** Returns the value mapped to name in memory. */
	public Object getVarValue(String name) {
		return memory.get(name);
	}
		
	/** Destroys a variable. */
	public void destroy(String varName) {
		memory.remove(varName);
	}

	/** Parses a literal. */
	public Object parseLiteral(String format, String literal) throws JailVMException {
		if ("dot".equals(format)) {
			try {
				return DOTDirectedGraphLoader.loadGraph(literal);
			} catch (ParseException e) {
				throw new JailVMException("Literal parsing failed",e);
			} catch (IOException e) {
				throw new JailVMException("Literal parsing failed",e);
			}
		} else {
			throw new JailVMException("Unknown literal format: " + format);
		}
	}
	
	/** Debugs the memory. */
	public void debugMemory() {
		for (String key: memory.keySet()) {
			Object value = memory.get(key);
			IPrintable adapted = (IPrintable) AdaptUtils.adapt(value,IPrintable.class);
			if (adapted != null) {
				System.out.println(key + ":");
				try { adapted.print(System.out); } catch (IOException e) {}
			} else {
				System.out.println(key + ": " + value.toString());
			}
		}
	}
	
	/** Executes a command on a specific toolkit. */
	public Object executeCommand(
			IJailVMToolkit toolkit, String command, 
			JailVMStack stack, Options options) throws JailVMException {
		if (!toolkit.hasCommand(command)) {
			throw new JailVMException("Unknown command: " + command);
		} else {
			return toolkit.executeCommand(command, this, stack, options);
		}		
	}

	/** Executes a named command. */
	public Object executeCommand(
			String namespace, String command, 
			JailVMStack stack, Options options) throws JailVMException {
		if (namespace != null) {
			IJailVMToolkit toolkit = getToolkit(namespace);
			return executeCommand(toolkit,command,stack,options);
		} else {
			// find the correct toolkit
			IJailVMToolkit found = null;
			for (IJailVMToolkit toolkit: toolkits.values()) {
				if (toolkit.hasCommand(command)) {
					if (found != null) {
						throw new JailVMException("Ambiguous command: " + command);
					} else {
						found = toolkit;
					}
				}
			}
			
			// execute command on found toolkit
			if (found == null) {
				throw new JailVMException("Unknown command: " + command);
			} else {
				return executeCommand(found,command,stack,options);
			}
		}
	}

	/** Registers an adaptation. */
	public void registerAdaptation(Class src, Class target, IAdapter adapter) {
		AdaptUtils.register(src, target, adapter);
	}

}
