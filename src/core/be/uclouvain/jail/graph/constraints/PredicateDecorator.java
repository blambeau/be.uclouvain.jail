package be.uclouvain.jail.graph.constraints;

import org.apache.commons.collections.Predicate;

import be.uclouvain.jail.common.IPredicate;

/**
 * Decorates à jakarta commons collection predicate to a {@link GraphPredicate}.
 * 
 * <p>This class is mainly introduces to convert AND, OR and NOT predicates,
 * as provided by common collections library to IGraphPredicateS in Jail.</p>
 * 
 * @author blambeau
 */
public class PredicateDecorator<T> implements IPredicate<T> {

	/** Decorated predicate. */
	private Predicate p;
	
	/** Creates a decorator instance. */
	public PredicateDecorator(Predicate p) {
		this.p = p;
	}

	/** Delegated to the decorated predicate. */
	public boolean evaluate(T value) {
		return p.evaluate(value);
	}

}
