package be.uclouvain.jail.graph.constraints;

import be.uclouvain.jail.algo.graph.connex.ConnexGraphConstraint;
import be.uclouvain.jail.fa.constraints.DFAGraphConstraint;
import be.uclouvain.jail.fa.constraints.FAGraphConstraint;
import be.uclouvain.jail.fa.constraints.PTAGraphConstraint;
import be.uclouvain.jail.fa.impl.IGraphFAInformer;
import be.uclouvain.jail.graph.IDirectedGraph;

/**
 * Provides a facade for checking graph predicates in a simple way. 
 * 
 * @author blambeau
 */
public final class GraphCheckUtils {

	/** Not intended to be instanciated. */
	private GraphCheckUtils() {}
	
	/** Checks presence of a vertex info attribute. */
	public static boolean isVertexInfoPresent(IDirectedGraph graph, String attr) {
		return new GraphInfoPresenceConstraint(AbstractGraphConstraint.VERTEX,attr).isRespectedBy(graph);
	}
	
	/** Checks presence of an edge info attribute. */
	public static boolean isEdgeInfoPresent(IDirectedGraph graph, String attr) {
		return new GraphInfoPresenceConstraint(AbstractGraphConstraint.EDGE,attr).isRespectedBy(graph);
	}
	
	/** Checks uniqueness and presence of a vertex info attribute. */
	public static boolean isVertexInfoUnique(IDirectedGraph graph, String attr, boolean mandatory) {
		return new GraphUniqueIndex(AbstractGraphConstraint.VERTEX,attr,mandatory).isRespectedBy(graph);
	}
	
	/** Checks uniqueness and presence of an edge info attribute. */
	public static boolean isEdgeInfoUnique(IDirectedGraph graph, String attr, boolean mandatory) {
		return new GraphUniqueIndex(AbstractGraphConstraint.EDGE,attr,mandatory).isRespectedBy(graph);
	}
	
	/** Checks if a graph is connex. */
	public static boolean isConnex(IDirectedGraph graph) {
		return new ConnexGraphConstraint().isRespectedBy(graph);
	}
	
	/** Checks that a graph respects the FA semantics. */
	public static boolean isFA(IDirectedGraph graph, IGraphFAInformer informer) {
		return new FAGraphConstraint(informer).isRespectedBy(graph);
	}
	
	/** Checks that a graph respects the FA semantics. */
	public static boolean isFA(IDirectedGraph graph) {
		return new FAGraphConstraint().isRespectedBy(graph);
	}
	
	/** Checks that a graph respects the DFA semantics. */
	public static boolean isDFA(IDirectedGraph graph, IGraphFAInformer informer) {
		return new DFAGraphConstraint(informer).isRespectedBy(graph);
	}
	
	/** Checks that a graph respects the DFA semantics. */
	public static boolean isDFA(IDirectedGraph graph) {
		return new DFAGraphConstraint().isRespectedBy(graph);
	}
	
	/** Checks that a graph respects the PTA semantics. */
	public static boolean isPTA(IDirectedGraph graph, IGraphFAInformer informer) {
		return new PTAGraphConstraint(informer).isRespectedBy(graph);
	}
	
	/** Checks that a graph respects the PTA semantics. */
	public static boolean isPTA(IDirectedGraph graph) {
		return new PTAGraphConstraint().isRespectedBy(graph);
	}
	
}
