package be.uclouvain.jail.algo.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

import be.uclouvain.jail.vm.JailVMException;
import be.uclouvain.jail.vm.JailVMOptions;

/**
 * Helps with options.
 * 
 * @author blambeau
 */
public class AlgoHelper {

	/** Option information. */
	static class OptionInfo {
		
		/** Option name. */
		private String optName;

		/** Property inside the class. */
		private String property;
		
		/** Mandatory option? */
		private boolean mandatory;

		/** Type of the value. */
		private Class type;

		/** Default option value. */
		private Object defValue;
		
		/** Creates an option info. */
		public OptionInfo(String optName, String property, 
				          boolean mandatory, Class type, Object defValue) {
			this.optName = optName;
			this.property = property;
			this.mandatory = mandatory;
			this.type = type;
			this.defValue = defValue;
		}
		
	}
	
	/** Helped instance. */
	private Object helped;
	
	/** Informations about options. */
	private List<OptionInfo> infos;
	
	/** Creates an helper instance. */
	public AlgoHelper(Object helped) {
		this.helped = helped;
	}

	/** Handles an exception when installing an option. */
	private void handleException(Exception e, OptionInfo info, Object value) throws JailVMException {
		throw new JailVMException(
				JailVMException.ERROR_TYPE.INTERNAL_ERROR,
				null,
				"Error when setting option value automatically " + info
				+ " :: " + value,
				e);
	}
	
	/** Installs the options. */
	public void setOptions(JailVMOptions options) throws JailVMException {
		if (infos == null) { return; }
		for (OptionInfo info: infos) {
			// bypass non mandatory, unpresent options when no default value
			if (!options.hasOption(info.optName) && 
				!info.mandatory && 
				info.defValue==null) {
				continue;
			}
			
			// get value 
			Object value = options.getOptionValue(info.optName, info.type, info.defValue);

			// affect it by reflection
			try {
				PropertyUtils.setProperty(helped,info.property,value);
			} catch (IllegalAccessException e) {
				handleException(e,info,value);
			} catch (InvocationTargetException e) {
				handleException(e,info,value);
			} catch (NoSuchMethodException e) {
				handleException(e,info,value);
			}
		}
	}

	/** Adds an option. */
	public void addOption(String optName, String property, 
			                 boolean mandatory, Class type, Object defValue) {
		// lazy load of options
		if (infos == null) {
			infos = new ArrayList<OptionInfo>();
		}
		// add info
		this.infos.add(new OptionInfo(optName, property, mandatory, type, defValue));
	}
	
	/** Adds an option. */
	public void addOption(String property, boolean mandatory, Class type, Object defValue) {
		addOption(property, property, mandatory, type, defValue);
	}
	
}
