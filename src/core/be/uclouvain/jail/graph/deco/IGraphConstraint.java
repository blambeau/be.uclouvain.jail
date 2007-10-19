package be.uclouvain.jail.graph.deco;

/**
 * Defines a constraint to be respected by a graph.
 * 
 * <p>Contraints are the way to ensure some properties on graphs. They typically
 * implement {@link IGraphListener} to listen to graph changes and throw 
 * @{link GraphConstraintViolationException} when changes tend to violate the 
 * graph constraint.</p>
 * 
 * <p>Constraints are also provided to be simply checked on graphs without being
 * actually installed.</p>
 * 
 * @author blambeau
 */
public interface IGraphConstraint {

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
	 */
	public void installOn(DirectedGraph graph) throws GraphConstraintViolationException;

	/** Uninstalls constraint on graph. */
	public void uninstallOn(DirectedGraph graph);
	
	/** Returns true if this constraint is respected by the graph, false
	 * otherwise. */
	public boolean isRespectedBy(DirectedGraph graph);
	
}
