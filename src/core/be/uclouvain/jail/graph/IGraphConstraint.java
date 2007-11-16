package be.uclouvain.jail.graph;

import be.uclouvain.jail.graph.deco.DirectedGraph;
import be.uclouvain.jail.graph.deco.IGraphListener;

/**
 * Defines a constraint to be respected by a graph.
 * 
 * <p>Contraints are the way to ensure/check some properties on graphs. A constraint
 * is said to be active when it ensure that the property will never be violated. These
 * constraints typically implement or use a {@link IGraphListener} to listen to graph 
 * changes and throw {@link GraphConstraintViolationException} when changes tend to 
 * violate the graph constraint. All constraints also provide a check to verify if the
 * property is currently respected by the graph ({@link #isRespectedBy(IDirectedGraph)}.
 * The {@link IGraphPredicate} implementation has exactly the same semantics.</p>
 * 
 * <p>The way to use active constraints is as follows (for some GraphUniqueIndex constraint, 
 * for example):</p>
 * <pre>
 *     // consider some graph instance
 *     DirectedGraph graph = [...]
 *     
 *     // create a unique constraint and install it on graph
 *     GraphUniqueIndex index = new GraphUniqueIndex(...).installOn(graph);
 *     
 *     // the index may also be used after installation
 *     index.get(...)
 * </pre>
 * <p>Warning that, besides the strange API of constraints, the same constraint 
 * instance may not be installed on two different graphs. The only reason why the
 * constraint returns itself at installOn invocation is to provide a simple way to
 * create and install the constraint at once (because, as for GraphUniqueIndex, some 
 * constraints provide useful effects to be used after installation.</p>
 * 
 * <p>The check/predicate semantics of the constraint is used simply as follows:</p>
 * <pre>
 *     // consider some graph instance
 *     DirectedGraph graph = [...]
 *     
 *     // just check some graph constraint from the outside
 *     // are vertex ids unique on my graph?
 *     boolean unique = new GraphUniqueIndex(GraphUniqueIndex.VERTEX,"id",true)
 *                         .isRespectedBy(graph);
 *                         
 *     // which is equivalent to the predicate evaluation method
 *     boolean uniq2 = new GraphUniqueIndex(GraphUniqueIndex.VERTEX,"id",true)
 *                         .evaluate(graph);
 *                         
 *     // ... ensures that ...
 *     assertTrue(unique,uniq2);
 * </pre> 
 * 
 * @author blambeau
 */
public interface IGraphConstraint extends IGraphPredicate {

	/** 
	 * Installs the constraint on the graph. 
	 * 
	 * <p>This method must ensure that the constraint is respected on the
	 * graph argument. It has no effect on the graph when the constraint is
	 * not respected but throws a violation exception.</p>
	 * 
	 * @param graph the graph on which the constraint must be installed.
	 * @throws GraphConstraintViolatioException if the constraint is not 
	 * already respected by the graph.
	 * @throws UnsupportedOperationException when the constraint is not (yet) 
	 * active.
	 */
	public <T extends IGraphConstraint> T installOn(DirectedGraph graph) 
		throws GraphConstraintViolationException;

	/** 
	 * Uninstalls constraint on graph. 
	 * 
	 * @throws IllegalStateException when the constraint has not been previously 
	 * installed on a graph.</p>
	 * @throws UnsupportedOperationException when the constraint is not (yet) 
	 * active.
	 */
	public void uninstall();
	
	/** Returns true if this constraint is respected by the graph, false
	 * otherwise. */
	public boolean isRespectedBy(IDirectedGraph graph);
	
}
