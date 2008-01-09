package be.uclouvain.jail.dialect.upv2;

import java.io.IOException;
import java.io.PrintWriter;

import net.chefbe.autogram.ast2.parsing.ParseException;
import be.uclouvain.jail.dialect.utils.AbstractGraphDialect;
import be.uclouvain.jail.vm.JailVMOptions;

/**
 * Provides upv2 dialect.
 * 
 * @author blambeau
 */
public class UPV2GraphDialect extends AbstractGraphDialect {

	/** Loads a graph in upv2 dialect. */
	public Object load(Object source, JailVMOptions options)
			throws IOException, ParseException {
		return null;
	}

	/** Prints a graph in upv2 dialect. */
	public void print(Object source, PrintWriter stream, JailVMOptions options)
			throws IOException {
	}

}
