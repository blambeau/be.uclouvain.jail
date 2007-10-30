package be.uclouvain.jail;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import jline.ConsoleReader;

import org.apache.commons.beanutils.ConvertUtils;

import be.uclouvain.jail.vm.IJailVMEnvironment;
import be.uclouvain.jail.vm.JailVM;
import be.uclouvain.jail.vm.JailVMException;

/** 
 * JAIL main class. 
 *
 * <p>This class provides the jail command line program and acts as the 
 * repository for all JAIL properties.</p> 
 */
public class Jail implements IJailVMEnvironment {

	/** JAIL properties. */
	private static Map<String,Object> properties = new HashMap<String,Object>();
	
	/** Installs jail. */
	public static void install() {}
	
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
	
	/** Out writer to use. */
	private PrintWriter out;
	
	/** Starts the virtual machine. */
	public void startVM(String jailFile) throws IOException {
		// create the virtual machine
		JailVM vm = new JailVM(this);

		// create a in and out
		ConsoleReader reader = new ConsoleReader();
        out = new PrintWriter(System.out);
        
        // executes jailFile if any
        if (jailFile != null) {
        	try {
				vm.execute(new File(jailFile));
			} catch (JailVMException e) {
				out.println("Unable to execute " + jailFile + " file: " + e.getMessage());
			}
        }
        
		// parse all lines
		String line;
        while ((line = reader.readLine("jail> ")) != null) {
        	if ("\\q".equals(line.trim())) {
        		System.exit(0);
        	}
        	try {
        		vm.execute(line + "\n");
        	} catch (JailVMException ex) {
        		out.println("Fatal error: " + ex.getMessage());
        	}
        }		
	}
	
	/** Starts the Jail VM on a file. */
	public static void main(String[] args) throws Exception {
		try {
			new Jail().startVM(args.length==1 ? args[0] : null);
		} catch (IOException ex) {
			System.out.println("An IOException occured when starting Jail.");
			ex.printStackTrace();
		}
	}

	/** Handles an error. */
	public void handleError(Throwable t) {
		out.println(t.getMessage());
		out.println();
		out.flush();

		if (t instanceof JailVMException) {
			JailVMException ex = (JailVMException) t;
			if (JailVMException.ERROR_TYPE.INTERNAL_ERROR.equals(ex.getType())) {
				ex.printStackTrace(out);
				out.flush();
			}
		}
	}

	/** Prints to the console. */
	public void printConsole(String message, LEVEL level) {
		out.println(message);
		out.println();
		out.flush();
	}
	
}
