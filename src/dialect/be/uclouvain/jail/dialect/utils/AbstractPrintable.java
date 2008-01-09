package be.uclouvain.jail.dialect.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import be.uclouvain.jail.dialect.IPrintable;

/** 
 * Utility to create printers.
 * 
 * @author blambeau
 */
public abstract class AbstractPrintable implements IPrintable {

	/** Prints this resource to a stream. */
	public void print(OutputStream stream) throws IOException {
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(stream));
		print(writer);
		writer.flush();
	}

	/** Prints on a writer. */
	protected abstract void print(PrintWriter writer) throws IOException;
	
}
