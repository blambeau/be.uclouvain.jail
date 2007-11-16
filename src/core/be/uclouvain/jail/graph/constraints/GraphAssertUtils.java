package be.uclouvain.jail.graph.constraints;

import be.uclouvain.jail.fa.impl.IGraphFAInformer;
import be.uclouvain.jail.graph.IDirectedGraph;

/**
 * Assertion utilities on graphs. 
 * 
 * @author blambeau
 */
public final class GraphAssertUtils {

	/** Force flag. */
	public static boolean FORCE = false;
	
	/** Not intended to be instanciated. */
	private GraphAssertUtils() {}
	
	/** Makes the assert. */
	private static void doAssert(String msg, boolean check) {
		if (!check) {
			throw new AssertionError(msg);
		}
	}
	
	/** Checks presence of a vertex info attribute. */
	public static void assertVertexInfoPresent(IDirectedGraph graph, String attr) {
		if (FORCE) {
			doAssert("Vertex info " + attr + " is present on graph",
					 GraphCheckUtils.isVertexInfoPresent(graph, attr));
		} else {
			assert GraphCheckUtils.isVertexInfoPresent(graph, attr) : 
				   "Vertex info " + attr + " is present on graph";
		}
	}
	
	/** Checks presence of an edge info attribute. */
	public static void assertEdgeInfoPresent(IDirectedGraph graph, String attr) {
		if (FORCE) {
			doAssert("Edge info " + attr + " is present on graph",
					 GraphCheckUtils.isEdgeInfoPresent(graph, attr));
		} else {
			assert GraphCheckUtils.isEdgeInfoPresent(graph, attr) : 
				   "Edge info " + attr + " is present on graph";
		}
	}
	
	/** Checks uniqueness and presence of a vertex info attribute. */
	public static void assertVertexInfoUnique(IDirectedGraph graph, String attr, boolean mandatory) {
		if (FORCE) {
			doAssert("Vertex info " + attr + " is unique on graph",
					 GraphCheckUtils.isVertexInfoUnique(graph, attr, mandatory));
		} else {
			assert GraphCheckUtils.isVertexInfoUnique(graph, attr, mandatory) : 
				   "Vertex info " + attr + " is unique on graph";
		}
	}
	
	/** Checks uniqueness and presence of an edge info attribute. */
	public static void assertEdgeInfoUnique(IDirectedGraph graph, String attr, boolean mandatory) {
		if (FORCE) {
			doAssert("Edge info " + attr + " is unique on graph",
					 GraphCheckUtils.isEdgeInfoUnique(graph, attr, mandatory));
		} else {
			assert GraphCheckUtils.isEdgeInfoUnique(graph, attr, mandatory) : 
				   "Edge info " + attr + " is unique on graph";
		}
	}
	
	/** Checks if a graph is connex. */
	public static void assertIsConnex(IDirectedGraph graph) {
		if (FORCE) {
			doAssert("Graph is connex", GraphCheckUtils.isConnex(graph));
		} else {
			assert GraphCheckUtils.isConnex(graph) : "Graph is connex.";
		}
	}
	
	/** Checks if a graph respectes FA semantics. */
	public static void assertIsFA(IDirectedGraph graph, IGraphFAInformer informer) {
		if (FORCE) {
			doAssert("Graph is FA compatible", GraphCheckUtils.isDFA(graph,informer));
		} else {
			assert GraphCheckUtils.isFA(graph,informer) : "Graph is FA compatible";
		}
	}
	
	/** Checks if a graph respectes FA semantics. */
	public static void assertIsFA(IDirectedGraph graph) {
		if (FORCE) {
			doAssert("Graph is FA compatible", GraphCheckUtils.isFA(graph));
		} else {
			assert GraphCheckUtils.isFA(graph) : "Graph is FA compatible";
		}
	}
	
	/** Checks if a graph respectes DFA semantics. */
	public static void assertIsDFA(IDirectedGraph graph, IGraphFAInformer informer) {
		if (FORCE) {
			doAssert("Graph is DFA compatible", GraphCheckUtils.isDFA(graph,informer));
		} else {
			assert GraphCheckUtils.isDFA(graph,informer) : "Graph is DFA compatible";
		}
	}
	
	/** Checks if a graph respectes DFA semantics. */
	public static void assertIsDFA(IDirectedGraph graph) {
		if (FORCE) {
			doAssert("Graph is DFA compatible", GraphCheckUtils.isDFA(graph));
		} else {
			assert GraphCheckUtils.isDFA(graph) : "Graph is DFA compatible";
		}
	}
	
	/** Checks if a graph respectes PTA semantics. */
	public static void assertIsPTA(IDirectedGraph graph, IGraphFAInformer informer) {
		if (FORCE) {
			doAssert("Graph is PTA compatible", GraphCheckUtils.isPTA(graph,informer));
		} else {
			assert GraphCheckUtils.isPTA(graph,informer) : "Graph is PTA compatible";
		}
	}
	
	/** Checks if a graph respectes DFA semantics. */
	public static void assertIsPTA(IDirectedGraph graph) {
		if (FORCE) {
			doAssert("Graph is PTA compatible", GraphCheckUtils.isPTA(graph));
		} else {
			assert GraphCheckUtils.isPTA(graph) : "Graph is PTA compatible";
		}
	}
	
	
}
