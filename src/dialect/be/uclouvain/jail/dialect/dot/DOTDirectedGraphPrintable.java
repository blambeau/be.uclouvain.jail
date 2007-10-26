package be.uclouvain.jail.dialect.dot;

import java.io.IOException;
import java.io.PrintWriter;

import be.uclouvain.jail.Jail;
import be.uclouvain.jail.adapt.AdaptUtils;
import be.uclouvain.jail.adapt.IAdaptable;
import be.uclouvain.jail.dialect.AbstractPrintable;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.utils.ITotalOrder;
import be.uclouvain.jail.uinfo.IUserInfo;

/**
 * Printable adapted on DirectedGraph
 */
public class DOTDirectedGraphPrintable extends AbstractPrintable implements IAdaptable {

	/** Installs default properties. */
	static {
		Jail.setProperty("DirectedGraphPrintable.dot.graph.attributes","rankdir=\"LR\"");
		Jail.setProperty("DirectedGraphPrintable.dot.graph.name.uinfo", "name");
		Jail.setProperty("DirectedGraphPrintable.dot.nodes.attributes","shape=\"circle\"");
		Jail.setProperty("DirectedGraphPrintable.dot.node.label.uinfo","label");
		Jail.setProperty("DirectedGraphPrintable.dot.edge.label.uinfo","label");
	}

	/** Graph to print. */
	private IDirectedGraph graph;

	/** Decorates a graph to a printable. */
	public DOTDirectedGraphPrintable(IDirectedGraph graph) {
		this.graph = graph;
	}

	/** Prints the graph on a writer. */
	@Override
	protected void print(PrintWriter bw) throws IOException {
		ITotalOrder<Object> vertices = graph.getVerticesTotalOrder();
		ITotalOrder<Object> edges = graph.getEdgesTotalOrder();

		// extract graph name
		Object name = graph.getUserInfo().getAttribute(
				Jail.getStringProperty(
						"DirectedGraphPrintable.dot.graph.name.uinfo", "name"));
		name = name == null ? "G" : normalize(name);

		bw.write("digraph " + name + " {\n");
		{
			/* header */
			bw.write("\tgraph [");
			bw.write(graphAttributes());
			bw.write("];\n");
			bw.write("\tnode [");
			bw.write(nodeAttributes());
			bw.write("];\n");

			/* states */
			for (Object state : vertices) {
				bw.write("\t");
				bw.write(Integer.toString(vertices.indexOf(state)));
				bw.write(" [");
				bw.write(stateAttributes(state));
				bw.write("];\n");
			}

			/* edges */
			for (Object edge : edges) {
				Object source = graph.getEdgeSource(edge);
				Object target = graph.getEdgeTarget(edge);
				bw.write("\t");
				bw.write(Integer.toString(vertices.indexOf(source)));
				bw.write(" -> ");
				bw.write(Integer.toString(vertices.indexOf(target)));
				bw.write(" [");
				bw.write(edgeAttributes(edge));
				bw.write("];\n");
			}

			/* footer */
			bw.write("}\n");
		}
	}

	/** Normalize a string (an object) for dot. */
	protected String normalize(Object s) {
		if (s == null) {
			return "";
		} else {
			return s.toString().replaceAll("\"", "").replaceAll("\n", " ");
		}
	}

	/** Returns a string containing the main dot graph attributes. */
	protected String graphAttributes() {
		return Jail
				.getStringProperty(
						"DirectedGraphPrintable.dot.graph.attributes",
						"rankdir=\"LR\"");
	}

	/** Returns a String containing the main dot node attributes. */
	protected String nodeAttributes() {
		return Jail.getStringProperty(
				"DirectedGraphPrintable.dot.nodes.attributes",
				"shape=\"circle\"");
	}

	/** Returns the label of a state. */
	protected Object stateLabel(Object vertex) {
		String key = Jail.getStringProperty(
				"DirectedGraphPrintable.dot.node.label.uinfo", null);
		Object label = (key != null) ? graph.getVertexInfo(vertex)
				.getAttribute(key) : graph.getVerticesTotalOrder()
				.indexOf(vertex);
		return label == null ? graph.getVerticesTotalOrder().indexOf(
				vertex) : label;
	}

	/** Returns a string containing the dot attributes to set to a vertex. */
	protected String stateAttributes(Object vertex) {
		Object label = stateLabel(vertex);
		String s = "label=\"" + normalize(label) + "\"";

		IUserInfo info = graph.getVertexInfo(vertex);
		for (String key: info.getKeys()) {
			Object value = info.getAttribute(key);
			s += " " + key + "=\"" + normalize(value) + "\"";
		}
		return s;
	}

	/** Returns label of an edge. */
	protected Object edgeLabel(Object edge) {
		String key = Jail.getStringProperty(
				"DirectedGraphPrintable.dot.edge.label.uinfo", null);
		Object label = (key != null) ? graph.getEdgeInfo(edge)
				.getAttribute(key) : graph.getEdgesTotalOrder()
				.indexOf(edge);
		return label == null ? graph.getEdgesTotalOrder().indexOf(edge)
				: label;
	}

	/** Returns a String containing the dot attributes to set to an edge. */
	protected String edgeAttributes(Object edge) {
		Object label = edgeLabel(edge);
		String s = "label=\"" + normalize(label) + "\"";

		IUserInfo info = graph.getEdgeInfo(edge);
		for (String key: info.getKeys()) {
			Object value = info.getAttribute(key);
			s += " " + key + "=\"" + normalize(value) + "\"";
		}
		return s;
	}

	/** Adapts this object to type c. */
	public <T> Object adapt(Class<T> c) {
		if (c.isAssignableFrom(getClass())) {
			return this;
		}
		
		if (IDirectedGraph.class.equals(c)) {
			return graph;
		}
		
		return AdaptUtils.externalAdapt(this, c);
	}

}
