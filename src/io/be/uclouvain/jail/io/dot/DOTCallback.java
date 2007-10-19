package be.uclouvain.jail.io.dot;

import net.chefbe.autogram.ast2.IASTNode;
import net.chefbe.autogram.ast2.utils.CallbackBase;

/** Callback tool DOT grammar. */
public class DOTCallback<T> extends CallbackBase<T> {

	/** Creates a callback instance. */
	public DOTCallback() {
		super(DOTNodes.values(), DOTCallback.class);
	}

	/** Callback method for GRAPHDEF nodes. */
	public T GRAPHDEF(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for GRAPH_COMMONS nodes. */
	public T GRAPH_COMMONS(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for NODE_COMMONS nodes. */
	public T NODE_COMMONS(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for EDGE_COMMONS nodes. */
	public T EDGE_COMMONS(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for NODEDEF nodes. */
	public T NODEDEF(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for EDGEDEF nodes. */
	public T EDGEDEF(IASTNode node) throws Exception {
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
