package be.uclouvain.jail.vm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.chefbe.autogram.ast2.parsing.ParseException;
import be.uclouvain.jail.adapt.AdaptUtils;
import be.uclouvain.jail.adapt.NetworkAdaptationTool;
import be.uclouvain.jail.dialect.IGraphDialect;
import be.uclouvain.jail.graph.IDirectedGraph;

/**
 * Core JAIL toolkit. 
 * 
 * @author blambeau
 */
public class JailCoreToolkit extends JailReflectionToolkit {

	/** Loaders by file extension. */
	private Map<String,IGraphDialect> loaders = new HashMap<String,IGraphDialect>();
	
	/** Installs the tollkit on a virtual machine. */
	public void install(JailVM vm) {
	}

	/** Registers an external loader. */
	protected void registerDialectLoader(String extension, IGraphDialect loader) {
		loaders.put(extension, loader);
	}
	
	/** Loads an object from a source using a given format. */
	protected Object load(Object source, String format) throws JailVMException {
		IGraphDialect loader = loaders.get(format);
		if (loader == null) {
			throw new JailVMException("Unknown loading format: " + format);
		}
		try {
			return loader.load(source, format);
		} catch (IOException e) {
			throw new JailVMException("Unable to load resource.",e);
		} catch (ParseException e) {
			throw new JailVMException("Unable to load resource.",e);
		}
	}
	
	/** Loads an object from a path. */
	public Object load(String path) throws JailVMException {
		// find file
		File f = new File(path);
		if (!f.exists()) {
			throw new JailVMException("Unable to find file: " + path);
		} else if (!f.canRead()) {
			throw new JailVMException("Unable to read file: " + path);
		}
		
		// format to use
		String extension = null;
		if (hasOption("format")) {
			extension = getOptionValue("format",String.class,null);
		} else {
			String name = f.getName();
			if (name.contains(".")) {
				extension = name.substring(name.lastIndexOf('.')+1);
			} else {
				throw new JailVMException("Unable to find extension of: " + path);
			}
		}
		
		// load it
		return load(f,extension);
	}
	
	/** Prints a jail resource. */
	protected Object print(Object[] sources, String format) throws JailVMException {
		IGraphDialect loader = loaders.get(format);
		if (loader == null) {
			throw new JailVMException("Unknown printing format: " + format);
		}
		try {
			for (Object source: sources) {
				loader.print(source, format, System.out);
			}
		} catch (IOException e) {
			throw new JailVMException("Unable to print resource.",e);
		}
		return sources[0];
	}
	
	/** Prints a jail resource. */
	public Object print(Object[] sources) throws JailVMException {
		String format = null;
		if (hasOption("format")) {
			format = getOptionValue("format",String.class,null);
		} else {
			throw new JailVMException("print usage: (print <G> :format <format>).");
		}
		return print(sources,format);
	}
	
	/** Saves a jail resource to a file. */
	public Object save(Object source, String path) throws JailVMException {
		try {
			// find file
			File f = new File(path);
			
			// format to use
			String extension = null;
			if (hasOption("format")) {
				extension = getOptionValue("format",String.class,null);
			} else {
				String name = f.getName();
				if (name.contains(".")) {
					extension = name.substring(name.lastIndexOf('.')+1);
				} else {
					throw new JailVMException("Unable to find extension of: " + path);
				}
			}
			
			// handle file writing
			if (!f.exists()) {
				if (!f.createNewFile()) {
					throw new JailVMException("Unable to create file: " + path);
				}
			} else if (!f.canWrite()) {
				throw new JailVMException("Unable to write file: " + path);
			}
			
			// find dialect
			IGraphDialect dialect = loaders.get(extension);
			if (dialect == null) {
				throw new JailVMException("Unknown printing format: " + extension);
			} else {
				FileOutputStream stream = new FileOutputStream(f);
				dialect.print(source, extension, stream);
				stream.flush();
				stream.close();
			}
			
			return source;
		} catch (IOException ex) {
			throw new JailVMException("Unable to save resource",ex);
		}
	}
	
	/** Returns adaptations of a source class. */
	public IDirectedGraph adaptations(Object source) {
		NetworkAdaptationTool tool = (NetworkAdaptationTool) AdaptUtils.getAdaptationTool();
		return tool.getAdaptationsOf(source.getClass());
	}

}
