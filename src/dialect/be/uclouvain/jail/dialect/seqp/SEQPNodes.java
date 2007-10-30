package be.uclouvain.jail.dialect.seqp;

import net.chefbe.autogram.ast2.IASTType;
import net.chefbe.autogram.utils.NamingUtils;

/** AST node types for SEQP grammar. */
public enum SEQPNodes implements IASTType {

	/** TESTUNIT node marker. */
	TESTUNIT,

	/** SEQP_DEF node marker. */
	SEQP_DEF,

	/** STATEDEF node marker. */
	STATEDEF,

	/** ATTRIBUTES node marker. */
	ATTRIBUTES,

	/** ATTRDEF node marker. */
	ATTRDEF,

	/** PATH node marker. */
	PATH,

	/** EVENTREF node marker. */
	EVENTREF,

	/** STATEREF node marker. */
	STATEREF;

	/** ID of AGPEG Grammar. */
	public static final String GRAMMAR_ID = "seqp";

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
