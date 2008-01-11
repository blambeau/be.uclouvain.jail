package be.uclouvain.jail.vm.autogram.v2;

import net.chefbe.autogram.ast2.parsing.ParserFactory;
import net.chefbe.autogram.ast2.xpath.XPathNodes;
import net.chefbe.autogram.ast2.xpath.internal.XPathParser;

/** Manages toolkit initialization for Jail grammar support. */
public class JailSupport {

	/** Private (static). */
	private JailSupport() {
	}

	/** Installs Jail support on the toolkit. */
	public static void install() {
		// install XPath package

		// install parsers
		ParserFactory.instance().addRatsParser(XPathNodes.XPATH_UNIT, XPathParser.class);
	}

}
