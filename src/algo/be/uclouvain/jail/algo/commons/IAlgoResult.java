package be.uclouvain.jail.algo.commons;

import be.uclouvain.jail.vm.JailVMException;
import be.uclouvain.jail.vm.JailVMOptions;

/**
 * Defines the contract respected by all algorithm results.
 * 
 * @author blambeau
 */
public interface IAlgoResult {

	/** Install input informations from options. */
	public void setOptions(JailVMOptions options) throws JailVMException;
	
}
