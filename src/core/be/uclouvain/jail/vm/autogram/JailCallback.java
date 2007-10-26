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

	/** Callback method for ATTRS_EXPR nodes. */
	public T ATTRS_EXPR(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for ATTR_AFFECT nodes. */
	public T ATTR_AFFECT(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for ATTRREF nodes. */
	public T ATTRREF(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for VARREF nodes. */
	public T VARREF(IASTNode node) throws Exception {
		return nonOverrided(node);
	}
}
