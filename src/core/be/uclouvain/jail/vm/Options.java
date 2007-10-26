package be.uclouvain.jail.vm;

import java.util.Map;

import be.uclouvain.jail.adapt.AdaptUtils;

/**
 * Encapsulates JAIL command language options.
 * 
 * @author blambeau
 */
public class Options {

	/** Options as a map (name,value). */
	private Map<String,Object> options;
	
	/** Returns true if an option has been explicited. */
	public boolean hasOption(String name) {
		return options.containsKey(name);
	}
	
	/** 
	 * Returns an option value by name. 
	 *
	 * @thows a JailVMException if the option is not present.
	 */
	public Object getOptionValue(String name) throws JailVMException {
		if (!hasOption(name)) {
			throw new JailVMException("Option required: " + name);
		} else  {
			return options.get(name);
		}
	}

	/** Finds an option value, checking its type. */ 
	@SuppressWarnings("unchecked")
	public <T> T getOptionValue(String name, Class type, T def) throws JailVMException {
		if (!hasOption(name)) {
			if (def != null) {
				return def;
			} else {
				throw new JailVMException("Option required: " + name);
			}
		} else {
			Object value = getOptionValue(name);
			if (type.isAssignableFrom(value.getClass())) {
				return (T) value;
			} 
			
			Object adapted = AdaptUtils.adapt(value,type);
			if (adapted == null) {
				throw new JailVMException("Unable to convert " + value + " to " + type.getSimpleName());
			}
			
			return (T) adapted;
		}
	}
	
}
