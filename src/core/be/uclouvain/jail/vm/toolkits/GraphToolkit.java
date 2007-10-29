package be.uclouvain.jail.vm.toolkits;

import java.io.IOException;

import be.uclouvain.jail.adapt.IAdaptable;
import be.uclouvain.jail.adapt.IAdapter;
import be.uclouvain.jail.algo.graph.copy.DirectedGraphCopier;
import be.uclouvain.jail.algo.graph.copy.match.GMatchPopulator;
import be.uclouvain.jail.dialect.IPrintable;
import be.uclouvain.jail.dialect.dot.DOTDirectedGraphPrintable;
import be.uclouvain.jail.dialect.dot.DOTGraphDialect;
import be.uclouvain.jail.dialect.dot.JDotty;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.adjacency.AdjacencyDirectedGraph;
import be.uclouvain.jail.graph.utils.DirectedGraphWriter;
import be.uclouvain.jail.vm.JailReflectionToolkit;
import be.uclouvain.jail.vm.JailVM;
import be.uclouvain.jail.vm.JailVMException;

/** Provides a graph tolkit. */
public class GraphToolkit extends JailReflectionToolkit implements IAdapter {

	/** Installs the toolkit on the virtual machine. */
	public void install(JailVM vm) {
		vm.registerAdaptation(IDirectedGraph.class, IPrintable.class, this);
		vm.registerAdaptation(DOTDirectedGraphPrintable.class, IDirectedGraph.class, this);
		
		// register dot dialect
		vm.registerDialectLoader("dot", new DOTGraphDialect());
	}

	/** Visualizes a graph using jdotty. */
	public IDirectedGraph jdotty(IDirectedGraph graph) throws JailVMException {
		try {
			new JDotty().present(graph);
		} catch (IOException e) {
			throw new JailVMException("Unable to present graph using jdotty: ",e);
		}
		return graph;
	}
	
	/** Copies a graph. */
	public IDirectedGraph copy(IDirectedGraph graph) throws JailVMException {
		AdjacencyDirectedGraph copy = new AdjacencyDirectedGraph();
		DirectedGraphWriter writer = new DirectedGraphWriter(copy);

		// add state populator
		if (hasOption("state")) {
			GMatchPopulator populator = getOptionValue("state",GMatchPopulator.class,null);
			writer.getVertexCopier().addPopulator(populator);
		}
		
		// add edge populator
		if (hasOption("edge")) {
			GMatchPopulator populator = getOptionValue("edge",GMatchPopulator.class,null);
			writer.getEdgeCopier().addPopulator(populator);
		}
		
		DirectedGraphCopier.copy(graph,writer);
		return copy;
	}
	
	/** Adapts who to the requested type. */
	public Object adapt(Object who, Class type) {
		if (IPrintable.class.equals(type)) {
			return adaptToPrintable(who);
		} else if (IDirectedGraph.class.equals(type)) {
			return adaptToDirectedGraph(who);
		}
		return null;
	}
	
	/** Adapts an object to a directed graph. */
	private IDirectedGraph adaptToDirectedGraph(Object who) {
		if (who instanceof DOTDirectedGraphPrintable) {
			return (IDirectedGraph) ((IAdaptable)who).adapt(IDirectedGraph.class);
		} else {
			throw new IllegalStateException("Unable to convert " + who + " to a IDirectedGraph.");
		}
	}
	
	/** Adapts who to a printable. */
	private IPrintable adaptToPrintable(Object who) {
		if (who instanceof IDirectedGraph) {
			return new DOTDirectedGraphPrintable((IDirectedGraph)who);
		} else {
			throw new IllegalStateException("Unable to convert " + who + " to a IPrintable.");
		}
	}

}
