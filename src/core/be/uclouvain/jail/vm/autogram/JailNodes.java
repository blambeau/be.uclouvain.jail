package be.uclouvain.jail.vm.autogram;

import net.chefbe.autogram.ast2.IASTType;
import net.chefbe.autogram.utils.NamingUtils;

/** AST node types for Jail grammar. */
public enum JailNodes implements IASTType {

	/** UNIT node marker. */
	UNIT,

	/** AFFECTATION node marker. */
	AFFECTATION,

	/** SHOW node marker. */
	SHOW,

	/** GLITERAL node marker. */
	GLITERAL,

	/** GOPERATOR node marker. */
	GOPERATOR,

	/** GOPERANDS node marker. */
	GOPERANDS,

	/** GOPERAND node marker. */
	GOPERAND,

	/** OPTIONS node marker. */
	OPTIONS,

	/** OPTION node marker. */
	OPTION,

	/** ATTRS_EXPR node marker. */
	ATTRS_EXPR,

	/** ATTR_AFFECT node marker. */
	ATTR_AFFECT,

	/** ATTRREF node marker. */
	ATTRREF,

	/** VARREF node marker. */
	VARREF;

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
