/**
 * 
 */
package be.uclouvain.jail.dialect.dot;

import java.io.IOException;
import java.io.PrintWriter;

import net.chefbe.autogram.ast2.parsing.ParseException;
import net.chefbe.javautils.adapt.AdaptUtils;
import be.uclouvain.jail.dialect.utils.AbstractGraphDialect;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.vm.JailVMOptions;

/** Installs the DOT graph dialect. */
public class DOTGraphDialect extends AbstractGraphDialect {

	/** Loads source in dot format. */
	public Object load(Object source, JailVMOptions options) throws IOException, ParseException {
		return DOTDirectedGraphLoader.loadGraph(source, this, options);
	}

	/** Prints source in dot format. */
	public void print(Object source, PrintWriter stream, JailVMOptions options) throws IOException {
		IDirectedGraph graph = (IDirectedGraph) AdaptUtils.adapt(source, IDirectedGraph.class);
		if (graph == null) {
			throw new IllegalStateException("Source must be IDirectedGraph adaptable");
		}
		new DOTDirectedGraphPrintable(graph).print(stream);
	}

}