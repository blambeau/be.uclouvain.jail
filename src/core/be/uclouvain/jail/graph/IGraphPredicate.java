package be.uclouvain.jail.graph;

import org.apache.commons.collections.Predicate;

/** 
 * Acts as a predicate for graph.
 * 
 * <p>A graph predicate checks that some property is respected by the 
 * graph which is evaluated and returns true if respected, false 
 * otherwise.</p> 
 * 
 * <p>This interface is not intended to be implemented directly because
 * of the not so friendly bridge with jakarta commons collections. Please
 * extends {@link AbstractGraphPredicate} instead.</p> 
 * 
 * <p>Please note that many interesting predicates are in fact implemented 
 * as {@link IGraphConstraint} in Jail.</p>
 * 
 * @author blambeau
 */
public interface IGraphPredicate extends Predicate {

	/** Returns true if the property is respected, false otherwise. */
	public boolean evaluate(IDirectedGraph graph);
	
}
