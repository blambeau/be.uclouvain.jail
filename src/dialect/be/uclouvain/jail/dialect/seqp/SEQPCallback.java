package be.uclouvain.jail.dialect.seqp;

import net.chefbe.autogram.ast2.IASTNode;
import net.chefbe.autogram.ast2.utils.CallbackBase;

/** Callback tool SEQP grammar. */
public class SEQPCallback<T> extends CallbackBase<T> {

	/** Creates a callback instance. */
	public SEQPCallback() {
		super(SEQPNodes.values(), SEQPCallback.class);
	}

	/** Callback method for TESTUNIT nodes. */
	public T TESTUNIT(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for SEQP_DEF nodes. */
	public T SEQP_DEF(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for STATEDEF nodes. */
	public T STATEDEF(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for ATTRIBUTES nodes. */
	public T ATTRIBUTES(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for ATTRDEF nodes. */
	public T ATTRDEF(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for PATH nodes. */
	public T PATH(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for EVENTREF nodes. */
	public T EVENTREF(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for STATEREF nodes. */
	public T STATEREF(IASTNode node) throws Exception {
		return nonOverrided(node);
	}
}
