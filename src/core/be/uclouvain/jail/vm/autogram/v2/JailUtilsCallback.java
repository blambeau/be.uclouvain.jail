package be.uclouvain.jail.vm.autogram.v2;

import net.chefbe.autogram.ast2.utils.CallbackBase;

/** Callback tool JailUtils grammar. */
public class JailUtilsCallback<T> extends CallbackBase<T> {

	/** Creates a callback instance. */
	public JailUtilsCallback() {
		super(JailUtilsNodes.values(), JailUtilsCallback.class);
	}

}
