package be.uclouvain.jail.vm;

import be.uclouvain.jail.vm.JailVMException.ERROR_TYPE;

/** 
 * Provides utilities to create commands.
 * 
 * @author blambeau
 */
public abstract class AbstractJailVMCommand implements IJailVMCommand {

	/** Command help. */
	private String help;
	
	/** Command signature. */
	private String signature;
	
	/** Returns command help. */
	public String getHelp() {
		ensureDoc();
		return help;
	}
	
	/** Sets command help. */
	public void setHelp(String help) {
		this.help = help;
	}
	
	/** Returns the command signature. */
	public String getSignature() {
		ensureDoc();
		return signature;
	}

	/** Sets command signature. */
	public void setSignature(String signature) {
		this.signature = signature;
	}

	/** Requests the parent toolkit to ensure its documentation. */
	private void ensureDoc() {
		IJailVMToolkit toolkit = getToolkit();
		if (toolkit instanceof AbstractJailToolkit) {
			((AbstractJailToolkit)toolkit).ensureDocumentation();
		} else {
			throw new IllegalStateException("AbstractJailToolkit expected.");
		}
	}

	/** Delegated to doExecute (install exception handling). */
	public final Object execute(JailVM vm, JailVMStack stack, JailVMOptions options) throws JailVMException {
		try {
			return doExecute(vm,stack,options);
		} catch (JailVMException ex) {
			ex.setCommand(this);
			throw ex;
		}
	}

	/** Same semantics as execute. */
	protected abstract Object doExecute(JailVM vm, JailVMStack stack, JailVMOptions options) throws JailVMException;

	/** Extracts an exception. */
	protected JailVMException extractException(Exception e) {
		if (e instanceof JailVMException) {
			return (JailVMException) e;
		}
		Throwable cause = e.getCause();
		if (cause instanceof JailVMException) {
			return (JailVMException) cause;
		} else {
			return new JailVMException(ERROR_TYPE.INTERNAL_ERROR,this,e);
		}
	}

}
