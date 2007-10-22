package be.uclouvain.jail.uinfo;

/** Installs a constant as attribute. */
public class ConstantPopulator<T> implements IUserInfoPopulator<T> {

	/** Installation attribute. */
	private String attr;
	
	/** Constant to install. */
	private Object constant;
	
	/** Creates a populator instance. */
	public ConstantPopulator(String attr, Object constant) {
		this.attr = attr;
		this.constant = constant;
	}
	
	/** Installs the constant inside target. */
	public void populate(IUserInfo target, T source) {
		target.setAttribute(attr,constant);
	}

}
