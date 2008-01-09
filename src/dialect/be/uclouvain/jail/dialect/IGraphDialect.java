package be.uclouvain.jail.dialect;

import java.io.IOException;
import java.io.PrintWriter;

import net.chefbe.autogram.ast2.parsing.ParseException;
import be.uclouvain.jail.dialect.utils.AbstractGraphDialect;
import be.uclouvain.jail.uinfo.IUserInfoRewriters;
import be.uclouvain.jail.vm.JailVMOptions;

/**
 * JailCoreToolkit extension interface to load/save/print files in 
 * different dialects.
 * 
 * <p>This interface is NOT intended to be implemented directly. Please
 * always extend {@link AbstractGraphDialect} which provides the necessary
 * tools to implement it correctly, and will allow future extensions of 
 * this interface.</p>
 * 
 * @author blambeau
 */
public interface IGraphDialect {

	/**
	 * Sets the rewriters to use to create user infos at graph load.
	 * 
	 * @param rewriters rewriters to use to create user infos. 
	 */
	public void setLoadRewriters(IUserInfoRewriters rewriters);
	
	/**
	 * Sets the rewriters to apply to user infos before printing
	 * 
	 * @param rewriters rewriters to use to create user infos. 
	 */
	public void setPrintRewriters(IUserInfoRewriters rewriters);
	
	/**
	 * Loads a graph from a source, using a given dialect.
	 * 
	 * @param source source of the load.
	 * @param options (optional, may be null) loading options. Exact
	 *        semantics is left to dialect implementation.  
	 * @return loaded graph.
	 * @throws IOException if an error occurs when reading source.
	 * @throws ParseException if the source is not syntaxically/semantically
	 *         correct.
	 */
	public Object load(Object source, JailVMOptions options) throws IOException, ParseException;
	
	/**
	 * Prints a graph to a stream.
	 * 
	 * <p>Please note that the stream should me flushed but MAY NOT be
	 * closed by the dialect after writing.</p>
	 * 
	 * @param source source graph to encode in a specific dialect.
	 * @param stream stream where the graph mut be printed.
	 * @param options (optional, may be null) printing options. Exact 
	 *        semantics is left to dialect implementation.  
	 * @throws IOException if an error occurs when writing on the stream.
	 */
	public void print(Object source, PrintWriter stream, JailVMOptions options) throws IOException;
	
}
