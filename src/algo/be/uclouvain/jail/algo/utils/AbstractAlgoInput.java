package be.uclouvain.jail.algo.utils;

import be.uclouvain.jail.algo.commons.IAlgoInput;
import be.uclouvain.jail.vm.JailVMException;
import be.uclouvain.jail.vm.JailVMOptions;

/**
 * Helps for creation of {@IAlgoInput}.
 * 
 * @author blambeau
 */
public class AbstractAlgoInput implements IAlgoInput {

	/** Algo helper. */
	private AlgoHelper helper = new AlgoHelper(this);
	
	/** Options are installed? */
	private boolean installed;
	
	/** Returns the helper. */
	protected AlgoHelper getAlgoHelper() {
		return helper;
	}

	/** Installs the options. */
	public void setOptions(JailVMOptions options) throws JailVMException {
		if (!installed) {
			installed = true;
			installOptions();
		}
		helper.setOptions(options);
	}
	
	/** Install options. */
	protected void installOptions() {
	}

	/** Adds an option. */
	protected void addOption(String property, boolean mandatory, Class type, Object defValue) {
		helper.addOption(property, mandatory, type, defValue);
	}

	/** Adds an option. */
	protected void addOption(String optName, String property, boolean mandatory, Class type, Object defValue) {
		helper.addOption(optName, property, mandatory, type, defValue);
	}

}
