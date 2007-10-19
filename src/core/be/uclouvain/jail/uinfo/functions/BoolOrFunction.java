package be.uclouvain.jail.uinfo.functions;

/** Boolean OR function. */
public class BoolOrFunction extends AbstractCommutativeFunction<Boolean> {

	/** Returns false. */
	@Override
	protected Boolean neutral() {
		return Boolean.FALSE;
	}

	/** Computes Boolean OR. */
	@Override
	public Boolean compute(Boolean op1, Boolean op2) {
		return op1 || op2;
	}

}
