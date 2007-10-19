package be.uclouvain.jail.vm.autogram;

import net.chefbe.autogram.ast2.IASTType;
import net.chefbe.autogram.utils.NamingUtils;

/** AST node types for Jail grammar. */
public enum JailNodes implements IASTType {

	/** UNIT node marker. */
	UNIT,

	/** PLUGIN_COMMAND node marker. */
	PLUGIN_COMMAND,

	/** ARGUMENT node marker. */
	ARGUMENT;

	/** ID of AGPEG Grammar. */
	public static final String GRAMMAR_ID = "jail";

	/** Returns the qualified name. */
	public String qName() {
		return namespace() + ":" + toString();
	}

	/** Returns the namespace. */
	public String namespace() {
		return GRAMMAR_ID;
	}

	/** Returns type for a qualified name. */
	public IASTType typeFor(String s) {
		return valueOf(NamingUtils.upperName(s));
	}

}
