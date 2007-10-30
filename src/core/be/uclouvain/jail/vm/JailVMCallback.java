package be.uclouvain.jail.vm;

import java.util.ArrayList;
import java.util.List;

import net.chefbe.autogram.ast2.IASTNode;
import be.uclouvain.jail.adapt.IAdaptable;
import be.uclouvain.jail.algo.graph.copy.match.GMatchAggregator;
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
	
	/** Scoping. */
	private IJailVMScope scope;
	
	/** Creates a callback instance. */
	public JailVMCallback(JailVM vm) {
		this(vm,vm);
	}
	
	/** Installs a sub callback. */
	public JailVMCallback(JailVM vm, IJailVMScope scope) {
		this.vm = vm;
		this.scope = scope;
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

	/** Callback method for SYSTEMC nodes. */
	public Object SYSTEMC(IASTNode node) throws Exception {
		String name = node.getAttrString("name");
		List<Object> args = new ArrayList<Object>();
		for (IASTNode child: node.children()) {
			Object value = makeCall(child);
			if (value != null) args.add(value);
		}
		vm.executeSystemCommand(name,args.toArray());
		return null;
	}

	/** Callback method for SYSTEMARG nodes. */
	public Object SYSTEMARG(IASTNode node) throws Exception {
		return node.getAttr("value");
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
		JailVMUserCommand command = new JailVMUserCommand(node,vm.getCoreToolkit());
		vm.addCommand(command);
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
	public Object OPTMATCH(final IASTNode node) throws Exception {
		return new IAdaptable() {
			/** Adapts to populator or aggregator. */
			public <T> Object adapt(Class<T> c) {
				if (GMatchPopulator.class.equals(c)) {
					return new GMatchPopulator(node.childFor("match"));
				} else if (GMatchAggregator.class.equals(c)) {
					return new GMatchAggregator(node.childFor("match"));
				} else return null;
			}
		};
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

	/** Callback method for VARREF nodes. */
	@Override
	public Object VARREF(IASTNode node) throws Exception {
		String name = node.getAttrString("var");
		if (scope.knows(name)) {
			return scope.valueOf(name);
		} else {
			throw JailVMException.unknownVariable(name); 
		}
	}

}
