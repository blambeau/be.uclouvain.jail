package be.uclouvain.jail.vm.toolkits;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import net.chefbe.javautils.adapt.IAdaptable;
import net.chefbe.javautils.adapt.IAdapter;
import be.uclouvain.jail.algo.graph.copy.DirectedGraphCopier;
import be.uclouvain.jail.algo.graph.copy.match.GMatchPopulator;
import be.uclouvain.jail.dialect.IPrintable;
import be.uclouvain.jail.dialect.dot.DOTDirectedGraphPrintable;
import be.uclouvain.jail.dialect.dot.DOTGraphDialect;
import be.uclouvain.jail.dialect.dot.JDotty;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.adjacency.AdjacencyDirectedGraph;
import be.uclouvain.jail.graph.utils.DirectedGraphWriter;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.vm.JailReflectionToolkit;
import be.uclouvain.jail.vm.JailVM;
import be.uclouvain.jail.vm.JailVMException;
import be.uclouvain.jail.vm.JailVMOptions;
import be.uclouvain.jail.vm.JailVMException.ERROR_TYPE;

/** Provides a graph tolkit. */
public class GraphToolkit extends JailReflectionToolkit implements IAdapter {

	/** JDotty instance for visualization. */
	private JDotty jdotty;
	
	/** Installs the toolkit on the virtual machine. */
	public void install(JailVM vm) {
		vm.registerAdaptation(IDirectedGraph.class, IPrintable.class, this);
		vm.registerAdaptation(DOTDirectedGraphPrintable.class, IDirectedGraph.class, this);
		
		// register dot dialect
		vm.registerDialectLoader("dot", new DOTGraphDialect());
		
		// register IUserInfo adaptability
		vm.registerAdaptation(IDirectedGraph.class, IUserInfo.class, this);
	}

	/** Visualizes graphs using jdotty. */
	public IDirectedGraph jdotty(IDirectedGraph[] graphs) throws JailVMException {
		if (graphs == null || graphs.length==0) {
			throw new IllegalArgumentException("At least one graph must be provided.");
		}
		if (jdotty == null) {
			jdotty = new JDotty();
			jdotty.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent arg0) {
					jdotty = null;
				}
			});
		}
		try {
			for (IDirectedGraph graph: graphs) { 
				jdotty.present(graph,"JailVM.VarName");
			}
		} catch (IOException e) {
			throw new JailVMException(ERROR_TYPE.INTERNAL_ERROR,null,"Unable to execute jdotty.",e);
		}
		return graphs[0];
	}
	
	/** Copies a graph. */
	public IDirectedGraph copy(IDirectedGraph[] graphs, JailVMOptions options) throws JailVMException {
		AdjacencyDirectedGraph copy = new AdjacencyDirectedGraph();
		DirectedGraphWriter writer = new DirectedGraphWriter(copy);
		writer.getVertexCopier().keepAll();
		writer.getEdgeCopier().keepAll();
		
		// add state populator
		if (options.hasOption("vertex")) {
			GMatchPopulator populator = options.getOptionValue("vertex",GMatchPopulator.class,null);
			writer.getVertexCopier().addPopulator(populator);
		}
		
		// add state populator
		if (options.hasOption("state")) {
			System.err.println("Warning, using depreacated :state on graph copy.");
			GMatchPopulator populator = options.getOptionValue("state",GMatchPopulator.class,null);
			writer.getVertexCopier().addPopulator(populator);
		}
		
		// add edge populator
		if (options.hasOption("edge")) {
			GMatchPopulator populator = options.getOptionValue("edge",GMatchPopulator.class,null);
			writer.getEdgeCopier().addPopulator(populator);
		}
		
		for (IDirectedGraph graph: graphs) {
			DirectedGraphCopier.copy(graph,writer);
		}
		return copy;
	}
	
	/** Adapts who to the requested type. */
	public Object adapt(Object who, Class type) {
		if (IPrintable.class.equals(type)) {
			return adaptToPrintable(who);
		} else if (IDirectedGraph.class.equals(type)) {
			return adaptToDirectedGraph(who);
		} else if (IUserInfo.class.equals(type)) {
			return adaptToUserInfo(who);
		}
		return super.adapt(who, type);
	}
	
	/** Adapts an object to a directed graph. */
	private IDirectedGraph adaptToDirectedGraph(Object who) {
		if (who instanceof DOTDirectedGraphPrintable) {
			return (IDirectedGraph) ((IAdaptable)who).adapt(IDirectedGraph.class);
		} else {
			Class c = who == null ? null : who.getClass();
			String msg = "Unable to convert "  + who;
			if (c != null) {
				msg += " [" + c.getSimpleName() + "]";
			}
			msg += " to a IDirectedGraph.";
			throw new IllegalStateException(msg);
		}
	}
	
	/** Adapts an object to a IUserInfo. */
	private IUserInfo adaptToUserInfo(Object who) {
		if (who instanceof IDirectedGraph) {
			return ((IDirectedGraph)who).getUserInfo();
		} else {
			throw new IllegalStateException("Unable to convert " + who + " to a IUserInfo.");
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
