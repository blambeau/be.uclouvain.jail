package be.uclouvain.jail;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;

import be.uclouvain.jail.adapt.AdaptUtils;
import be.uclouvain.jail.adapt.CreateClassAdapter;
import be.uclouvain.jail.graph.adjacency.AdjacencyDirectedGraph;
import be.uclouvain.jail.io.IPrintable;
import be.uclouvain.jail.io.dot.DirectedGraphPrintable;

/** 
 * JAIL main class. 
 *
 * <p>This class provides the jail command line program and acts as the 
 * repository for all JAIL properties.</p> 
 */
public class Jail {

	/** JAIL properties. */
	private static Map<String,Object> properties = new HashMap<String,Object>();
	
	/** Installs jail. */
	public static void install() {}
	
	/** Installs core adapters. */
	static {
		AdaptUtils.register(AdjacencyDirectedGraph.class,IPrintable.class,
				new CreateClassAdapter(DirectedGraphPrintable.class));
	}
	
	/** Sets a JAIL property. */
	public static void setProperty(String key, Object value) {
		properties.put(key,value);
	}

	/** Returns a property. */
	public static Object getProperty(String key, Object def) {
		Object value = properties.get(key);
		return value == null ? def: value;
	}

	/** Returns an int property. */
	public static Boolean getBoolProperty(String key, Boolean def) {
		Object prop = getProperty(key,def);
		return (Boolean) ((prop instanceof Boolean) ?  prop : 
			ConvertUtils.convert(prop.toString(), Boolean.class));
	}

	/** Returns an int property. */
	public static Integer getIntProperty(String key, Integer def) {
		Object prop = getProperty(key,def);
		return (Integer) ((prop instanceof Integer) ?  prop : 
			ConvertUtils.convert(prop.toString(), Integer.class));
	}

	/** Returns a double property. */
	public static Double getDoubleProperty(String key, Double def) {
		Object prop = getProperty(key,def);
		return (Double) ((prop instanceof Double) ?  prop : 
			ConvertUtils.convert(prop.toString(), Double.class));
	}
	
	/** Returns a float property. */
	public static Float getFloatProperty(String key, Float def) {
		Object prop = getProperty(key,def);
		return (Float) ((prop instanceof Float) ?  prop : 
			ConvertUtils.convert(prop.toString(), Float.class));
	}
	
	/** Returns a string property. */
	public static String getStringProperty(String key, String def) {
		Object prop = getProperty(key,def);
		return prop == null ? def : prop.toString();
	}
	
}
