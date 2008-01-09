package be.uclouvain.jail.uinfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Provides a randomizer for user infos.
 * 
 * @author blambeau
 */
public class UserInfoRandomizer implements IUserInfoCreator<Random> {

	/** Populators to use. */
	private List<IUserInfoPopulator<Random>> populators;
	
	/** Creates a user info creator. */
	public UserInfoRandomizer() {
		this.populators = new ArrayList<IUserInfoPopulator<Random>>();
	}
	
	/** Adds a populator. */
	public UserInfoRandomizer addPopulator(IUserInfoPopulator<Random> populator) {
		if (populator == null) {
			throw new IllegalArgumentException("populator cannot be null.");
		}
		this.populators.add(populator);
		return this;
	}
	
	/** 
	 * Factors a IUserInfo instance.
	 * 
	 * <p>This method creates a MapUserInfo instance by default
	 * and may be overrided to create instances of another class.</p>
	 * 
	 * @return newly created instance of a IUserInfo.
	 */
	protected IUserInfo factor() {
		return new MapUserInfo();
	}
	
	/** Randomly creates a user info with populators. */
	public IUserInfo create(Random r) {
		IUserInfo copy = factor();
		for (IUserInfoPopulator<Random> p : populators) {
			p.populate(copy, r);
		}
		return copy;
	}

}
