package be.uclouvain.jail.graph.utils;

/**
 * Provides some java utils.
 * 
 * @author blambeau
 */
public class JavaUtils {

	/** Makes the sum of some numbers. */
	public static Number sum(Number i, Number j) {
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
	
}
