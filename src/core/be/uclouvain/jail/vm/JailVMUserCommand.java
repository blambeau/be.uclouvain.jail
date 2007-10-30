package be.uclouvain.jail.vm;

import java.util.ArrayList;
import java.util.List;

import net.chefbe.autogram.ast2.IASTNode;
import be.uclouvain.jail.vm.JailVMException.ERROR_TYPE;
import be.uclouvain.jail.vm.autogram.JailCallback;
import be.uclouvain.jail.vm.autogram.JailNodes;

/** User-defined command bootstraping. */
public class JailVMUserCommand extends AbstractJailVMCommand {

	/** Root node. */
	private IASTNode root;

	/** Attached toolkit. */
	private IJailVMToolkit toolkit;
	
	/** Creates a command for a DEFINE node. */
	public JailVMUserCommand(IASTNode node, IJailVMToolkit toolkit) {
		if (!JailNodes.DEFINE.equals(node.type())) {
			throw new IllegalArgumentException("DEFINE node expected, " + node.type() + " received.");
		}
		this.root = node;
		this.toolkit = toolkit;
	}
	
	/** Executes the command on the virtual machine. */
	public Object doExecute(JailVM vm, JailVMStack stack, JailVMOptions options) throws JailVMException {
		try {
			return root.accept(new CommandCallback(vm,stack,options));
		} catch (Exception e) {
			throw super.extractException(e);
		}
	}

	/** Returns the toolkit. */
	public IJailVMToolkit getToolkit() {
		return toolkit;
	}

	/** Returns command name. */
	public String getName() {
		return root.childFor("header").getAttrString("name");
	}
	
	/** Command execution as a Callback on Jail AST. */
	class CommandCallback extends JailCallback<Object>  {
	
		/** Jail Stack. */
		private JailVMStack stack;
		
		/** Virtual machine. */
		private JailVM vm;
		
		/** Options. */
		@SuppressWarnings("unused")
		private JailVMOptions options;
		
		/** Creates a command for a DEFINE node. */
		public CommandCallback(JailVM vm, JailVMStack stack, JailVMOptions options) {
			this.stack = stack;
			this.vm = vm;
			this.options = options;
		}
		
		/** Callback method for DEFINE nodes. */
		@Override
		public Object DEFINE(IASTNode node) throws Exception {
			JailVMCallback callback = (JailVMCallback) makeCall(node.childFor("header"));
			IASTNode body = node.childFor("body").childFor("op");
			return body.accept(callback);
		}
	
		/** Callback method for DEF_HEADER nodes. */
		@SuppressWarnings("unchecked")
		@Override
		public Object DEF_HEADER(IASTNode node) throws Exception {
			// create variables
			IJailVMScope scope = new JailVMMapScope();
	
			// parse operands
			List<String> names = (List<String>) makeCall(node.childFor("operand"));
			if (stack.size() != names.size()) {
				throw new JailVMException(ERROR_TYPE.BAD_COMMAND_USAGE,JailVMUserCommand.this);
			}
			for (String key: names) {
				scope.affect(key, stack.pop());
			}
	
			// parse options
			// TODO: implement options in user-defined commands
			
			return new JailVMCallback(vm,scope);
		}
	
		/** Callback method for DEF_OPERANDS nodes. */
		@Override
		public Object DEF_OPERANDS(IASTNode node) throws Exception {
			List<String> names = new ArrayList<String>();
			for (IASTNode child: node.children()) {
				names.add((String)makeCall(child));
			}
			return names;
		}
	
		/** Callback method for DEF_OPTIONS nodes. */
		@Override
		public Object DEF_OPTIONS(IASTNode node) throws Exception {
			return nonOverrided(node);
		}
	
		/** Callback method for DEF_OPTION nodes. */
		@Override
		public Object DEF_OPTION(IASTNode node) throws Exception {
			return nonOverrided(node);
		}
	
		/** Callback method for PHOLDERDEF nodes. */
		@Override
		public Object PHOLDERDEF(IASTNode node) throws Exception {
			return node.getAttrString("name");
		}

	}
	
}