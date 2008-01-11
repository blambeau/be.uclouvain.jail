package be.uclouvain.jail.vm.autogram.v2;

import net.chefbe.autogram.ast2.parsing.ParserFactory;
import net.chefbe.autogram.ast2.xpath.XPathNodes;
import net.chefbe.autogram.ast2.xpath.internal.XPathParser;

/** Manages toolkit initialization for JailUtils grammar support. */
public class JailUtilsSupport {

	/** Private (static). */
	private JailUtilsSupport() {
	}

	/** Installs JailUtils support on the toolkit. */
	public static void install() {
		// install XPath package

		// install parsers
		ParserFactory.instance().addRatsParser(XPathNodes.XPATH_UNIT, XPathParser.class);
	}

}
