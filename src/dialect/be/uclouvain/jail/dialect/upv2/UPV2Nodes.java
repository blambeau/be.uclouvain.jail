package be.uclouvain.jail.dialect.upv2;

import net.chefbe.autogram.ast2.IASTType;
import net.chefbe.autogram.utils.NamingUtils;

/** AST node types for UPV2 grammar. */
public enum UPV2Nodes implements IASTType {

	/** GRAPHDEF node marker. */
	GRAPHDEF,

	/** STATE_LINE node marker. */
	STATE_LINE,

	/** EDGE_LINE node marker. */
	EDGE_LINE,

	/** ATTRIBUTES node marker. */
	ATTRIBUTES,

	/** ATTRIBUTE node marker. */
	ATTRIBUTE;

	/** ID of AGPEG Grammar. */
	public static final String GRAMMAR_ID = "upv2";

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
