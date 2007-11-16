package be.uclouvain.jail.graph.constraints;

import org.apache.commons.collections.Predicate;

import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.IGraphPredicate;

/**
 * Decorates à jakarta commons collection predicate to a {@link GraphPredicate}.
 * 
 * <p>This class is mainly introduces to convert AND, OR and NOT predicates,
 * as provided by common collections library to IGraphPredicateS in Jail.</p>
 * 
 * @author blambeau
 */
public class PredicateDecorator implements IGraphPredicate {

	/** Decorated predicate. */
	private Predicate p;
	
	/** Creates a decorator instance. */
	public PredicateDecorator(Predicate p) {
		this.p = p;
	}

	/** Checks that g is a graph and delegates it to the decorated
	 * predicate. */
	public boolean evaluate(Object g) {
		if (g instanceof IDirectedGraph) {
			return p.evaluate(g);
		} else {
			return false;
		}
	}

	/** Delegated to the decorated predicate. */
	public boolean evaluate(IDirectedGraph graph) {
		return p.evaluate(graph);
	}

}
