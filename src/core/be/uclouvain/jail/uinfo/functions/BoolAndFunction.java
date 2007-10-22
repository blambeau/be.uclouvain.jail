package be.uclouvain.jail.uinfo.functions;

/** Boolean AND function. */
public class BoolAndFunction extends AbstractCommutativeFunction<Boolean> {

	/** Computes Boolean OR. */
	@Override
	public Boolean compute(Boolean op1, Boolean op2) {
		return op1 && op2;
	}

}
