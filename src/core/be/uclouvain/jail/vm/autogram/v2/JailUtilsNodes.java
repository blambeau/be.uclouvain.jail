package be.uclouvain.jail.vm.autogram.v2;

import net.chefbe.autogram.ast2.IASTType;
import net.chefbe.autogram.utils.NamingUtils;

/** AST node types for JailUtils grammar. */
public enum JailUtilsNodes implements IASTType {

	;

	/** ID of AGPEG Grammar. */
	public static final String GRAMMAR_ID = "u";
	
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

