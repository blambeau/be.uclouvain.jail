package be.uclouvain.jail.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/** 
 * Utility to create printers.
 * 
 * @author blambeau
 */
public abstract class AbstractPrintable implements IPrintable {

	/* (non-Javadoc)
	 * @see be.uclouvain.jail.io.IPrintable#print(java.io.OutputStream)
	 */
	public void print(OutputStream stream) throws IOException {
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(stream));
		print(writer);
		writer.flush();
	}

	/** Prints on a writer. */
	protected abstract void print(PrintWriter writer) throws IOException;
	
}
