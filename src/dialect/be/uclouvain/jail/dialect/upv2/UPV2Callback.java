package be.uclouvain.jail.dialect.upv2;

import net.chefbe.autogram.ast2.IASTNode;
import net.chefbe.autogram.ast2.utils.CallbackBase;

/** Callback tool UPV2 grammar. */
public class UPV2Callback<T> extends CallbackBase<T> {

	/** Creates a callback instance. */
	public UPV2Callback() {
		super(UPV2Nodes.values(), UPV2Callback.class);
	}

	/** Callback method for GRAPHDEF nodes. */
	public T GRAPHDEF(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for STATE_LINE nodes. */
	public T STATE_LINE(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for EDGE_LINE nodes. */
	public T EDGE_LINE(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for ATTRIBUTES nodes. */
	public T ATTRIBUTES(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for ATTRIBUTE nodes. */
	public T ATTRIBUTE(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

}
