package be.uclouvain.jail.uinfo;

import java.util.Random;

/**
 * Base implementation for populating user infos randomly.
 * 
 * @author blambeau
 */
public abstract class AbstractRandomPopulator implements IUserInfoPopulator<Random> {

	/** Attribute to install. */
	protected String attr;
	
	/** Creates a instance of the populator. */
	public AbstractRandomPopulator(String attr) {
		this.attr = attr;
	}

	/** Chooses a value and set it to the target. */
	public void populate(IUserInfo target, Random r) {
		Object value = choose(r);
		target.setAttribute(attr, value);
	}
	
	/** Randomly chooses a value. */
	public abstract Object choose(Random r);
	
}
