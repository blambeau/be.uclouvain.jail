package be.uclouvain.jail.uinfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides an extensible implementation of IUserInfoCreator.
 * 
 * @author blambeau
 */
public class UserInfoCopier implements IUserInfoCreator<IUserInfo> {

	/** Populators to use. */
	private List<IUserInfoPopulator<IUserInfo>> populators;
	
	/** Creates a user info creator. */
	public UserInfoCopier() {
		this.populators = new ArrayList<IUserInfoPopulator<IUserInfo>>();
	}
	
	/** Adds a populator. */
	public UserInfoCopier addPopulator(IUserInfoPopulator<IUserInfo> populator) {
		if (populator == null) {
			throw new IllegalArgumentException("populator cannot be null.");
		}
		this.populators.add(populator);
		return this;
	}
	
	/** Adds a rule to keep some attributes. */
	public UserInfoCopier keep(String...attrs) {
		if (attrs == null || attrs.length==0) {
			throw new IllegalArgumentException("At least one attribute must be provided.");
		}
		return addPopulator(new CopyPopulator(attrs));
	}
	
	/** Adds a rule to keep all attributes. */
	public UserInfoCopier keepAll() {
		return addPopulator(new CopyPopulator());
	}
	
	/** Adds a rule to throw some attributes away. */ 
	public UserInfoCopier keepAllBut(String...attrs) {
		if (attrs == null || attrs.length==0) {
			throw new IllegalArgumentException("At least one attribute must be provided.");
		}
		return addPopulator(new CopyPopulator(true,attrs));
	}
	
	/** Adds a rule to rename some attribute. */
	public UserInfoCopier rename(String src, String trg) {
		if (src == null || trg == null) {
			throw new IllegalArgumentException("Source and target attributes are mandatory.");
		}
		return addPopulator(new RenamePopulator(src,trg));
	}
	
	/** Adds a rule which maps a constant to some attribute. */
	public UserInfoCopier addConstant(String attr, Object constant) {
		if (attr == null || constant == null) {
			throw new IllegalArgumentException("Attribute and constant are mandatory.");
		}
		return addPopulator(new ConstantPopulator<IUserInfo>(attr,constant));
	}
	
	/** Adds a rule which increments some attribute. */
	public UserInfoCopier addIncrement(String attr) {
		if (attr == null) {
			throw new IllegalArgumentException("Attribute is mandatory.");
		}
		return addPopulator(new IncrementPopulator<IUserInfo>(attr));
	}
	
	/** 
	 * Factors a IUserInfo instance.
	 * 
	 * <p>This method creates a MapUserInfo instance by default
	 * and may be overrided to create instances of another class.</p>
	 * 		if (attrs == null || attrs.length==0) {
			throw new IllegalArgumentException("At least one attribute must be provided.");
		}

	 * @return newly created instance of a IUserInfo.
	 */
	protected IUserInfo factor() {
		return new MapUserInfo();
	}
	
	/** Creates a IUserInfo instance from another one. */
	public IUserInfo create(IUserInfo info) {
		IUserInfo copy = factor();
		for (IUserInfoPopulator<IUserInfo> p : populators) {
			p.populate(copy, info);
		}
		return copy;
	}

}
