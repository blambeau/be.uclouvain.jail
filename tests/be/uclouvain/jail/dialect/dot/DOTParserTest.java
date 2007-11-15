package be.uclouvain.jail.dialect.dot;

import java.io.IOException;
import java.net.URL;

import junit.framework.TestCase;
import net.chefbe.autogram.ast2.parsing.ParseException;
import be.uclouvain.jail.adapt.AdaptUtils;
import be.uclouvain.jail.adapt.CreateClassAdapter;
import be.uclouvain.jail.dialect.IPrintable;
import be.uclouvain.jail.graph.IDirectedGraph;

/** Tests the DOT parser. */
public class DOTParserTest extends TestCase {

	/** Returns a graph URL. */
	public URL getGraphURL() {
		return DOTParserTest.class.getResource("graph.dot");
	}
	
	/** Tests the dot parser. */
	public void testDOTParser() throws IOException, ParseException {
		AdaptUtils.register(IDirectedGraph.class,IPrintable.class,new CreateClassAdapter(DOTDirectedGraphPrintable.class));
		URL url = getGraphURL();
		IDirectedGraph graph = DOTDirectedGraphLoader.loadGraph(url);
		IPrintable printable = (IPrintable) graph.adapt(IPrintable.class);
		assertNotNull("Adaptation worked.", printable);
		//printable.print(System.out);
	}
	
}
