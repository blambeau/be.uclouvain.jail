package be.uclouvain.jail.uinfo;

/** Populates a IUserInfo instance from another one. */
public interface IUserInfoPopulator<T> {
	
	/** Populates some target IUserInfo instance using a source one. */
	public void populate(IUserInfo target, T source);
	
}