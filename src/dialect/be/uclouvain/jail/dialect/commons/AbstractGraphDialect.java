package be.uclouvain.jail.dialect.commons;

import be.uclouvain.jail.dialect.IGraphDialect;
import be.uclouvain.jail.uinfo.IUserInfoHelper;

/**
 * Provides base implementation for graph dialects.
 * 
 * @author blambeau
 */
public abstract class AbstractGraphDialect implements IGraphDialect {

	/** Helper to use. */
	protected IUserInfoHelper uInfoHelper;
	
	/** Sets the helper to use. */
	public void setUserInfoHelper(IUserInfoHelper helper) {
		this.uInfoHelper = helper;
	}

}
