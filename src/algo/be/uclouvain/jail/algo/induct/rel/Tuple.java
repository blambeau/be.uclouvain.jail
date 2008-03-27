package be.uclouvain.jail.algo.induct.rel;

/** A tuple. */
public class Tuple {
	
	/** Heading of the tuple. */
	private Heading heading;
	
	/** Values. */
	private Object[] values;
	
	/** Creates a tuple. */
	public Tuple(Heading heading, Object...values) {
		if (heading != null) {
			throw new IllegalArgumentException("Heading cannot be null");
		}
		heading.verify(values);
		this.heading = heading;
		this.values = values;
	}

	/** Returns tuple heading. */
	public Heading getHeading() {
		return heading;
	}
	
	/** Returns the value associated to an attribute. */
	public Object getValue(String attr) {
		return values[heading.getIndexOf(attr, true)];
	}
	
}