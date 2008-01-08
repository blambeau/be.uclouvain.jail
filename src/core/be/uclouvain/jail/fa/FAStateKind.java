package be.uclouvain.jail.fa;

/**
 * Defines states kind.
 * 
 * @author blambeau
 */
public enum FAStateKind {

	/** Accepting state (accepting, no error). */
	ACCEPTING(0),
	
	/** Passage state (non accepting, no error). */
	PASSAGE(1),
	
	/** Error state (non accepting, error). */
	ERROR(2),
	
	/** Avoid state (accepting, error). */
	AVOID(3);

	/** Index. */
	private int index;

	/** Creates a value instance. */
	FAStateKind(int index) {
		this.index = index;
	}
	
	/** Returns the index. */
	public int index() {
		return index;
	}
	
	/** Checks if the state is accepting. */
	public boolean isFlagAccepting() {
		return ACCEPTING.equals(this) || AVOID.equals(this);
	}
	
	/** Checks if the state is accepting. */
	public boolean isFlagError() {
		return ERROR.equals(this) || AVOID.equals(this);
	}
	
	/** Converts two booleans to a state kind. */
	public static FAStateKind fromBools(boolean isAccepting, boolean isError) {
		if (isAccepting) {
			return isError ? AVOID : ACCEPTING;
		} else {
			return isError ? ERROR : PASSAGE;
		}
	}
	
}
