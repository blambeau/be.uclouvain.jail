package be.uclouvain.jail.vm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import net.chefbe.autogram.ast2.parsing.ParseException;
import net.chefbe.javautils.adapt.AdaptUtils;
import net.chefbe.javautils.adapt.IAdapter;
import net.chefbe.javautils.adapt.NetworkAdaptationTool;
import net.chefbe.javautils.adapt.StringAdaptationTool;
import be.uclouvain.jail.adapt.NetworkGraphDecorator;
import be.uclouvain.jail.dialect.IGraphDialect;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.vm.JailVMException.ERROR_TYPE;

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
		vm.registerAdaptation(String.class, Class.class, new IAdapter() {
			public Object adapt(Object who, Class type) {
				if (type.equals(Class.class) && who != null) {
					try {
						return Class.forName(who.toString());
					} catch (ClassNotFoundException e) {
						return null;
					}
				}
				return null;
			}
		});
		
		vm.registerAdaptation(String.class, Boolean.class, StringAdaptationTool.TO_BOOL);
		vm.registerAdaptation(String.class, Integer.class, StringAdaptationTool.TO_INT);
		vm.registerAdaptation(String.class, Double.class, StringAdaptationTool.TO_DOUBLE);
	}

	/** Registers an external loader. */
	protected void registerDialectLoader(String extension, IGraphDialect loader) {
		loaders.put(extension, loader);
	}
	
	/** Ensures a file instance. */
	private File ensureFileAccess(String path, String mode, boolean create) throws JailVMException {
		if (path == null) { 
			throw new JailVMException(JailVMException.ERROR_TYPE.FILE_ACCESS_ERROR,null,"Unable to ensure file access on null path."); 
		}
		// find file
		File f = new File(path);
		if (!f.exists()) {
			if (!create) {
				throw new JailVMException(ERROR_TYPE.FILE_ACCESS_ERROR,null,"Unable to find file: " + path);
			} else {
				try {
					f.createNewFile();
				} catch (IOException ex) {
					throw new JailVMException(ERROR_TYPE.FILE_ACCESS_ERROR,null,"Unable to create file: " + path,ex);
				}
			}
		} else if (!f.canRead()) {
			throw new JailVMException(ERROR_TYPE.FILE_ACCESS_ERROR,null,"Unable to read file: " + path);
		}
		return f;
	}
	
	/** Ensures that a dialect is recognized. */
	private IGraphDialect ensureDialect(String name) throws JailVMException {
		IGraphDialect dialect = loaders.get(name);
		if (dialect == null) {
			throw JailVMException.unknownDialect(name);
		} else {
			return dialect;
		}
	}
	
	/** Executes jail commands in a file. */
	public void execute(String path, JailVM vm) throws JailVMException {
		// check PRE
		if (path == null) { 
			throw new JailVMException(JailVMException.ERROR_TYPE.FILE_ACCESS_ERROR,null,"Unable to ensure file access on null path."); 
		}

		// ask the virtual machine to resolve the path
		String rpath = vm.resolvePath(path);

		// not successfull ? throw an exception
		if (rpath == null) { 
			throw new JailVMException(JailVMException.ERROR_TYPE.FILE_ACCESS_ERROR,null,"Unable to resolve file on read access: " + path); 
		}

		File f = ensureFileAccess(rpath,"r",false);
		vm.execute(f);
	}
	
	/** Loads an object from a source using a given format. */
	protected Object load(Object source, String format) throws JailVMException {
		IGraphDialect loader = ensureDialect(format);
		try {
			return loader.load(source, null);
		} catch (IOException e) {
			throw new JailVMException(ERROR_TYPE.FILE_ACCESS_ERROR,null,"Unable to read resource: " + source,e);
		} catch (ParseException e) {
			throw new JailVMException(ERROR_TYPE.PARSE_ERROR,null,e);
		}
	}
	
	/** Loads an object from a path. */
	public Object load(String path, JailVMOptions options) throws JailVMException {
		File f = ensureFileAccess(path,"r",false);
		
		// format to use
		String extension = null;
		if (options.hasOption("format")) {
			extension = options.getOptionValue("format",String.class,null);
		} else {
			String name = f.getName();
			if (name.contains(".")) {
				extension = name.substring(name.lastIndexOf('.')+1);
			} else {
				throw new JailVMException(ERROR_TYPE.FILE_ACCESS_ERROR,null,"Unable to autodetect dialect: " + path);
			}
		}
		
		// load it
		return load(f,extension);
	}
	
	/** Prints a jail resource. */
	protected Object print(Object[] sources, String format, JailVM vm) throws JailVMException {
		IGraphDialect loader = ensureDialect(format);
		try {
			for (Object source: sources) {
				PrintWriter writer = vm.getEnvironment().getConsoleWriter();
				loader.print(source, writer, null);
				writer.flush();
			}
		} catch (IOException e) {
			throw new JailVMException(ERROR_TYPE.INTERNAL_ERROR,null,"Unable to print resource.",e);
		}
		return sources[0];
	}
	
	/** Prints a jail resource. */
	public Object print(Object[] sources, JailVM vm, JailVMOptions options) throws JailVMException {
		String format = null;
		if (options.hasOption("format")) {
			format = options.getOptionValue("format",String.class,null);
		} else {
			throw new JailVMException(ERROR_TYPE.BAD_COMMAND_USAGE,null);
		}
		return print(sources,format,vm);
	}
	
	/** Saves a jail resource to a file. */
	public Object save(Object source, String path, JailVMOptions options) throws JailVMException {
		try {
			// format to use
			String extension = null;
			if (options.hasOption("format")) {
				extension = options.getOptionValue("format",String.class,null);
			} else {
				if (path.contains(".")) {
					extension = path.substring(path.lastIndexOf('.')+1);
				} else {
					throw new JailVMException(ERROR_TYPE.FILE_ACCESS_ERROR,null,"Unable to autodetect dialect: " + path);
				}
			}
			
			// find dialect
			IGraphDialect dialect = ensureDialect(extension);
			
			// handle file writing
			File f = ensureFileAccess(path,"w",true);
			
			// create 
			PrintWriter writer = new PrintWriter(new FileWriter(f));
			dialect.print(source, writer, null);
			writer.flush();
			writer.close();
			
			return source;
		} catch (IOException ex) {
			throw new JailVMException(ERROR_TYPE.FILE_ACCESS_ERROR,null,"Unable to write file: " + path,ex);
		}
	}

	/** Adapts a source to some specific type. */
	@SuppressWarnings("unchecked")
	public Object cast(Object source, Class to) throws JailVMException {
		Object result = AdaptUtils.adapt(source, to);
		if (result == null) {
			throw new JailVMException(ERROR_TYPE.ADAPTABILITY_ERROR,null,"Unable to adapt to " + to.getSimpleName());
		} else {
			return result;
		}
	}
	
	/** Returns adaptations of a source class. */
	public IDirectedGraph adaptations(Object source) {
		NetworkAdaptationTool tool = (NetworkAdaptationTool) AdaptUtils.getAdaptationTool();
		return new NetworkGraphDecorator(tool.getGraph());
	}

}
