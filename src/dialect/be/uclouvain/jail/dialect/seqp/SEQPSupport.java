package be.uclouvain.jail.dialect.seqp;

import net.chefbe.autogram.ast2.parsing.ParserFactory;
import net.chefbe.autogram.ast2.xpath.XPathNodes;
import net.chefbe.autogram.ast2.xpath.internal.XPathParser;

/** Manages toolkit initialization for SEQP grammar support. */
public class SEQPSupport {

	/** Private (static). */
	private SEQPSupport() {
	}

	/** Installs SEQP support on the toolkit. */
	public static void install() {
		// install XPath package

		// install parsers
		ParserFactory.instance().addRatsParser(XPathNodes.XPATH_UNIT,
				XPathParser.class);
	}

}
