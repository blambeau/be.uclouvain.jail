/**
 * 
 */
package be.uclouvain.jail.dialect.dot;

import java.io.IOException;
import java.io.PrintWriter;

import net.chefbe.autogram.ast2.parsing.ParseException;
import net.chefbe.javautils.adapt.AdaptUtils;
import be.uclouvain.jail.dialect.IGraphDialect;
import be.uclouvain.jail.graph.IDirectedGraph;

/** Installs the DOT graph dialect. */
public class DOTGraphDialect implements IGraphDialect {

	/** Loads source in dot format. */
	public Object load(Object source, String format) throws IOException,
			ParseException {
		if ("dot".equals(format)) {
			return DOTDirectedGraphLoader.loadGraph(source);
		} else {
			throw new IllegalStateException(
					"Unknown format (not graphviz .dot): " + format);
		}
	}

	/** Prints source in dot format. */
	public void print(Object source, String format, PrintWriter stream)
			throws IOException {
		IDirectedGraph graph = (IDirectedGraph) AdaptUtils.adapt(source,
				IDirectedGraph.class);
		if (graph == null) {
			throw new IllegalStateException(
					"Source must be IDirectedGraph adaptable");
		}

		// print the graph
		new DOTDirectedGraphPrintable(graph).print(stream);
	}

}