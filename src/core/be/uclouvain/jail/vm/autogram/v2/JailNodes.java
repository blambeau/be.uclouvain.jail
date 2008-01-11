package be.uclouvain.jail.vm.autogram.v2;

import net.chefbe.autogram.ast2.IASTType;
import net.chefbe.autogram.utils.NamingUtils;

/** AST node types for Jail grammar. */
public enum JailNodes implements IASTType {

	/** UNIT node marker. */
	UNIT,

	/** USE node marker. */
	USE,

	/** NATIVE node marker. */
	NATIVE,

	/** DEFINE node marker. */
	DEFINE,

	/** AFFECT node marker. */
	AFFECT,

	/** EVAL node marker. */
	EVAL,

	/** SIGNATURE node marker. */
	SIGNATURE,

	/** PARAMETERS node marker. */
	PARAMETERS,

	/** OPTPARAMS node marker. */
	OPTPARAMS,

	/** OPTPARAM node marker. */
	OPTPARAM,

	/** SINGPARAM node marker. */
	SINGPARAM,

	/** ARGSPARAM node marker. */
	ARGSPARAM,

	/** PICKPARAM node marker. */
	PICKPARAM,

	/** FUNCCALL node marker. */
	FUNCCALL,

	/** ARGS node marker. */
	ARGS,

	/** ARG node marker. */
	ARG,

	/** OPTARGS node marker. */
	OPTARGS,

	/** OPTARG node marker. */
	OPTARG,

	/** DIALECTLIT node marker. */
	DIALECTLIT,

	/** ARRAYLIT node marker. */
	ARRAYLIT,

	/** SETLIT node marker. */
	SETLIT,

	/** ATOMICLIT node marker. */
	ATOMICLIT,

	/** DIRECTREF node marker. */
	DIRECTREF,

	/** QUALIFIEDREF node marker. */
	QUALIFIEDREF,

	/** INDEXEDREF node marker. */
	INDEXEDREF,

	/** EXTENSIONREF node marker. */
	EXTENSIONREF;

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
