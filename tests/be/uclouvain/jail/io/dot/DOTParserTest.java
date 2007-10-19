package be.uclouvain.jail.io.dot;

import java.io.IOException;
import java.net.URL;

import junit.framework.TestCase;
import net.chefbe.autogram.ast2.parsing.ParseException;
import be.uclouvain.jail.Jail;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.io.IPrintable;

/** Tests the DOT parser. */
public class DOTParserTest extends TestCase {

	/** Returns a graph URL. */
	public URL getGraphURL() {
		return DOTParserTest.class.getResource("graph.dot");
	}
	
	/** Tests the dot parser. */
	public void testDOTParser() throws IOException, ParseException {
		Jail.install();
		URL url = getGraphURL();
		IDirectedGraph graph = DOTDirectedGraphLoader.loadGraph(url);
		IPrintable printable = (IPrintable) graph.adapt(IPrintable.class);
		assertNotNull("Adaptation worked.", printable);
		printable.print(System.out);
	}
	
}
