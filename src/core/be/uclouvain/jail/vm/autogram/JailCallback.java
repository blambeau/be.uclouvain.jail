package be.uclouvain.jail.vm.autogram;

import net.chefbe.autogram.ast2.IASTNode;
import net.chefbe.autogram.ast2.utils.CallbackBase;

/** Callback tool Jail grammar. */
public class JailCallback<T> extends CallbackBase<T> {

	/** Creates a callback instance. */
	public JailCallback() {
		super(JailNodes.values(), JailCallback.class);
	}

	/** Callback method for UNIT nodes. */
	public T UNIT(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for AFFECTATION nodes. */
	public T AFFECTATION(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for SHOW nodes. */
	public T SHOW(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for GLITERAL nodes. */
	public T GLITERAL(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for GOPERATOR nodes. */
	public T GOPERATOR(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for GOPERANDS nodes. */
	public T GOPERANDS(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for GOPERAND nodes. */
	public T GOPERAND(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for OPTIONS nodes. */
	public T OPTIONS(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for OPTION nodes. */
	public T OPTION(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for OPTMATCH nodes. */
	public T OPTMATCH(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for OPTLITERAL nodes. */
	public T OPTLITERAL(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for LITERAL nodes. */
	public T LITERAL(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for DEFINE nodes. */
	public T DEFINE(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for DEF_HEADER nodes. */
	public T DEF_HEADER(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for DEF_OPERANDS nodes. */
	public T DEF_OPERANDS(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for DEF_OPTIONS nodes. */
	public T DEF_OPTIONS(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for DEF_OPTION nodes. */
	public T DEF_OPTION(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for DEF_BODY nodes. */
	public T DEF_BODY(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for PHOLDERDEF nodes. */
	public T PHOLDERDEF(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for VARREF nodes. */
	public T VARREF(IASTNode node) throws Exception {
		return nonOverrided(node);
	}
}
