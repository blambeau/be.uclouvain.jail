package be.uclouvain.jail.vm;

import java.util.List;

import net.chefbe.autogram.ast2.IASTNode;
import be.uclouvain.jail.adapt.AdaptUtils;
import be.uclouvain.jail.dialect.IPrintable;
import be.uclouvain.jail.vm.autogram.JailCallback;

/**
 * Executes a JAIL Abstract Syntax Tree on a virtual machine.
 * 
 * @author blambeau
 */
public class JailVMCallback extends JailCallback<Object> {

	/** The virtual machine. */
	private JailVM vm;
	
	/** Creates a callback instance. */
	public JailVMCallback(JailVM vm) {
		this.vm = vm;
	}
	
	/** Concats some children results. */
	@Override
	protected Object concat(List<Object> arg0) {
		if (arg0 == null) { return null; }
		return arg0.toArray();
	}

	/** Callback method for UNIT nodes. */
	public Object UNIT(IASTNode node) throws Exception {
		super.recurseOnChildren(node);
		return null;
	}

	/** Callback method for AFFECTATION nodes. */
	public Object AFFECTATION(IASTNode node) throws Exception {
		String name = node.getAttrString("var");
		Object value = makeCall(node.childFor("value"));
		vm.affect(name, value);
		return null;
	}

	/** Callback method for SHOW nodes. */
	public Object SHOW(IASTNode node) throws Exception {
		Object toShow = makeCall(node.childFor("value"));
		IPrintable printable = (IPrintable) AdaptUtils.adapt(toShow,IPrintable.class);
		if (printable != null) {
			printable.print(System.out);
		}
		return toShow;
	}

	/** Callback method for GLITERAL nodes. */
	public Object GLITERAL(IASTNode node) throws Exception {
		String format = node.getAttrString("format");
		String literal = node.getAttrString("literal");
		return vm.parseLiteral(format,literal);
	}

	/** Callback method for GOPERATOR nodes. */
	public Object GOPERATOR(IASTNode node) throws Exception {
		String name = node.getAttrString("name");
		JailVMStack stack = (JailVMStack) makeCall(node.childFor("operands"));
		IASTNode optNode = node.childFor("options");
		Options options = optNode == null ? null : (Options) makeCall(optNode);
		return vm.executeCommand((String)null,name,stack,options);
	}

	/** Callback method for GOPERANDS nodes. */
	public Object GOPERANDS(IASTNode node) throws Exception {
		Object[] values = (Object[]) recurseOnChildren(node);
		return new JailVMStack(values);
	}
	
	/** Callback method for GOPERAND nodes. */
	public Object GOPERAND(IASTNode node) throws Exception {
		return makeCall(node.childFor("expr"));
	}

	/** Callback method for OPTIONS nodes. */
	public Object OPTIONS(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for OPTION nodes. */
	public Object OPTION(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for ATTRS_EXPR nodes. */
	public Object ATTRS_EXPR(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for ATTR_AFFECT nodes. */
	public Object ATTR_AFFECT(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for ATTRREF nodes. */
	public Object ATTRREF(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for VARREF nodes. */
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
