package be.uclouvain.jail.graph.deco;

/**
 * Exception thrown by graph constraints when a violation occurs.
 * 
 * <p>This exception extends RuntimeException to avoid hurting JAIL APIs.
 * As constraints should always be respected by algorithms this fact should
 * not really matter in practice. Please always ensure that you catch this
 * exception when constraints-faulty write accesses are made on graphs.</p>
 * 
 * @author blambeau
 */
public class GraphConstraintViolationException extends RuntimeException {

	/** Serial version UID. */
	private static final long serialVersionUID = 6280241958711470908L;

	/** Violated constraint. */
	private IGraphConstraint constraint;

	/** Creates a constraint exception instance. */
	public GraphConstraintViolationException(IGraphConstraint constraint, String msg) {
		super(msg);
		if (constraint == null) {
			throw new IllegalArgumentException("Constraint argument required.");
		}
		this.constraint = constraint;
	}
	
	/** Returns the violated constraint. */
	public IGraphConstraint getViolatedConstraint() {
		return constraint;
	}
	
}
