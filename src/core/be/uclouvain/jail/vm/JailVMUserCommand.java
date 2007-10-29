package be.uclouvain.jail.vm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.chefbe.autogram.ast2.IASTNode;
import be.uclouvain.jail.vm.autogram.JailCallback;
import be.uclouvain.jail.vm.autogram.JailNodes;

/** User command. */
public class JailVMUserCommand extends JailCallback<Object> {

	/** Root node. */
	private IASTNode root;

	/** Jail Stack. */
	private JailVMStack stack;
	
	/** Virtual machine. */
	private JailVM vm;
	
	/** Creates a command for a DEFINE node. */
	public JailVMUserCommand(IASTNode node) {
		if (!JailNodes.DEFINE.equals(node.type())) {
			throw new IllegalArgumentException("DEFINE node expected, " + node.type() + " received.");
		}
		this.root = node;
	}
	
	/** Returns command name. */
	public String getName() {
		return root.childFor("header").getAttrString("name");
	}
	
	/** Executes the command. */
	public Object execute(
			String command, 
			JailVM vm, 
			JailVMStack stack, 
			JailVMOptions options) throws JailVMException {
		try {
			this.stack = stack;
			this.vm = vm;
			return root.accept(this);
		} catch (Exception e) {
			throw new JailVMException("Command " + command + " execution failed.",e);
		}
	}
	
	/** Callback method for DEFINE nodes. */
	public Object DEFINE(IASTNode node) throws Exception {
		JailVMCallback callback = (JailVMCallback) makeCall(node.childFor("header"));
		IASTNode body = node.childFor("body").childFor("op");
		return body.accept(callback);
	}

	/** Callback method for DEF_HEADER nodes. */
	@SuppressWarnings("unchecked")
	public Object DEF_HEADER(IASTNode node) throws Exception {
		// create variables
		Map<String,Object> params = new HashMap<String,Object>();

		// parse operands
		List<String> names = (List<String>) makeCall(node.childFor("operand"));
		if (stack.size() != names.size()) {
			throw new JailVMException("Invalid number of operands.");
		}
		for (String key: names) {
			params.put(key, stack.pop());
		}

		// parse options
		// TODO: implement options in user-defined commands
		
		return new JailVMCallback(vm,params);
	}

	/** Callback method for DEF_OPERANDS nodes. */
	public Object DEF_OPERANDS(IASTNode node) throws Exception {
		List<String> names = new ArrayList<String>();
		for (IASTNode child: node.children()) {
			names.add((String)makeCall(child));
		}
		return names;
	}

	/** Callback method for DEF_OPTIONS nodes. */
	public Object DEF_OPTIONS(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for DEF_OPTION nodes. */
	public Object DEF_OPTION(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for PHOLDERDEF nodes. */
	public Object PHOLDERDEF(IASTNode node) throws Exception {
		return node.getAttrString("name");
	}

}