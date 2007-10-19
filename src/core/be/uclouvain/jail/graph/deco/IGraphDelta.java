package be.uclouvain.jail.graph.deco;

/**
 * Encapsulates informations about graph changes.
 * 
 * <p>This interface is not intended to be implemented.</p>
 * 
 * @author blambeau
 */
public interface IGraphDelta {

	/** Returns the graph which changed. */
	public DirectedGraph getDirectedGraph();
	
	/**
	 * Accepts a visitor.
	 * 
	 * @param visitor visitor to accept.
	 * @param mask OR-mask of interesting events.
	 */
	public void accept(IGraphDeltaVisitor visitor, int mask);
	
}
