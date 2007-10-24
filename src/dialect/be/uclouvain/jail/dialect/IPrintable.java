package be.uclouvain.jail.dialect;

import java.io.IOException;
import java.io.OutputStream;

/** Marker for printable components. */
public interface IPrintable {

	/** Prints the component on a stream. */
	public void print(OutputStream stream) throws IOException;
	
}
