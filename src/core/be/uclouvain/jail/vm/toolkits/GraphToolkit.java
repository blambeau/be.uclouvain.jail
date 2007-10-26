package be.uclouvain.jail.vm.toolkits;

import java.io.IOException;

import be.uclouvain.jail.adapt.IAdaptable;
import be.uclouvain.jail.adapt.IAdapter;
import be.uclouvain.jail.dialect.IPrintable;
import be.uclouvain.jail.dialect.dot.DOTDirectedGraphPrintable;
import be.uclouvain.jail.dialect.dot.JDotty;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.vm.JailVM;
import be.uclouvain.jail.vm.JailVMException;
import be.uclouvain.jail.vm.ReflectionToolkit;

/** Provides a graph tolkit. */
public class GraphToolkit extends ReflectionToolkit implements IAdapter {

	/** Installs the toolkit on the virtual machine. */
	public void install(JailVM vm) {
		vm.registerAdaptation(IDirectedGraph.class, IPrintable.class, this);
		vm.registerAdaptation(DOTDirectedGraphPrintable.class, IDirectedGraph.class, this);
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
	
	/** Visualizes a graph using jdotty. */
	public IDirectedGraph jdotty(IDirectedGraph graph) throws JailVMException {
		try {
			new JDotty().present(graph);
		} catch (IOException e) {
			throw new JailVMException("Unable to present graph using jdotty: ",e);
		}
		return graph;
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
	public IDirectedGraph adaptToDirectedGraph(Object who) {
		if (who instanceof DOTDirectedGraphPrintable) {
			return (IDirectedGraph) ((IAdaptable)who).adapt(IDirectedGraph.class);
		} else {
			throw new IllegalStateException("Unable to convert " + who + " to a IDirectedGraph.");
		}
	}
	
	/** Adapts who to a printable. */
	public IPrintable adaptToPrintable(Object who) {
		if (who instanceof IDirectedGraph) {
			return new DOTDirectedGraphPrintable((IDirectedGraph)who);
		} else {
			throw new IllegalStateException("Unable to convert " + who + " to a IPrintable.");
		}
	}

}
