package be.uclouvain.jail.algo.graph.copy.match;

import net.chefbe.autogram.ast2.parsing.ParserFactory;
import net.chefbe.autogram.ast2.xpath.XPathNodes;
import net.chefbe.autogram.ast2.xpath.internal.XPathParser;

/** Manages toolkit initialization for GMatch grammar support. */
public class GMatchSupport {

	/** Private (static). */
	private GMatchSupport() {
	}

	/** Installs GMatch support on the toolkit. */
	public static void install() {
		// install XPath package

		// install parsers
		ParserFactory.instance().addRatsParser(XPathNodes.XPATH_UNIT,XPathParser.class);
			}
	
}
