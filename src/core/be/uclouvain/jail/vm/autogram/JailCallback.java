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

	/** Callback method for PLUGIN_COMMAND nodes. */
	public T PLUGIN_COMMAND(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for ARGUMENT nodes. */
	public T ARGUMENT(IASTNode node) throws Exception {
		return nonOverrided(node);
	}
}
