package be.uclouvain.jail.vm;

import java.util.HashMap;
import java.util.Map;

import be.uclouvain.jail.adapt.AdaptUtils;
import be.uclouvain.jail.vm.JailVMException.ERROR_TYPE;

/**
 * Encapsulates JAIL command language options.
 * 
 * @author blambeau
 */
public class JailVMOptions {

	/** Options as a map (name,value). */
	private Map<String,Object> options  = new HashMap<String,Object>();
	
	/** Returns true if an option has been explicited. */
	public boolean hasOption(String name) {
		return options.containsKey(name);
	}
	
	/** 
	 * Returns an option value by name. 
	 *
	 * @throws a JailVMException if the option is not present.
	 */
	public Object getOptionValue(String name) throws JailVMException {
		if (!hasOption(name)) {
			throw new JailVMException(ERROR_TYPE.BAD_COMMAND_USAGE,null);
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
				throw new JailVMException(ERROR_TYPE.BAD_COMMAND_USAGE,null);
			}
		} else {
			Object value = getOptionValue(name);
			if (type.isAssignableFrom(value.getClass())) {
				return (T) value;
			} 
			
			Object adapted = AdaptUtils.adapt(value,type);
			if (adapted == null) {
				throw new JailVMException(ERROR_TYPE.ADAPTABILITY_ERROR,null,"Unable to adapt option " + name + " to " + type.getSimpleName());
			}
			
			return (T) adapted;
		}
	}

	/** Sets an option value. */
	public void setOptionValue(String key, Object value) {
		options.put(key, value);
	}
	
}
