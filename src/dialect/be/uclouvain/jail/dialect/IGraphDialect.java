package be.uclouvain.jail.dialect;

import java.io.IOException;
import java.io.OutputStream;

import net.chefbe.autogram.ast2.parsing.ParseException;


/**
 * JailCoreToolkit extension interface to load/save/print files in diferent 
 * dialects.
 * 
 * @author blambeau
 */
public interface IGraphDialect {

	/** Loads from a source. */
	public Object load(Object source, String format) throws IOException, ParseException;
	
	/** Prints a resource to a stream. */
	public void print(Object source, String format, OutputStream stream) throws IOException;
	
}
