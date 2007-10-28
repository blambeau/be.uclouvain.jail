package be.uclouvain.jail.vm;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import be.uclouvain.jail.dialect.IPrintable;

/**
 * Core JAIL toolkit. 
 * 
 * @author blambeau
 */
public class JailCoreToolkit extends JailReflectionToolkit {

	/** Loades by file extension. */
	private Map<String,IJailVMDialectLoader> loaders = new HashMap<String,IJailVMDialectLoader>();
	
	/** Installs the tollkit on a virtual machine. */
	public void install(JailVM vm) {
	}

	/** Registers an external loader. */
	public void registerDialectLoader(String extension, IJailVMDialectLoader loader) {
		loaders.put(extension, loader);
	}
	
	/** Loads an object from a source using a given format. */
	protected Object load(Object source, String format) throws JailVMException {
		IJailVMDialectLoader loader = loaders.get(format);
		if (loader == null) {
			throw new JailVMException("Unknown loading format: " + format);
		}
		return loader.load(source, format);
	}
	
	/** Loads an object from a path. */
	public Object load(String path) throws JailVMException {
		File f = new File(path);
		if (!f.exists()) {
			throw new JailVMException("Unable to find file: " + path);
		} else if (!f.canRead()) {
			throw new JailVMException("Unable to read file: " + path);
		}
		
		String name = f.getName();
		if (name.contains(".")) {
			String extension = name.substring(name.lastIndexOf('.')+1);
			return load(f,extension);
		} else {
			throw new JailVMException("Unable to find extension of: " + path);
		}
	}
	
	/** Prints a printable. */
	public IPrintable print(IPrintable p) throws JailVMException {
		try {
			p.print(System.out);
			return p;
		} catch (IOException e) {
			throw new JailVMException("Unable to print " + p,e);
		}
	}
	
}
