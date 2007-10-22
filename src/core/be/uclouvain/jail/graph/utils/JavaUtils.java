package be.uclouvain.jail.graph.utils;

/**
 * Provides some java utils.
 * 
 * @author blambeau
 */
public class JavaUtils {

	/** Makes the sum of some numbers. */
	public static Number sum(Number i, Number j) {
		if (i == null || j == null) {
			throw new IllegalArgumentException("i and j cannot be null.");
		}
		if (i instanceof Double || j instanceof Double) {
			return new Double(i.doubleValue()+j.doubleValue());
		} else if (i instanceof Float || j instanceof Float) {
			return new Float(i.floatValue()+j.floatValue());
		} else if (i instanceof Long || j instanceof Long) {
			return new Long(i.longValue()+j.longValue());
		} else {
			return new Integer(i.intValue()+j.intValue());
		}
	}

	/** Compares two numbers. */
	public static int compare(Number d, Number e) {
		if (d == null || e == null) {
			throw new IllegalArgumentException("i and j cannot be null.");
		}
		Double dd = d.doubleValue();
		Double ed = e.doubleValue();
		return dd.compareTo(ed);
	}
	
}
