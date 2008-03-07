package be.uclouvain.jail.algo.induct.compatibility;

import be.uclouvain.jail.algo.commons.Avoid;
import be.uclouvain.jail.fa.FAStateKind;
import be.uclouvain.jail.fa.functions.FAStateKindFunction;
import be.uclouvain.jail.fa.impl.AttributeGraphFAInformer;

/**
 * Provides the basic state kind compatibility.
 * 
 * @author blambeau
 */
public class StateKindCompatibility extends AbstractCompatibility {

	/** Function used to compute compatibility. */
	private FAStateKindFunction function;
	
	/** Creates a compatibility instance. */
	public StateKindCompatibility() {
		function = new FAStateKindFunction(FAStateKindFunction.OR,FAStateKindFunction.OR,true);
	}
	
	/** Checks compatibility of two state kinds. */
	@Override
	public boolean isCompatible(Object s, Object t) {
		FAStateKind si = (FAStateKind) getUserInfoAttr(s, AttributeGraphFAInformer.STATE_KIND_KEY);
		FAStateKind ti = (FAStateKind) getUserInfoAttr(t, AttributeGraphFAInformer.STATE_KIND_KEY);
		try { 
			function.compute(si, ti);
			return true;
		} catch (Avoid e) {
			return false;
		}
	}

	
	
}
