/**
 * 
 */
package be.uclouvain.jail.dialect.seqp;

import java.io.IOException;
import java.io.PrintWriter;

import net.chefbe.autogram.ast2.parsing.ParseException;
import be.uclouvain.jail.dialect.utils.AbstractGraphDialect;
import be.uclouvain.jail.vm.JailVMOptions;

/** Installs the SEQP graph dialect. */
public class SEQPGraphDialect extends AbstractGraphDialect {

	/** Loads source in dot format. */
	public Object load(Object source, JailVMOptions options) throws IOException, ParseException {
		return SEQPGraphLoader.load(source, this, options);
	}

	/** Prints source in dot format. */
	public void print(Object source, PrintWriter stream, JailVMOptions options) throws IOException {
		throw new UnsupportedOperationException("SEQP dialect does not implement print.");
	}

}