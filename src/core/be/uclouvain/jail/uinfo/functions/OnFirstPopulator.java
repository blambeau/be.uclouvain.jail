package be.uclouvain.jail.uinfo.functions;

import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.IUserInfoPopulator;

/** 
 * Populator which install a different value on first call than
 * on next ones.
 */
public class OnFirstPopulator<T> implements IUserInfoPopulator<T> {

	/** Attribute to install. */
	private String attr;
	
	/** First value? */
	private boolean first = true;
	
	/** On first object. */
	private Object onFirst;
	
	/** On next object. */
	private Object onNext;
	
	/** Creates a populator instance. */
	public OnFirstPopulator(String attr, Object onFirst, Object onNext) {
		this.attr = attr;
		this.onFirst = onFirst;
		this.onNext = onNext;
	}

	/** Populates the target. */
	public void populate(IUserInfo target, T source) {
		target.setAttribute(attr, first ? onFirst : onNext);
		first = false;
	}

}
