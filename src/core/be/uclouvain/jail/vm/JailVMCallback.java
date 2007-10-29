package be.uclouvain.jail.vm;

import java.util.List;
import java.util.Map;

import net.chefbe.autogram.ast2.IASTNode;
import be.uclouvain.jail.algo.graph.copy.match.GMatchPopulator;
import be.uclouvain.jail.vm.autogram.JailCallback;

/**
 * Executes a JAIL Abstract Syntax Tree on a virtual machine.
 * 
 * @author blambeau
 */
public class JailVMCallback extends JailCallback<Object> {

	/** The virtual machine. */
	private JailVM vm;
	
	/** Placeholder variables. */
	private Map<String,Object> variables;
	
	/** Creates a callback instance. */
	public JailVMCallback(JailVM vm) {
		this(vm,null);
	}
	
	/** Installs a sub callback. */
	public JailVMCallback(JailVM vm, Map<String,Object> variables) {
		this.vm = vm;
		this.variables = variables;
	}
	
	/** Concats some children results. */
	@Override
	protected Object concat(List<Object> arg0) {
		if (arg0 == null) { return null; }
		return arg0.toArray();
	}

	/** Callback method for UNIT nodes. */
	@Override
	public Object UNIT(IASTNode node) throws Exception {
		super.recurseOnChildren(node);
		return null;
	}

	/** Callback method for AFFECTATION nodes. */
	@Override
	public Object AFFECTATION(IASTNode node) throws Exception {
		String name = node.getAttrString("var");
		Object value = makeCall(node.childFor("value"));
		vm.affect(name, value);
		return null;
	}

	/** Callback method for SHOW nodes. */
	@Override
	public Object SHOW(IASTNode node) throws Exception {
		Object toShow = makeCall(node.childFor("value"));
		/*
		IPrintable printable = (IPrintable) AdaptUtils.adapt(toShow,IPrintable.class);
		if (printable != null) {
			printable.print(System.out);
		}
		*/
		return toShow;
	}

	/** Callback method for DEFINE nodes. */
	@Override
	public Object DEFINE(IASTNode node) throws Exception {
		JailVMUserCommand command = new JailVMUserCommand(node);
		vm.defineUserCommand(command);
		return null;
	}

	/** Callback method for GLITERAL nodes. */
	@Override
	public Object GLITERAL(IASTNode node) throws Exception {
		String format = node.getAttrString("format");
		String literal = node.getAttrString("literal");
		return vm.getCoreToolkit().load(literal, format);
	}

	/** Callback method for GOPERATOR nodes. */
	@Override
	public Object GOPERATOR(IASTNode node) throws Exception {
		String name = node.getAttrString("name");
		JailVMStack stack = (JailVMStack) makeCall(node.childFor("operands"));
		IASTNode optNode = node.childFor("options");
		JailVMOptions options = optNode == null ? null : (JailVMOptions) makeCall(optNode);
		return vm.executeCommand((String)null,name,stack,options);
	}

	/** Callback method for GOPERANDS nodes. */
	@Override
	public Object GOPERANDS(IASTNode node) throws Exception {
		Object[] values = (Object[]) recurseOnChildren(node);
		return new JailVMStack(values);
	}
	
	/** Callback method for GOPERAND nodes. */
	@Override
	public Object GOPERAND(IASTNode node) throws Exception {
		return makeCall(node.childFor("expr"));
	}

	/** Callback method for OPTIONS nodes. */
	@Override
	public Object OPTIONS(IASTNode node) throws Exception {
		JailVMOptions options = new JailVMOptions();
		for (IASTNode child: node.children()) {
			Object[] keyValue = (Object[]) makeCall(child);
			options.setOptionValue((String)keyValue[0],keyValue[1]);
		}
		return options;
	}

	/** Callback method for OPTION nodes. */
	@Override
	public Object OPTION(IASTNode node) throws Exception {
		String key = node.getAttrString("name");
		Object value = makeCall(node.childFor("value"));
		return new Object[]{key,value};
	}

	/** Callback method for OPTMATCH nodes. */
	@Override
	public Object OPTMATCH(IASTNode node) throws Exception {
		return new GMatchPopulator(node.childFor("match"));
	}

	/** Callback method for OPTLITERAL nodes. */
	@Override
	public Object OPTLITERAL(IASTNode node) throws Exception {
		return node.getAttr("value");
	}

	/** Callback method for LITERAL nodes. */
	@Override
	public Object LITERAL(IASTNode node) throws Exception {
		return node.getAttr("value");
	}

	/** Callback method for PHOLDERREF nodes. */
	public Object PHOLDERREF(IASTNode node) throws Exception {
		String name = node.getAttrString("name");
		if (variables != null && variables.containsKey(name)) {
			return variables.get(name);
		} else {
			throw new JailVMException("Unbounded placeholer: " + name);
		}
	}

	/** Callback method for VARREF nodes. */
	@Override
	public Object VARREF(IASTNode node) throws Exception {
		String name = node.getAttrString("var");
		Object value = vm.getVarValue(name);
		if (value == null) { 
			throw new JailVMException("Unknown variable " + name); 
		} else {
			return value;
		}
	}

}
