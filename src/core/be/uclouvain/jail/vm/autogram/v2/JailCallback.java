package be.uclouvain.jail.vm.autogram.v2;

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

	/** Callback method for USE nodes. */
	public T USE(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for NATIVE nodes. */
	public T NATIVE(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for DEFINE nodes. */
	public T DEFINE(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for AFFECT nodes. */
	public T AFFECT(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for EVAL nodes. */
	public T EVAL(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for SIGNATURE nodes. */
	public T SIGNATURE(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for PARAMETERS nodes. */
	public T PARAMETERS(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for OPTPARAMS nodes. */
	public T OPTPARAMS(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for OPTPARAM nodes. */
	public T OPTPARAM(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for SINGPARAM nodes. */
	public T SINGPARAM(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for ARGSPARAM nodes. */
	public T ARGSPARAM(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for PICKPARAM nodes. */
	public T PICKPARAM(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for FUNCCALL nodes. */
	public T FUNCCALL(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for ARGS nodes. */
	public T ARGS(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for ARG nodes. */
	public T ARG(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for OPTARGS nodes. */
	public T OPTARGS(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for OPTARG nodes. */
	public T OPTARG(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for DIALECTLIT nodes. */
	public T DIALECTLIT(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for ARRAYLIT nodes. */
	public T ARRAYLIT(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for SETLIT nodes. */
	public T SETLIT(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for ATOMICLIT nodes. */
	public T ATOMICLIT(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for DIRECTREF nodes. */
	public T DIRECTREF(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for QUALIFIEDREF nodes. */
	public T QUALIFIEDREF(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for INDEXEDREF nodes. */
	public T INDEXEDREF(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for EXTENSIONREF nodes. */
	public T EXTENSIONREF(IASTNode node) throws Exception {
		return nonOverrided(node);
	}
}
