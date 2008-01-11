package be.uclouvain.jail.graph.constraints;

import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.IGraphPredicate;

/**
 * Makes the bridge between Jail and jakarta commons collections about
 * predicates.
 * 
 * <p>This class is primary introduced to provide a compatibility between
 * jakarta predicates and {@link IGraphPredicate} while keeping a user-
 * friendly evaluation method (which takes an IDirectedGraph, not an Object).</p>
 * 
 * <p>Its {@link #evaluate(Object)} method simply checks that argument is
 * indeed a graph and delegates the real predicate job to 
 * {@link #evaluate(IDirectedGraph)}. When used on objects that are not graphs,
 * it always returns false.</p> 
 * 
 * @author blambeau
 */
public abstract class AbstractGraphPredicate implements IGraphPredicate {

	/** To be implemented by subclasses. */
	public abstract boolean evaluate(IDirectedGraph graph);

}
