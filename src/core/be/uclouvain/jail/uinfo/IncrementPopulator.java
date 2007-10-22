package be.uclouvain.jail.uinfo;

/**
 * Increments some integer value.
 * 
 * @author blambeau
 */
public class IncrementPopulator<T> implements IUserInfoPopulator<T> {

	/** Attribute to install. */
	private String attr;
	
	/** Current value. */
	private Integer value;
	
	/** Increment to use. */
	private Integer increment = 1;
	
	/** 
	 * Creates a populator instance
	 * 
	 * @param attr key of the attribute to install.
	 * @param start incrementation starts at?
	 * @param increment increment to use.
	 */
	public IncrementPopulator(String attr, Integer start, Integer increment) {
		this.attr = attr;
		this.value = start;
		this.increment = increment;
	}
	
	/** Creates a populator instance which starts at 0 and increments by 1. */
	public IncrementPopulator(String attr) {
		this(attr,0,1);
	}
	
	/** Populates the IUserInfo. */
	public void populate(IUserInfo target, T source) {
		target.setAttribute(attr,value);
		value += increment;
	}

}
