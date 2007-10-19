package be.uclouvain.jail.fa.functions;

/**
 * Defines states kind.
 * 
 * @author blambeau
 */
public enum FAStateKind {

	/** Accepting state. */
	ACCEPTING(0),
	
	/** Passage state. */
	PASSAGE(1),
	
	/** Error state. */
	ERROR(3),
	
	/** Avoid state. */
	AVOID(4);

	/** Suppremum array. */
	public static FAStateKind[][] suppremum = new FAStateKind[][]{
		/* A A - -
		   A P E -
		   - E E -
		   - - - - */
		{ ACCEPTING, ACCEPTING, AVOID, AVOID },
		{ ACCEPTING, PASSAGE, ERROR, AVOID},
		{ AVOID, ERROR, ERROR, AVOID },
		{ AVOID, AVOID, AVOID, AVOID }
	};
	
	/** Computes the suppremum between two values. */
	public static FAStateKind supremum(FAStateKind k1, FAStateKind k2) {
		return suppremum[k1.index][k2.index];
	}

	/** Index. */
	private Integer index;

	/** Creates a value instance. */
	FAStateKind(Integer index) {
		this.index = index;
	}
	
}
