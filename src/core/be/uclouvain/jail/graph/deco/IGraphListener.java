package be.uclouvain.jail.graph.deco;

/**
 * Provides listening facilities on @{link DirectedGraph}.
 * 
 * <p>This interface installs a generic listener on directed graphs, in order 
 * to allow JAIL to evolve about graph events. If this interface, as well as 
 * {@link IGraphDeltaVisitor}, is too generic for your needs please consider 
 * using DirectedGraphListenerAdapter directly.</p>
 * 
 * @author blambeau
 */
public interface IGraphListener {

	/** 
	 * Fired when a graph has changed. 
	 * 
	 * @param graph graph firing this event.
	 * @param delta informations about graph changes.
	 */
	public void graphChanged(DirectedGraph graph, IGraphDelta delta);
	
}
