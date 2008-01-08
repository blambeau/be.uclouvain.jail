/**
 * 
 */
package be.uclouvain.jail.dialect.seqp;

import java.io.IOException;
import java.io.PrintWriter;

import net.chefbe.autogram.ast2.parsing.ParseException;
import be.uclouvain.jail.dialect.commons.AbstractGraphDialect;

/** Installs the DOT graph dialect. */
public class SEQPGraphDialect extends AbstractGraphDialect {

	/** Loads source in dot format. */
	public Object load(Object source, String format) throws IOException,
			ParseException {
		if ("seqp".equals(format)) {
			return SEQPGraphLoader.load(source, uInfoHelper);
		} else {
			throw new IllegalStateException("Unknown format (not jail .seqp): "
					+ format);
		}
	}

	/** Prints source in dot format. */
	public void print(Object source, String format, PrintWriter stream)
			throws IOException {
		throw new UnsupportedOperationException(
				"SEQP dialect does not implement print.");
	}

}