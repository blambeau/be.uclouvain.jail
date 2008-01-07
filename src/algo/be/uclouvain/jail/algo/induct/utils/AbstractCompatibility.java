package be.uclouvain.jail.algo.induct.utils;

import be.uclouvain.jail.algo.induct.open.ICompatibility;

/** Helper to create compatibility informations. 
 * 
 * <p>This implementation allows compatibilies information to be kept in
 * a responsability chain. Two states are compatible if and only if all
 * compatibilities in the chain actually accord on that fact.</p>
 * 
 * <p>A good practice should be to put low-cost and high-failure-probability 
 * informers in the begining of the chain.</p>
 */
public abstract class AbstractCompatibility implements ICompatibility {

	/** Parent in the compatibility chain. */ 
	protected ICompatibility parent;

	/** Creates a compatibility with a parent. */
	public AbstractCompatibility(ICompatibility parent) {
		this.parent = parent;
	}

	/** Checks if two states are compatible. */
	public boolean isCompatible(Object p, Object q) {
		if (!doIsCompatible(p, q)) {
			return false;
		} else {
			return parent != null ? parent.isCompatible(p, q) : true;
		}
	}

	/** To be implemented. */
	protected abstract boolean doIsCompatible(Object obj, Object obj1);

}
