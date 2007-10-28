package be.uclouvain.jail.dialect.dot;

import net.chefbe.autogram.ast2.parsing.ParserFactory;
import net.chefbe.autogram.ast2.xpath.XPathNodes;
import net.chefbe.autogram.ast2.xpath.internal.XPathParser;

/** Manages toolkit initialization for DOT grammar support. */
public class DOTSupport {

	/** Private (static). */
	private DOTSupport() {
	}

	/** Installs DOT support on the toolkit. */
	public static void install() {
		// install XPath package

		// install parsers
		ParserFactory.instance().addRatsParser(XPathNodes.XPATH_UNIT,XPathParser.class);
			}
	
}
