package be.uclouvain.jail.uinfo.functions;

/**
 * Concats function.
 * 
 * @author blambeau
 */
public class ConcatFunction extends AbstractAggregateFunction<String> {

	/** Delimiter to use. */
	private String delimiter = ", ";
	
	/** Creates a concat function. */
	public ConcatFunction(String delimiter) {
		this.delimiter = delimiter;
	}
	
	/** Creates a concat function. */
	public ConcatFunction() {
		this(", ");
	}
	
	/** Computes the concatenation. */
	@Override
	public String compute(String op1, String op2) {
		String s1 = op1 == null ? "" : op1.toString();
		String s2 = op2 == null ? "" : op2.toString();
		String res =  "".equals(s1) || "".equals(s2) ? s1.concat(s2) : s1.concat(delimiter).concat(s2);
		return res;
	}

	/** Returns an empty string. */
	@Override
	protected String onEmpty() {
		return "";
	}

}
