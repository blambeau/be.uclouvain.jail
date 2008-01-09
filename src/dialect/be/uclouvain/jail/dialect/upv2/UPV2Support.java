package be.uclouvain.jail.dialect.upv2;

import net.chefbe.autogram.ast2.parsing.ParserFactory;
import net.chefbe.autogram.ast2.xpath.XPathNodes;
import net.chefbe.autogram.ast2.xpath.internal.XPathParser;

/** Manages toolkit initialization for UPV2 grammar support. */
public class UPV2Support {

	/** Private (static). */
	private UPV2Support() {
	}

	/** Installs UPV2 support on the toolkit. */
	public static void install() {
		// install XPath package

		// install parsers
		ParserFactory.instance().addRatsParser(XPathNodes.XPATH_UNIT,
				XPathParser.class);
	}

}
