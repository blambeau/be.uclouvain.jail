package be.uclouvain.jail.vm;

import net.chefbe.autogram.ast2.IASTNode;
import be.uclouvain.jail.vm.autogram.JailCallback;

/** Installs documentation on a toolkit. */
public class JailVMDocumentationCallback extends JailCallback<Object> {

	/** Requesting toolkit. */
	private IJailVMToolkit toolkit;
	
	/** Creates a callback instance. */
	public JailVMDocumentationCallback(IJailVMToolkit toolkit) {
		this.toolkit = toolkit;
	}

	/** Callback method for NATIVEDOC nodes. */
	public Object NATIVEDOC(IASTNode node) throws Exception {
		super.recurseOnChildren(node);
		return null;
	}

	/** Callback method for DOC nodes. */
	public Object DOC(IASTNode node) throws Exception {
		String help = node.getAttrString("help");
		String signature = node.getAttrString("signature");
		String command = extractCommandName(signature);
		
		if (toolkit.hasCommand(command)) {
			IJailVMCommand c = toolkit.getCommand(command);
			if (c instanceof AbstractJailVMCommand) {
				((AbstractJailVMCommand)c).setHelp(help);
				((AbstractJailVMCommand)c).setSignature(signature);
			} else {
				throw new IllegalStateException("AbstractJailVMCommand expected, command: " + command);
			}
		} else {
			throw new IllegalStateException("Native documentation corrupted, unknown command: " + command);
		}
		return nonOverrided(node);
	}

	/** Extracts the command name from a signature. */
	public String extractCommandName(String signature) {
		int i = signature.indexOf('(')+1;
		int j = signature.indexOf(' ', i);
		return signature.substring(i,j);
	}
	
}
