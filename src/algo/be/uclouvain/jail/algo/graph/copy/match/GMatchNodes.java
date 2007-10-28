package be.uclouvain.jail.algo.graph.copy.match;

import net.chefbe.autogram.ast2.IASTType;
import net.chefbe.autogram.utils.NamingUtils;

/** AST node types for GMatch grammar. */
public enum GMatchNodes implements IASTType {

	/** MATCH_TEST node marker. */
	MATCH_TEST,

	/** MATCH_RULE node marker. */
	MATCH_RULE,

	/** MATCH_PATH node marker. */
	MATCH_PATH,

	/** MATCH_DO node marker. */
	MATCH_DO,

	/** MATCH_DOATTR node marker. */
	MATCH_DOATTR,

	/** ATTR_NAMEDEF node marker. */
	ATTR_NAMEDEF,

	/** ATTR_REF node marker. */
	ATTR_REF,

	/** CASE_EXPR node marker. */
	CASE_EXPR,

	/** WHEN_EXPR node marker. */
	WHEN_EXPR,

	/** ELSE_EXPR node marker. */
	ELSE_EXPR,

	/** FUNCTION_CALL node marker. */
	FUNCTION_CALL,

	/** LITERAL node marker. */
	LITERAL,

	/** BOOL_OREXPR node marker. */
	BOOL_OREXPR,

	/** BOOL_ANDEXPR node marker. */
	BOOL_ANDEXPR,

	/** BOOL_NOTEXPR node marker. */
	BOOL_NOTEXPR,

	/** BOOL_DYADEXPR node marker. */
	BOOL_DYADEXPR,

	/** BOOL_BOOLTERM node marker. */
	BOOL_BOOLTERM;

	/** ID of AGPEG Grammar. */
	public static final String GRAMMAR_ID = "gm";

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
