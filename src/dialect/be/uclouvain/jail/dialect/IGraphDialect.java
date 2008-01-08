package be.uclouvain.jail.dialect;

import java.io.IOException;
import java.io.PrintWriter;

import net.chefbe.autogram.ast2.parsing.ParseException;
import be.uclouvain.jail.uinfo.IUserInfoHelper;

/**
 * JailCoreToolkit extension interface to load/save/print files in diferent 
 * dialects.
 * 
 * @author blambeau
 */
public interface IGraphDialect {

	/** Sets helper to use. */
	public void setUserInfoHelper(IUserInfoHelper helper);
	
	/** Loads from a source. */
	public Object load(Object source, String format) throws IOException, ParseException;
	
	/** Prints a resource to a stream. */
	public void print(Object source, String format, PrintWriter stream) throws IOException;
	
}
