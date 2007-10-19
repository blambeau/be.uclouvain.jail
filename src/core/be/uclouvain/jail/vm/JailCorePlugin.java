package be.uclouvain.jail.vm;

import java.io.File;
import java.io.IOException;

import net.chefbe.autogram.ast2.parsing.ParseException;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.io.IPrintable;
import be.uclouvain.jail.io.dot.DOTDirectedGraphLoader;

/**
 * JAIL Core plugin.
 * 
 * @author blambeau
 */
public class JailCorePlugin extends AbstractJailPlugin {

	/** Returns core. */
	public String namespace() {
		return "core";
	}

	/** Load a directed graph from a dot file. */
	public IDirectedGraph load(JailVMCommand command, JailVM vm) throws JailVMException {
		String path = command.getArgumentValue(0,vm);
		File file = assertReadableFile(path);
		try {
			return DOTDirectedGraphLoader.loadGraph(file);
		} catch (ParseException e) {
			throw new JailVMException("Parse exception when reading graph.",e);
		} catch (IOException e) {
			throw new JailVMException("Unable to read graph.",e);
		}
	}
	
	/** Prints something. */
	public void print(JailVMCommand command, JailVM vm) throws JailVMException {
		// retrieve graph.
		IDirectedGraph graph = command.getArgumentValue(0, vm);
		
		// transform to printable
		IPrintable printable = (IPrintable) graph.adapt(IPrintable.class);
		assertNotNull(printable,"Unable to print graph (no printer associated).");
		
		// print it
		try {
			printable.print(System.out);
		} catch (IOException e) {
			throw new JailVMException("Unable to print graph.",e);
		}
	}
	
}
