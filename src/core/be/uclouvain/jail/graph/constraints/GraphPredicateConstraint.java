package be.uclouvain.jail.graph.constraints;

import org.apache.commons.collections.Predicate;

import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.IGraphPredicate;

/**
 * Decorates a graph predicate to a (non active) constraint.
 * 
 * @author blambeau
 */
public class GraphPredicateConstraint extends AbstractGraphConstraint {

	/** Predicate to use. */
	private IGraphPredicate predicate;
	
	/** Creates a predicate constraint instance. */
	public GraphPredicateConstraint(IGraphPredicate predicate) {
		this.predicate = predicate;
	}

	/** Shortcut to <code>new GraphPredicateConstraint(new PredicateDecorator(p))</code>. */ 
	public GraphPredicateConstraint(Predicate p) {
		this(new PredicateDecorator(p));
	}
	
	/** Delegated to the decorated predicate. */
	@Override
	public boolean isRespectedBy(IDirectedGraph graph) {
		return predicate.evaluate(graph);
	}

}
