package be.uclouvain.jail.uinfo;

/** Copies some attribute value. */
public class RenamePopulator implements IUserInfoPopulator<IUserInfo> {

	/** Source and target attributes. */
	private String srcAttr, trgAttr;
	
	/** Copies a source attribute value to a target attribute. */
	public RenamePopulator(String srcAttr, String trgAttr) {
		this.srcAttr = srcAttr;
		this.trgAttr = trgAttr;
	}
	
	/** Copies an attribute value without renaming. */
	public RenamePopulator(String attr) {
		this(attr,attr);
	}
	
	/** Copies source[srcAttr] into target[trgAttr]. */
	public void populate(IUserInfo target, IUserInfo source) {
		target.setAttribute(trgAttr, source.getAttribute(srcAttr));
	}

}