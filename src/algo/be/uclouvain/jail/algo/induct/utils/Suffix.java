package be.uclouvain.jail.algo.induct.utils;

/** Suffix of a state inside a PTA. */
public class Suffix {

	/** Suffix letters. */
	public Object suffix[];

	/** Negative suffix? */
	public boolean negative;

	/** Creates a suffix instance. */
	public Suffix(Object suffix[], boolean negative) {
		this.suffix = suffix;
		this.negative = negative;
	}
	
}
