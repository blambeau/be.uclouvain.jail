package be.uclouvain.jail.graph.deco;

/** 
 * Defines a visitor of graph delta. 
 * 
 * <p>This visitor interface provides a user friendly API to
 * listen to graph changes.</p>
 * 
 * <p>This interface may be implemented. Please extend VisitorAdapter when 
 * possible.</p>
 */
public interface IGraphDeltaVisitor {

	/** Visits an event. */
	public void visit(GraphChangeEvent event);
	
}