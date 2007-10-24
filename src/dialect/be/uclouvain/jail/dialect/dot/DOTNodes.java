package be.uclouvain.jail.dialect.dot;

import net.chefbe.autogram.ast2.IASTType;
import net.chefbe.autogram.utils.NamingUtils;

/** AST node types for DOT grammar. */
public enum DOTNodes implements IASTType {

	/** GRAPHDEF node marker. */
	GRAPHDEF,

	/** GRAPH_COMMONS node marker. */
	GRAPH_COMMONS,

	/** NODE_COMMONS node marker. */
	NODE_COMMONS,

	/** EDGE_COMMONS node marker. */
	EDGE_COMMONS,

	/** NODEDEF node marker. */
	NODEDEF,

	/** EDGEDEF node marker. */
	EDGEDEF,

	/** ATTRIBUTES node marker. */
	ATTRIBUTES,

	/** ATTRIBUTE node marker. */
	ATTRIBUTE;

	/** ID of AGPEG Grammar. */
	public static final String GRAMMAR_ID = "dot";

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
